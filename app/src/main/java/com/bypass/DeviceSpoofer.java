package com.bypass;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import org.json.JSONObject;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class DeviceSpoofer {
    
    private static final String TAG = "DeviceSpoofer";
    private static DeviceSpoofer instance;
    private Context context;
    private JSONObject config;
    
    public static DeviceSpoofer getInstance() {
        if (instance == null) {
            instance = new DeviceSpoofer();
        }
        return instance;
    }
    
    public void init(Context ctx) {
        this.context = ctx.getApplicationContext();
        loadConfig();
    }
    
    private void loadConfig() {
        try {
            InputStream is = context.getAssets().open("device_config.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            
            String json = new String(buffer, "UTF-8");
            config = new JSONObject(json);
            Log.i(TAG, "Config loaded");
        } catch (Exception e) {
            Log.e(TAG, "Config load failed, using defaults");
            config = getDefaultConfig();
        }
    }
    
    private JSONObject getDefaultConfig() {
        try {
            JSONObject cfg = new JSONObject();
            cfg.put("fingerprint", "google/sailfish/sailfish:8.1.0/OPM1.171019.011/4767987:user/release-keys");
            cfg.put("model", "Pixel 2");
            cfg.put("manufacturer", "Google");
            cfg.put("brand", "google");
            cfg.put("device", "sailfish");
            cfg.put("product", "sailfish");
            cfg.put("hardware", "sailfish");
            cfg.put("board", "sailfish");
            cfg.put("serial", "0123456789ABCDEF");
            cfg.put("imei", "359881060236487");
            cfg.put("imsi", "460001234567890");
            cfg.put("mac", "00:11:22:33:44:55");
            cfg.put("android_id", "a1b2c3d4e5f6g7h8");
            return cfg;
        } catch (Exception e) {
            return new JSONObject();
        }
    }
    
    public void spoofAll() {
        spoofBuild();
        spoofAndroidId();
        Log.i(TAG, "All spoofing completed");
    }
    
    private void spoofBuild() {
        try {
            setStaticField(Build.class, "FINGERPRINT", config.optString("fingerprint"));
            setStaticField(Build.class, "MODEL", config.optString("model"));
            setStaticField(Build.class, "MANUFACTURER", config.optString("manufacturer"));
            setStaticField(Build.class, "BRAND", config.optString("brand"));
            setStaticField(Build.class, "DEVICE", config.optString("device"));
            setStaticField(Build.class, "PRODUCT", config.optString("product"));
            setStaticField(Build.class, "HARDWARE", config.optString("hardware"));
            setStaticField(Build.class, "BOARD", config.optString("board"));
            
            if (Build.VERSION.SDK_INT < 28) {
                setStaticField(Build.class, "SERIAL", config.optString("serial"));
            }
            
            Log.i(TAG, "Build 浼瀹屾垚: " + Build.MODEL);
        } catch (Exception e) {
            Log.e(TAG, "Build 浼澶辫触: " + e.getMessage());
        }
    }
    
    private void spoofAndroidId() {
        try {
            String androidId = config.optString("android_id");
            Settings.Secure.putString(
                context.getContentResolver(),
                Settings.Secure.ANDROID_ID,
                androidId
            );
            Log.i(TAG, "Android ID 浼瀹屾垚: " + androidId);
        } catch (Exception e) {
            Log.e(TAG, "Android ID 浼澶辫触: " + e.getMessage());
        }
    }
    
    private void setStaticField(Class<?> clazz, String fieldName, Object value) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            
            field.set(null, value);
        } catch (Exception e) {
            Log.e(TAG, "璁剧疆瀛楁澶辫触 " + fieldName + ": " + e.getMessage());
        }
    }
    
    public String getSpoofedIMEI() {
        return config.optString("imei");
    }
    
    public String getSpoofedMAC() {
        return config.optString("mac");
    }
}
