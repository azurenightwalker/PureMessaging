package com.androidproductions.puremessaging;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.LruCache;

import java.io.InputStream;

public final class ContactImageCache {
    private static ContactImageCache _cache;
    private final Context mContext;
    private final LruCache<String, Bitmap> lruCache;

    private ContactImageCache(Context context, int maxSize)
    {
        mContext = context;
        lruCache = new LruCache<String, Bitmap>(maxSize);
    }

    public static void Initialize(Context context, int maxSize)
    {
        if (_cache!=null) throw new IllegalStateException("Cache already initialized");
        _cache = new ContactImageCache(context, maxSize);
    }

    public static void Release()
    {
        if (_cache==null) throw new IllegalStateException("Cache not initialized");
        _cache.destroy();
        _cache = null;
    }

    private void destroy() {

    }

    public static ContactImageCache getInstance()
    {
        if (_cache==null) throw new IllegalStateException("Cache not initialized");
        return  _cache;
    }

    public Bitmap get(String key)
    {
        Bitmap val = lruCache.get(key);
        if (val != null) return val;
        val = findContactBitmap(key);
        lruCache.put(key,val);
        return val;
    }

    private Bitmap findContactBitmap(String key) {
        long id = -1L;
        ContentResolver cr = mContext.getContentResolver();
        if (key.equals("SELF"))
            return loadProfileImage();
        final Uri uri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(key)
        );
        if (uri != null)
        {
            final Cursor c = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID},null,null,null);
            if (c != null)
            {
                if (c.moveToFirst())
                {
                    id = c.getLong(c.getColumnIndex(ContactsContract.PhoneLookup._ID));
                }
                c.close();
            }
        }
        return loadContactPhoto(id);
    }

    public Bitmap loadContactPhoto(long  id) {
        if (id < 0)
        {
            return getDefaultImage();
        }
        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
        return _getContactImage(uri);
    }

    public Bitmap loadProfileImage() {
        return _getContactImage(ContactsContract.Profile.CONTENT_URI);
    }

    private Bitmap _getContactImage(final Uri uri)
    {
        try
        {
            final InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(
                    mContext.getContentResolver() ,uri);
            if (input != null)
            {
                final Bitmap img = BitmapFactory.decodeStream(input);
                input.close();
                return img;
            }
        }
        catch(Exception ex)
        {
            Log.d("PureMessaging", "Couldnt find image");
        }
        return getDefaultImage();
    }

    public Bitmap getDefaultImage()
    {
        return BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher);
    }
}
