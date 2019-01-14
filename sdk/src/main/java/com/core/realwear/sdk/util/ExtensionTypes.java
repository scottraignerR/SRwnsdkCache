/*
 * RealWear Development Software, Source Code and Object Code
 * (c) RealWear, Inc. All rights reserved.
 * <p>
 * Contact info@realwear.com for further information about the use of this code.
 */
package com.core.realwear.sdk.util;

/**
 * Created by Fin on 05/09/2016.
 */
public class ExtensionTypes {

    public enum Supported{
        PDF,
        PNG,
        JPEG,
        MP4,
        MKV,
        GPP,
        WEBM,
        WEBP,
        BMP,
        MOV,
        AMR,
        WMA,
        MP3,
        WAV,
        OGG,
        MIDI,
        AVI,
        GIF,
        NOT_SUPPORTED
    }

    public static final String PNG = ".png";
    public static final String PDF = ".pdf";
    public static final String BMP = ".bmp";
    public static final String JPEG = ".jpeg";
    public static final String JPG = ".jpg";
    public static final String MP4 = ".mp4";
    public static final String MKV = ".mkv";
    public static final String GP = ".3gp";
    public static final String GPP = ".3gpp";
    public static final String WEBM = ".webm";
    public static final String WEBP = ".webp";
    public static final String MOV = ".mov";
    public static final String AMR = ".amr";
    public static final String AWB = ".awb";
    public static final String AMRWB = ".amrwb";
    public static final String WMA = ".wma";
    public static final String MP3 = ".mp3";
    public static final String WAV = ".wav";
    public static final String OGG = ".ogg";
    public static final String MIDI = ".midi";
    public static final String AVI = ".avi";
    public static final String GIF = ".gif";

    public static Supported ParseLocationToType(String location)
    {
        if(location.toLowerCase().endsWith(PNG)){
            return Supported.PNG;
        }else if(location.toLowerCase().endsWith(PDF)){
            return Supported.PDF;
        }else if(location.toLowerCase().endsWith(JPEG) || location.toLowerCase().endsWith(JPG)) {
            return Supported.JPEG;
        }else if(location.toLowerCase().endsWith(MP4)) {
            return Supported.MP4;
        }else if(location.toLowerCase().endsWith(BMP)) {
            return Supported.BMP;
        }
        else if(location.toLowerCase().endsWith(WEBP)) {
            return Supported.WEBP;
        }
        else if(location.toLowerCase().endsWith(MKV)) {
            return Supported.MKV;
        }
        else if(location.toLowerCase().endsWith(GPP) || location.toLowerCase().endsWith(GP)) {
            return Supported.GPP;
        }
        else if(location.toLowerCase().endsWith(WEBM)) {
            return Supported.WEBM;
        }
        else if(location.toLowerCase().endsWith(MOV)) {
            return Supported.MOV;
        }
        else if(location.toLowerCase().endsWith(AMR) || location.toLowerCase().endsWith(AWB) || location.toLowerCase().endsWith(AMRWB)) {
            return Supported.AMR;
        }
        else if(location.toLowerCase().endsWith(WMA)) {
            return Supported.WMA;
        }
        else if(location.toLowerCase().endsWith(MP3)) {
            return Supported.MP3;
        }
        else if(location.toLowerCase().endsWith(WAV)) {
            return Supported.WAV;
        }
        else if(location.toLowerCase().endsWith(OGG)) {
            return Supported.OGG;
        }
        else if(location.toLowerCase().endsWith(MIDI)) {
            return Supported.MIDI;
        }
        else if(location.toLowerCase().endsWith(AVI)) {
            return Supported.AVI;
        }
        else if(location.toLowerCase().endsWith(GIF)) {
            return Supported.GIF;
        }

        return Supported.NOT_SUPPORTED;
    }

    public static boolean isImage(String location) {
        switch (ParseLocationToType(location)) {
            case PNG:
            case JPEG:
            case WEBP:
            case BMP:
            case GIF:
                return true;

            default:
                return false;
        }
    }

    public static boolean isVideo(String location, boolean strict) {
        switch (ParseLocationToType(location)) {
            case MP4:
            case MKV:
            case GPP:
                if (strict) {
                    return false;
                }
            case WEBM:
            case MOV:
            case AVI:
                return true;

            default:
                return false;
        }
    }

    public static boolean isAudio(String location, boolean strict) {
        switch (ParseLocationToType(location)) {
            case GPP:
                if (strict) {
                    return false;
                }
            case AMR:
            case WMA:
            case MP3:
            case WAV:
            case OGG:
            case MIDI:
                return true;

            default:
                return false;
        }
    }

    public static boolean isPdf(String location) {
        return ParseLocationToType(location) == Supported.PDF;
    }

}
