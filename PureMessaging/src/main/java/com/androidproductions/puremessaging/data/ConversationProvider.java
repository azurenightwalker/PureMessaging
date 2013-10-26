package com.androidproductions.puremessaging.data;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

public class ConversationProvider extends ContentProvider
{
    private static final int CONVERSATION = 0;
    private static final int CONVERSATIONS = 1;

    private static final String PROVIDER_NAME =
            "com.androidproductions.puremessaging";
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(PROVIDER_NAME, "conversations", CONVERSATIONS);
        uriMatcher.addURI(PROVIDER_NAME, "conversations/#", CONVERSATION);
    }

    public boolean onCreate() {
        return true;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Default projection if none supplied
        if(projection == null) projection = getDefaultProjection(uri);

        Cursor c = null;
        // Defaults & ID's
        switch(uriMatcher.match(uri))
        {
            case CONVERSATIONS:
                if (TextUtils.isEmpty(sortOrder)) sortOrder = "_ID ASC";
                c = getContext().getContentResolver().
                        query(Uri.parse("content://mms-sms/conversations?simple=true"),
                                projection, selection, selectionArgs, sortOrder);
                break;
            case CONVERSATION:
                selection = (selection == null ? "" : (selection + " and ")) +
                        ConversationContract.ThreadId + " = " + uri.getLastPathSegment();
                c = getContext().getContentResolver().query(Uri.parse("content://sms/"),
                        projection, selection, selectionArgs, sortOrder);

                break;
            default:
                return null;
        }

        return c;
    }

    public String getType(Uri uri) {
        switch(uriMatcher.match(uri))
        {
            case CONVERSATIONS:
                return "vnd.android.cursor.dir/vnd.com.androidproductions.puremessaging.conversation";
            case CONVERSATION:
                return "vnd.android.cursor.item/vnd.com.androidproductions.puremessaging.conversation";
            default:
                return null;
        }
    }

    public Uri insert(Uri uri, ContentValues contentValues) {
        // TODO: Implement
        return null;
    }
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO: Implement
        return 0;
    }

    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        // TODO: Implement
        return 0;
    }

    private String[] getDefaultProjection(Uri uri)
    {
        switch(uriMatcher.match(uri))
        {
            case CONVERSATION:
                return ConversationContract.PROJECTION;
            case CONVERSATIONS:
                return ConversationSummaryContract.PROJECTION;
            default:
                return null;
        }
    }
}
