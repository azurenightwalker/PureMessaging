package com.androidproductions.puremessaging.data;

import com.androidproductions.puremessaging.MessageType;

public class Message {
    private int Type;
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

    public Message(long date, String body, int type) {
        Date = date;
        Body = body;
        Type = type;
    }

    public boolean IsIncoming()
    {
        return Type == MessageType.SMS_INBOX || Type == MessageType.DATA_INBOX;
    }

    public boolean IsOutgoing()
    {
        return !IsIncoming();
    }
}
