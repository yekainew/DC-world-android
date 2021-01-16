package com.jkkg.hhtx.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.jkkg.hhtx.app.MyApp;

import timber.log.Timber;

public class SpUtil {

    // 首选项名称
    public static final String PREFERENCE_NAME= MyApp.getApp().getPackageName()+"PREFERENCE";

    private static SpUtil preferenceUitl;

    private SharedPreferences sp;

    private SharedPreferences.Editor ed;

    private SpUtil(Context context) {
        init(context);
    }

    public void init(Context context) {
        if(sp == null || ed == null) {
            try {
                sp=context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
                ed=sp.edit();
            } catch(Exception e) {
            }
        }
    }

    public static SpUtil getInstance(Context context) {
        if(preferenceUitl == null) {
            preferenceUitl=new SpUtil(context);
        }
        preferenceUitl.init(context);
        return preferenceUitl;
    }

    public void saveLong(String key, long l) {
        ed.putLong(key, l);
        ed.commit();
    }

    public long getLong(String key, long defaultlong) {
        return sp.getLong(key, defaultlong);
    }

    public void saveBoolean(String key, boolean value) {
        ed.putBoolean(key, value);
        ed.commit();
    }

    public boolean getBoolean(String key, boolean defaultboolean) {
        return sp.getBoolean(key, defaultboolean);
    }

    public void saveInt(String key, int value) {
        if(ed != null) {
            ed.putInt(key, value);
            ed.commit();
        } else {
                Timber.e("PreferenceUitl.saveInt ed is null");
        }
    }

    public void saveFloat(String key, float value) {
        if(ed != null) {
            ed.putFloat(key, value);
            ed.commit();
        } else {
            Timber.e("PreferenceUitl.saveFloat ed is null");
        }
    }

    public int getInt(String key, int defaultInt) {
        return sp.getInt(key, defaultInt);
    }

    public float getFloat(String key, float defaultInt) {
        return sp.getFloat(key, defaultInt);
    }

    public String getString(String key, String defaultInt) {
        return sp.getString(key, defaultInt);
    }

    public String getString(Context context, String key, String defaultValue) {
        if(sp == null || ed == null) {
            sp=context.getSharedPreferences(PREFERENCE_NAME, 0);
            ed=sp.edit();
        }
        if(sp != null) {
            return sp.getString(key, defaultValue);
        }
        return defaultValue;
    }

    public void saveString(String key, String value) {
        ed.putString(key, value);
        ed.commit();
    }

    public void remove(String key) {
        ed.remove(key);
        ed.commit();
    }
    public void removeAll() {
        ed.clear();
        ed.commit();
    }

    public void destroy() {
        sp=null;
        ed=null;
        preferenceUitl=null;
    }
}
