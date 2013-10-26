package com.androidproductions.puremessaging.data;

import android.net.Uri;

public final class ConversationContract {

        public static final String _ID = "_id";
        public static final String ThreadId = "thread_id";
        public static final String Date = "date";
        public static final String Address = "address";
        public static final String Body = "body";
        public static final String Type = "type";
        public static final String Read = "type";
        public static final String Locked = "type";
        public static final String ContactId = "contact_id";


        public static final Uri CONTENT_URI =
                Uri.parse("content://com.androidproductions.puremessaging/conversations");

        public static final String[] PROJECTION = new String[] {
                ConversationContract._ID,
                ConversationContract.ThreadId,
                ConversationContract.Date,
                ConversationContract.Address,
                ConversationContract.Body,
                ConversationContract.Type,
                ConversationContract.Read,
                ConversationContract.Locked
        };
}
