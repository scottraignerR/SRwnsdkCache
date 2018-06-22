package com.core.realwear.sdk.util;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.util.Log;
import android.util.SizeF;

public class DeviceUtils {
    public static final String TAG = "DeviceUtils";

    public enum CameraSensor {
        SENSOR_1, SENSOR_2, UNKNOWN
    }

    public static boolean isHoneywellDevice() {
        return Build.DISPLAY.endsWith("HW");
    }

    public static CameraSensor getCameraSensorType(Context context) {
        CameraManager mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);

        if (mCameraManager == null) {
            return CameraSensor.UNKNOWN;
        }

        try {
            for (String cameraId : mCameraManager.getCameraIdList()) {
                CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(cameraId);

                // We don't use a front facing camera in this sample.
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }
                StreamConfigurationMap map = characteristics.get(
                        CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                if (map == null) {
                    continue;
                }

                SizeF sizeF = characteristics.get(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE);
                if (sizeF == null) {
                    continue;
                }
                if (5.23 < sizeF.getWidth() && sizeF.getWidth() < 5.24 &&
                        3.93 < sizeF.getHeight() && sizeF.getHeight() < 3.94) {
                    return CameraSensor.SENSOR_1;
                } else if (4.65 < sizeF.getWidth() && sizeF.getWidth() < 4.66 &&
                        3.4 < sizeF.getHeight() && sizeF.getHeight() < 3.6) {
                    return CameraSensor.SENSOR_2;
                } else {
                    return CameraSensor.UNKNOWN;
                }
            }
        } catch (CameraAccessException e) {
            Log.w(TAG, e);
        }
        return CameraSensor.UNKNOWN;
    }
}
