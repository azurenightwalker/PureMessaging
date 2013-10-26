package com.androidproductions.puremessaging.data;

import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;

public final class MessageHelper {
    public static Cursor getAllConversations(final Context mContext)
    {
        return mContext.getContentResolver()
                .query(ConversationSummaryContract.CONTENT_URI, null, null, null, null);
    }

    public static CursorLoader getAllConversationsAsync(final Context mContext)
    {
        return new CursorLoader(mContext, ConversationSummaryContract.CONTENT_URI, null, null, null, null);
    }

    public static Cursor getConversation(final Context mContext, final long threadId)
    {
        return mContext.getContentResolver()
                .query(ConversationSummaryContract.CONTENT_URI, null, null, null, null);
    }

    public static CursorLoader getConversationAsync(final Context mContext, final long threadId)
    {
        return new CursorLoader(mContext,
                ContentUris.withAppendedId(ConversationContract.CONTENT_URI,threadId),
                null, null, null, ConversationContract.Date + " DESC LIMIT 25");
    }
}
