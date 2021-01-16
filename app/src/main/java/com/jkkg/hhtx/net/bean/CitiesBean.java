package com.jkkg.hhtx.net.bean;

import java.io.Serializable;
import java.util.List;

import me.yokeyword.indexablerv.IndexableEntity;

public  class CitiesBean implements Serializable ,IndexableEntity {
    private String en;
    private String zh;
    private String locale;
    private String code;

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getZh() {
        return zh;
    }

    public void setZh(String zh) {
        this.zh = zh;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getFieldIndexBy() {
        return zh;
    }

    @Override
    public void setFieldIndexBy(String indexField) {
        this.zh = indexField;
    }

    @Override
    public void setFieldPinyinIndexBy(String pinyin) {
        // 保存排序field的拼音,在执行比如搜索等功能时有用 （若不需要，空实现该方法即可）

    }
}
