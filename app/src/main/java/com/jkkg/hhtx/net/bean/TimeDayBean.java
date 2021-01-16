package com.jkkg.hhtx.net.bean;


import java.util.Date;

public class TimeDayBean {
    private String name;
    private Date id ;
    private char day;


    public TimeDayBean(String name, Date id,char day) {
        this.name = name;
        this.id = id;
        this.day=day;

    }

    public TimeDayBean() {

    }


    public TimeDayBean(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getId() {
        return id;
    }

    public void setId(Date id) {
        this.id = id;
    }

    public char getDay() {
        return day;
    }

    public void setDay(char day) {
        this.day = day;
    }
}
