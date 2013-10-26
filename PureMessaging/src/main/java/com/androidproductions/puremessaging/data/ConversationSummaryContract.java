package com.androidproductions.puremessaging.data;

import android.net.Uri;

public final class ConversationSummaryContract {

        public static final String _ID = "_id";
        public static final String Date = "date";
        public static final String RecipientIds = "recipient_ids";

        public static final String Snippet = "snippet";
        public static final String Count = "message_count";

        public static final Uri CONTENT_URI =
                Uri.parse("content://com.androidproductions.puremessaging/conversations");

        public static final String[] PROJECTION = new String[] {
                ConversationSummaryContract._ID,
                ConversationSummaryContract.Date,
                ConversationSummaryContract.RecipientIds,
                ConversationSummaryContract.Snippet,
                ConversationSummaryContract.Count
        };
}
