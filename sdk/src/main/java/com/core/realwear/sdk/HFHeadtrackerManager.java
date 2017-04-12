package com.core.realwear.sdk;
/*------------------- COPYRIGHT AND TRADEMARK INFORMATION -------------------+
  |
  |    WearNext Development Software, Source Code and Object Code
  |    (c) 2015, 2016 WearNext, Inc. All rights reserved.
  |
  |    Contact info@wearnext.com for further information about the use of
  |    this code.
  |
  +--------------------------------------------------------------------------*/


/*----------------------- SOURCE MODULE INFORMATION -------------------------+
 |
 | Source Name:  Headtracker Manager
 |
 | Handles the headtracking
 |
 | Version: v1.0
 | Date: January 2016
 | Author: Chris Parkinson
 |
  +--------------------------------------------------------------------------*/

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.util.Log;

import java.util.List;

public class HFHeadtrackerManager implements SensorEventListener {
    private static final String TAG = "HeadtrackerManager";

    public static final int TRACK_HORIZONTAL_MOTION = 0;
    public static final int TRACK_VERTICAL_MOTION = 1;
    public static final int TRACK_ALL_MOTION = 2;

    private static final int DEVICE_HMT = 0;
    private static final int DEVICE_HDK = 1;
    private int deviceType;

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private HFHeadtrackerListener eventListener;
    private int trackingMode;

    private float minMovementThreshold; //Reject movement under this amount
    private float xScaleFactor;  //Final scale applied to x offsets ..-ve changes direction
    private float yScaleFactor;
    private boolean antiJitter;
    private float lastXValue = 0;
    private float lastXDeltaValue;
    private float lastYValue = 0;
    private float lastYDeltaValue;
    private int trendCounterX, trendCounterY; //Simple Anti jitter filter

    public HFHeadtrackerManager(HFHeadtrackerListener eventListener) {
        this.eventListener = eventListener;
        deviceType = getDeviceType();
    }

    public void startHeadtracker(Context context, int trackingMode) {
        if (eventListener == null) return; //No one listening, so don't start
        this.trackingMode = trackingMode;

        //Each device has specific tuning parameters here...
        int sensorSpeed = SensorManager.SENSOR_DELAY_FASTEST;

        if (deviceType == DEVICE_HMT) {
            minMovementThreshold = 0.02f;
            xScaleFactor = -100f;
            yScaleFactor = 100f;
            antiJitter = true;
            sensorSpeed = SensorManager.SENSOR_DELAY_FASTEST;

        }

        if (deviceType == DEVICE_HDK) {
            minMovementThreshold = 0.02f;
            xScaleFactor = -20f;
            yScaleFactor = -15f;
            antiJitter = true;
            sensorSpeed = SensorManager.SENSOR_DELAY_FASTEST;
        }


        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        //Get list of all sensors on device
        List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        for (int i = 0; i < sensorList.size(); i++) {
            final Sensor sensor = sensorList.get(i);
            Log.d(TAG, "Sensor " + i + " = " + sensor.getName() + "  Vendor: " + sensor.getVendor() + "  Version: " + sensor.getVersion());
        }

        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        Log.d(TAG, "Default Sensor = " + mSensor.getName());

        mSensorManager.registerListener(this, mSensor, sensorSpeed);
        Log.d(TAG, "Registering Headtracker");
    }

    public void stopHeadtracker() {
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
            Log.d(TAG, "Unregistering Headtracker");
        }

        mSensorManager = null;
        mSensor = null;
    }

    public void onSensorChanged(SensorEvent event) {
        float x = 0;
        float y = 0;
        float z = 0;

        //  Log.d(TAG, "Sensor Changed " + System.currentTimeMillis());

        if (deviceType == DEVICE_HDK) {
            x = event.values[1];
            y = event.values[2];
            z = event.values[0];
        }

        if (deviceType == DEVICE_HMT) {
            x = event.values[0];
            y = -event.values[1];
            z = event.values[2];
        }

        int newXOffset = 0;
        int newYOffset = 0;


        //Process X-direction
        if (trackingMode == TRACK_HORIZONTAL_MOTION || trackingMode == TRACK_ALL_MOTION) {
            //Following two lines throw out the finest movement to attempt to smooth the motion
            float deltaX = (x - lastXValue);
            if (deltaX < -minMovementThreshold || deltaX > minMovementThreshold) {
                //Crude Anti-Jitter filter
                if (antiJitter) {
                    if (lastXDeltaValue < 0 && deltaX < 0) trendCounterX++;
                    if (lastXDeltaValue > 0 && deltaX > 0) trendCounterX++;
                    if (lastXDeltaValue < 0 && deltaX > 0) trendCounterX = 0;
                    if (lastXDeltaValue > 0 && deltaX < 0) trendCounterX = 0;
                    if (trendCounterX > 3) newXOffset = (int) (deltaX * xScaleFactor);
                } else newXOffset = (int) (deltaX * xScaleFactor);
                lastXValue = x;
                lastXDeltaValue = deltaX;
            }
        }

        //Process Y-direction
        if (trackingMode == TRACK_VERTICAL_MOTION || trackingMode == TRACK_ALL_MOTION) {
            //Following two lines throw out the finest movement to attempt to smooth the motion
            float deltaY = (y - lastYValue);
            if (deltaY < -minMovementThreshold || deltaY > minMovementThreshold) {

                //Crude Anti-Jitter filter
                if (antiJitter == true) {
                    if (lastYDeltaValue < 0 && deltaY < 0) trendCounterY++;
                    if (lastYDeltaValue > 0 && deltaY > 0) trendCounterY++;
                    if (lastYDeltaValue < 0 && deltaY > 0) trendCounterY = 0;
                    if (lastYDeltaValue > 0 && deltaY < 0) trendCounterY = 0;
                    if (trendCounterY > 3) newYOffset = (int) (deltaY * yScaleFactor);
                } else newYOffset = (int) (deltaY * yScaleFactor);

                lastYValue = y;
                lastYDeltaValue = deltaY;
            }
        }

        //Only allow movement in one axis, the bigger one
        if (Math.abs(newXOffset) > Math.abs(newYOffset)) newYOffset = 0;
        else newXOffset = 0;
        //   Log.d(TAG, "xyz = " + x + ", " + y + ", " + z );
        if (newXOffset == 0 && newYOffset == 0) return;
        if (eventListener != null) eventListener.onHeadMoved(newXOffset, newYOffset);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public static int getDeviceType() {
        if (Build.DEVICE.equals("bullhead")) return DEVICE_HDK;
        if (Build.PRODUCT.equals("HMT-1")) return DEVICE_HMT;

        return DEVICE_HMT;
    }
}
