package com.jkkg.hhtx.utils;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {

    //创建JSONObject
    public static JSONObject getJSONObject(String o) {
        JSONObject jsonObject = null;
        if (!TextUtils.isEmpty(o)) {
            try {
                jsonObject = new JSONObject(o);
            } catch (JSONException e) {
                e.printStackTrace();
                return jsonObject;
            }
        }
        return jsonObject;
    }

    public static JSONObject getJSONObject(Object o) {
        JSONObject jsonObject = null;
        if (o != null) {
            try {
                jsonObject = new JSONObject(new Gson().toJson(o));
            } catch (JSONException e) {
                e.printStackTrace();
                return jsonObject;
            }
        }
        return jsonObject;
    }

    public static JSONObject getJSONObject(Object o, String key) {
        if (o != null) {
            try {
                return getJSONObject(o).getJSONObject(key);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public static JSONObject getJSONObject(JSONObject jsonObject, String key) {
        if (jsonObject != null) {
            try {
                return jsonObject.getJSONObject(key);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public static String getString(String o, String key) {
        if (o != null) {
            try {
                return getJSONObject(o).getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
                return "";
            }
        }
        return "";
    }

    public static String getString(Object o, String key) {
        if (o != null) {
            try {
                if (getJSONObject(o).has(key)) {
                    return getJSONObject(o).getString(key);
                } else {
                    return "";
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return "";
            }
        }
        return "";
    }

    public static String getString(JSONObject jsonObject, String key) {
        if (jsonObject != null) {
            try {
                return jsonObject.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
                return "";
            }
        }
        return "";
    }

    public static Integer getInt(String o, String key) {
        if (o != null) {
            try {
                if (getJSONObject(o).has(key)) {
                    return getJSONObject(o).getInt(key);
                }
                return 0;
            } catch (JSONException e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    public static Integer getInt(Object o, String key) {
        if (o != null) {
            try {
                if (getJSONObject(o).has(key)) {
                    return getJSONObject(o).getInt(key);
                }
                return 0;
            } catch (JSONException e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    public static Integer getInt(JSONObject jsonObject, String key) {
        if (jsonObject != null) {
            try {
                if (jsonObject.has(key)) {
                    return jsonObject.getInt(key);
                }
                return 0;
            } catch (JSONException e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    public static Boolean getBoolean(String o, String key) {
        if (o != null) {
            try {
                if (getJSONObject(o).has(key)) {
                    return getJSONObject(o).getBoolean(key);
                }
                return false;
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public static Boolean getBoolean(Object o, String key) {
        if (o != null) {
            try {
                if (getJSONObject(o).has(key)) {
                    return getJSONObject(o).getBoolean(key);
                }
                return false;
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public static Boolean getBoolean(JSONObject jsonObject, String key) {
        if (jsonObject != null) {
            try {
                if (jsonObject.has(key)) {
                    return jsonObject.getBoolean(key);
                }
                return false;
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public static String getDateString(Object o) {
        if (o != null) {
            try {
                if (getJSONObject(o).has("date")) {
                    return getJSONObject(o).getString("date");
                } else {
                    return "";
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return "";
            }
        }
        return "";
    }

    public static JSONObject getDateJSONObject(Object o) {
        if (o != null) {
            try {
                if (getJSONObject(o).has("date")) {
                    return getJSONObject(o).getJSONObject("date");
                } else {
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public static JSONArray getDateJSONArray(Object o) {
        if (o != null) {
            try {
                if (getJSONObject(o).has("date")) {
                    return getJSONObject(o).getJSONArray("date");
                } else {
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

}
