package com.bypass;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.ScrollView;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity {
    
    private static final String TAG = "MainActivity";
    private TextView logView;
    private StringBuilder logBuilder = new StringBuilder();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ScrollView scrollView = new ScrollView(this);
        logView = new TextView(this);
        logView.setPadding(20, 20, 20, 20);
        logView.setTextSize(14);
        scrollView.addView(logView);
        setContentView(scrollView);
        
        appendLog("=== 鐧炬鍔犲浐缁曡繃 APK ===");
        appendLog("鏃堕棿: " + getCurrentTime());
        appendLog("");
        appendLog("--- 鐜淇℃伅 ---");
        appendLog("鏋舵瀯: " + ArchHelper.getCurrentArch());
        appendLog("鐜: " + ArchHelper.getEnvironment());
        appendLog("Android: " + android.os.Build.VERSION.RELEASE);
        appendLog("");
        appendLog("--- 璁惧浼 ---");
        appendLog("Model: " + android.os.Build.MODEL);
        appendLog("Manufacturer: " + android.os.Build.MANUFACTURER);
        appendLog("Brand: " + android.os.Build.BRAND);
        appendLog("Device: " + android.os.Build.DEVICE);
        appendLog("Product: " + android.os.Build.PRODUCT);
        appendLog("Fingerprint: " + android.os.Build.FINGERPRINT);
        appendLog("");
        
        appendLog("--- 鍔犺浇 SO ---");
        try {
            NativeLoader.loadJiagu();
            appendLog("Status: Load success");
        } catch (Exception e) {
            appendLog("Status: Load failed");
            appendLog("閿欒: " + e.getMessage());
            Log.e(TAG, "SO 鍔犺浇澶辫触", e);
        }
    }
    
    private void appendLog(String msg) {
        logBuilder.append(msg).append("\n");
        logView.setText(logBuilder.toString());
        Log.i(TAG, msg);
    }
    
    private String getCurrentTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}
