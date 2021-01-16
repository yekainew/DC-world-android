package com.jkkg.hhtx.utils;

import android.text.TextUtils;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2019/6/16.
 */

public class TryParse {

    public static String getString(Object name) {
        if (name == null) {
            return "";
        } else if (name instanceof Number) {
            return name.toString().trim();
        } else if (name instanceof String) {
            return ((String) name).trim();
        } else {
            return String.valueOf(name);
        }
    }

    public static Integer getInteger(String key) {
        if (TextUtils.isEmpty(key)) {
            return 0;
        } else {
            try {
                return Integer.valueOf(key);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return 0;
            }
        }
    }

    public static Integer getInteger(Integer key) {
        if (key == null) {
            return 0;
        } else {
            return key;
        }
    }


    public static Integer getInteger(String key, Integer defaultValue) {
        Integer name = getInteger(key);
        if (name.equals(0)) {
            return defaultValue == null ? 0 : defaultValue;
        }
        return name;
    }

    public static Double getDouble(String key) {
        if (TextUtils.isEmpty(key)) {
            return 0D;
        } else {
            try {
                return Double.valueOf(key);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return 0D;
            }
        }
    }

    public static Double getDouble(String key, Double defaultValue) {
        Double name = getDouble(key);
        if (name.equals(0)) {
            return defaultValue == null ? 0D : defaultValue;
        }
        return name;
    }

    public static Float getFloat(String key) {
        if (TextUtils.isEmpty(key)) {
            return 0F;
        } else {
            try {
                return Float.valueOf(key);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return 0F;
            }
        }
    }

    public static Float getFloat(String key, Float defaultValue) {
        Float name = getFloat(key);
        if (name.equals(0F)) {
            return defaultValue == null ? 0F : defaultValue;
        }
        return name;
    }

    public static Boolean getBoolean(String key) {
        if (TextUtils.isEmpty(key)) {
            return false;
        } else {
            try {
                return Boolean.valueOf(key);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public static Boolean getBoolean(Object key) {
        if (key == null) {
            return false;
        } else {
            try {
                return Boolean.valueOf(getString(key));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public static BigDecimal getBigDecimal(String key) {
        if (TextUtils.isEmpty(key)) {
            return BigDecimal.ZERO;
        } else {
            try {
                return new BigDecimal(key);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return BigDecimal.ZERO;
            }
        }
    }
}
