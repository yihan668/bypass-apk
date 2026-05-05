package com.bypass;

import android.os.Build;

public class ArchHelper {
    
    public static String getCurrentArch() {
        String[] abis = Build.SUPPORTED_ABIS;
        if (abis != null && abis.length > 0) {
            return abis[0];
        }
        return "unknown";
    }
    
    public static boolean isARM64() {
        String arch = getCurrentArch();
        return arch.contains("arm64") || arch.contains("aarch64");
    }
    
    public static boolean isX86() {
        String arch = getCurrentArch();
        return arch.contains("x86");
    }
    
    public static String getEnvironment() {
        if (isEmulator()) {
            return "妯℃嫙鍣?(" + getCurrentArch() + ")";
        } else {
            return "鐪熸満 (" + getCurrentArch() + ")";
        }
    }
    
    public static boolean isEmulator() {
        return (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                || "google_sdk".equals(Build.PRODUCT));
    }
}
