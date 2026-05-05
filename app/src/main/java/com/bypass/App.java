package com.bypass;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class App extends Application {
    
    private static final String TAG = "BypassApp";
    private static Context appContext;
    
    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        
        Log.i(TAG, "=== 鐧炬鍔犲浐缁曡繃 APK 鍚姩 ===");
        Log.i(TAG, "褰撳墠鏋舵瀯: " + ArchHelper.getCurrentArch());
        Log.i(TAG, "杩愯鐜: " + ArchHelper.getEnvironment());
        
        // 1. 璁惧浼锛堝繀椤诲湪鍔犺浇 SO 鍓嶆墽琛岋級
        DeviceSpoofer.getInstance().init(this);
        DeviceSpoofer.getInstance().spoofAll();
        
        Log.i(TAG, "=== 璁惧浼瀹屾垚 ===");
    }
    
    public static Context getAppContext() {
        return appContext;
    }
}
