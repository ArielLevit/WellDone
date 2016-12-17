package com.ariellevit.welldone;



public class Food {
    private String name;
    private long nameId, time, dateCreatedMilli;

    public Food() {
    }

    public Food(String name, long time) {
        this.time = time;
        this.name = name;
        this.dateCreatedMilli = 0;
    }

    public Food(String name, long time, long dateCreatedMilli, long nameId) {
        this.name = name;
        this.time = time;
        this.dateCreatedMilli = dateCreatedMilli;
        this.nameId = nameId;
    }

    public String getName() {
        return name;
    }

    public long getNameId() {
        return nameId;
    }

    public long getTime() {
        return time;
    }

    public long getDateCreatedMilli() {
        return dateCreatedMilli;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNameId(long nameId) {
        this.nameId = nameId;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setDateCreatedMilli(long dateCreatedMilli) {
        this.dateCreatedMilli = dateCreatedMilli;
    }
}

