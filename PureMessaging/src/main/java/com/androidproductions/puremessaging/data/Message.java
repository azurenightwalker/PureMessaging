package com.androidproductions.puremessaging.data;

public class Message {
    private long Date;
    private String Body;

    public long getDate() {
        return Date;
    }

    public void setDate(long date) {
        Date = date;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }

    public Message(long date, String body) {
        Date = date;
        Body = body;
    }
}
