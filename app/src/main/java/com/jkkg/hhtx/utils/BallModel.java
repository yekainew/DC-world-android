package com.jkkg.hhtx.utils;

public class BallModel {
    private String content;
    private String value;
    private String id;
    public BallModel(String content, String value,String id) {
        this.content = content;
        this.value = value;
        this.id=id;
    }

    public String getContent() {
        return content;
    }

    public String getValue() {
        return value;
    }

    public String getId(){
        return id;
    }
}
