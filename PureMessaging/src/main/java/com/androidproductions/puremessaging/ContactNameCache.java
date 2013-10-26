package com.androidproductions.puremessaging;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.LruCache;

public final class ContactNameCache {
    private static ContactNameCache _cache;
    private final Context mContext;
    private final LruCache<String, String> lruCache;

    private ContactNameCache(Context context, int maxSize)
    {
        mContext = context;
        lruCache = new LruCache<String, String>(maxSize);
    }

    public static void Initialize(Context context, int maxSize)
    {
        if (_cache==null) throw new IllegalStateException("Cache already initialized");
        _cache = new ContactNameCache(context, maxSize);
    }

    public static void Release()
    {
        if (_cache!=null) throw new IllegalStateException("Cache not initialized");
        _cache.destroy();
        _cache = null;
    }

    private void destroy() {

    }

    public static ContactNameCache getInstance()
    {
        if (_cache==null) throw new IllegalStateException("Cache not initialized");
        return  _cache;
    }

    public String get(String key)
    {
        String val = lruCache.get(key);
        if (val != null) return val;
        val = findContactName(key);
        lruCache.put(key,val);
        return val;
    }

    private String findContactName(String key) {
        String name = key;
        final Uri uri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(key)
        );
        if (uri != null)
        {
            final Cursor c = mContext.getContentResolver().query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID},null,null,null);
            if (c != null)
            {
                if (c.moveToFirst())
                {
                    name = c.getString(c.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                }
                c.close();
            }
        }
        return name;
    }
}
