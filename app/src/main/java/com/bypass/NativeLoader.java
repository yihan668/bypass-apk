package com.bypass;

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class NativeLoader {
    
    private static final String TAG = "NativeLoader";
    private static boolean loaded = false;
    
    public static synchronized void loadJiagu() {
        if (loaded) {
            return;
        }
        
        try {
            System.loadLibrary("jiagu");
            Log.i(TAG, "SO 鍔犺浇鎴愬姛 (jniLibs)");
            loaded = true;
            
        } catch (UnsatisfiedLinkError e1) {
            Log.w(TAG, "jniLibs 鍔犺浇澶辫触锛屽皾璇曚粠 assets 鍔犺浇");
            
            try {
                String soPath = extractFromAssets(
                    App.getAppContext(),
                    getSoFileName()
                );
                
                if (soPath != null) {
                    System.load(soPath);
                    Log.i(TAG, "SO 鍔犺浇鎴愬姛 (assets): " + soPath);
                    loaded = true;
                }
                
            } catch (Exception e2) {
                Log.e(TAG, "SO 鍔犺浇澶辫触: " + e2.getMessage());
            }
        }
    }
    
    private static String getSoFileName() {
        String arch = ArchHelper.getCurrentArch();
        
        if (arch.contains("arm64")) {
            return "libjiagu_arm64.so";
        } else if (arch.contains("armeabi") || arch.contains("arm")) {
            return "libjiagu_arm32.so";
        } else if (arch.contains("x86_64")) {
            return "libjiagu_x86_64.so";
        } else if (arch.contains("x86")) {
            return "libjiagu_x86.so";
        }
        
        return "libjiagu.so";
    }
    
    private static String extractFromAssets(Context context, String soName) {
        try {
            File outFile = new File(context.getCacheDir(), soName);
            
            if (outFile.exists() && outFile.length() > 0) {
                return outFile.getAbsolutePath();
            }
            
            InputStream is = context.getAssets().open(soName);
            FileOutputStream fos = new FileOutputStream(outFile);
            
            byte[] buffer = new byte[8192];
            int len;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            
            fos.close();
            is.close();
            
            return outFile.getAbsolutePath();
            
        } catch (Exception e) {
            Log.e(TAG, "鎻愬彇 SO 澶辫触: " + e.getMessage());
            return null;
        }
    }
    
    public static boolean isLoaded() {
        return loaded;
    }
}
