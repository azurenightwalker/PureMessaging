package com.androidproductions.puremessaging;

public final class MessageType {
    public static final int ALL = 0;

    public static final int SMS_INBOX = 1;
    public static final int SMS_SENT = 2;
    public static final int SMS_DRAFT = 3;
    public static final int SMS_OUTBOX = 4;
    public static final int SMS_FAILED = 5;
    public static final int SMS_QUEUED = 6;

    public static final int DATA_INBOX = 7;
    public static final int DATA_SENT = 8;
    public static final int DATA_DRAFT = 9;
    public static final int DATA_OUTBOX = 10;
    public static final int DATA_FAILED = 11;
    public static final int DATA_QUEUED = 12;

    private MessageType() {
    }
}
