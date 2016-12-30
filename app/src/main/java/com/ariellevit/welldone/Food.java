package com.ariellevit.welldone;



public class Food {
    private String name, printedTime;
    private long nameId, time, dateCreatedMilli;

    public Food() {
    }

    public Food(String name, long time, String printedTime) {
        this.time = time;
        this.name = name;
        this.printedTime = printedTime;

        this.dateCreatedMilli = 0;
    }

    public Food(String name, long time, String printedTime, long dateCreatedMilli, long nameId) {
        this.name = name;
        this.time = time;
        this.printedTime = printedTime;
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

    public String getPrintedTime() {
        return printedTime;
    }

    public long getDateCreatedMilli() {
        return dateCreatedMilli;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrintedTime(String printedTime) {
        this.printedTime = printedTime;
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

