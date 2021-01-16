package com.mugui.sql;

import android.database.Cursor;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Iterator;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 一个完整的表数据装载器
 *
 * @author 木鬼
 */
public final class TableMode implements Serializable {
    private static final long serialVersionUID = 8606735714775342730L;
    private int len = 0;
    private int row = 0;
    private ArrayList<JSONObject> Tablemode = null;
    private ArrayList<String> column_name = null;

    public TableMode() {

    }

    public TableMode(Cursor rs) {
        try {

            this.len = rs.getColumnCount();
            this.Tablemode = new ArrayList<JSONObject>(this.len);
            JSONObject v = new JSONObject();
            if (rs.moveToNext()) {
                this.column_name = new ArrayList<>();
                for (int i = 0; i < this.len; ++i) {
                    this.column_name.add(rs.getColumnName(i));
                    String s = rs.getString(i);
                    v.put(this.column_name.get(i), s);
                }
                this.Tablemode.add(v);
                row++;
            }
            while (rs.moveToNext()) {
                v = new JSONObject();
                for (int i = 0; i < this.len; ++i) {
                    String s = rs.getString(i);
                    v.put(this.column_name.get(i), s);
                }
                this.Tablemode.add(v);
                row++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null)
                rs.close();

        }
    }


    public int getColumnCount() {
        return this.len;
    }

    public int getRowCount() {
        return this.row;
    }

    public String getValueAt(int arg0, int arg1) {
        JSONObject taStrings = null;
        try {
            taStrings = (JSONObject) this.Tablemode.get(arg0);
        } catch (Exception e) {
            return null;
        }
        String s = null;
        try {
            s = (String) taStrings.get(column_name.get(arg1));
        } catch (Exception e) {
            return s;
        }
        return s;
    }

    public String getColumnName(int column) {
        if (this.column_name == null)
            return null;
        return ((String) this.column_name.get(column));
    }

    public JSONObject getRowData(int row) {
        return (JSONObject) this.Tablemode.get(row);
    }

    @Override
    public String toString() {
        String string = "";
        for (int i = 0; i < getColumnCount(); i++) {
            string += getColumnName(i) + "\t";
        }
        string += "\n";
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                string += getValueAt(i, j) + "\t";
            }
            string += "\n";
        }
        return string;
    }

    public JSONArray getData() {
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(this.Tablemode);
        return jsonArray;
    }

    public Iterator<JSONObject> iterator() {
        return Tablemode.iterator();
    }
}