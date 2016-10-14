package com.core.realwear.sdk.Util;

/**
 * Created by Fin on 05/09/2016.
 */
public class ExtensionTypes {

    public enum Supported{
        PDF,
        PNG,
        JPEG,
        JPG,
        MP4,
        MKV,
        GPP,
        WEBM,
        BMP, NOT_SUPPORTED
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

    public static Supported ParseLocationToType(String location)
    {
        if(location.toLowerCase().endsWith(PNG)){
            return Supported.PNG;
        }else if(location.toLowerCase().endsWith(PDF)){
            return Supported.PDF;
        }else if(location.toLowerCase().endsWith(JPEG)) {
            return Supported.JPEG;
        }else if(location.toLowerCase().endsWith(JPG)) {
            return Supported.JPG;
        }else if(location.toLowerCase().endsWith(MP4)) {
            return Supported.MP4;
        }else if(location.toLowerCase().endsWith(BMP)) {
            return Supported.BMP;
        }
        else if(location.toLowerCase().endsWith(BMP)) {
            return Supported.BMP;
        }
        else if(location.toLowerCase().endsWith(MKV)) {
            return Supported.MKV;
        }
        else if(location.toLowerCase().endsWith(GPP)) {
            return Supported.GPP;
        }
        else if(location.toLowerCase().endsWith(GP)) {
            return Supported.GPP;
        }
        else if(location.toLowerCase().endsWith(WEBM)) {
            return Supported.WEBM;
        }

        return Supported.NOT_SUPPORTED;
    }

}
