package com.androidproductions.puremessaging.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidproductions.puremessaging.ContactImageCache;
import com.androidproductions.puremessaging.ContactNameCache;
import com.androidproductions.puremessaging.R;

public class ConversationSummaryCursorAdapter extends CursorAdapter {

    private final ContactNameCache contactNameCache;
    private final ContactImageCache contactPhotoCache;

    public ConversationSummaryCursorAdapter(final Context context) {
        super(context,null,0);
        contactNameCache = ContactNameCache.getInstance();
        contactPhotoCache = ContactImageCache.getInstance();
    }

    @Override
    public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final RelativeLayout ret = (RelativeLayout) inflater.inflate(R.layout.conversation_list_item, parent, false);
        if (ret != null)
        {
            return populateView(cursor, ret, context);
        }
        return null;
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        populateView(cursor,view, context);
    }

    private View populateView(Cursor cursor, View ret, Context context) {
        final TextView mName = (TextView) ret.findViewById(R.id.contact_name);
        final TextView mDesc = (TextView) ret.findViewById(R.id.snippet);
        final ImageView mImage = (ImageView) ret.findViewById(R.id.contact_image);

        final int nameIdx = cursor.getColumnIndex(ConversationSummaryContract.RecipientIds);
        final int snip = cursor.getColumnIndex(ConversationSummaryContract.Snippet);
        final String recs = cursor.getString(nameIdx);

        final String snippet = cursor.getString(snip);

        String address = "";
        if (recs != null)
        {
            for (final String recipient : recs.split(" "))
            {
                final Cursor c2 = context.getContentResolver().query(Uri.parse("content://mms-sms/canonical-address/" + recipient),null, null,
                        null, null);
                if (c2 != null)
                {
                    if (c2.moveToFirst()) {
                        address = c2.getString(0);
                    }
                    c2.close();
                }
            }
        }

        mName.setText(contactNameCache.get(address));
        mDesc.setText(snippet);
        //mImage.setImageBitmap(contactPhotoCache.get(address));
        return ret;
    }


    public String getName(int position, Context context) {
        Cursor cursor = (Cursor) getItem(position);
        final int nameIdx = cursor.getColumnIndex(ConversationSummaryContract.RecipientIds);
        final String recs = cursor.getString(nameIdx);

        String address = "";
        if (recs != null)
        {
            for (final String recipient : recs.split(" "))
            {
                final Cursor c2 = context.getContentResolver().query(Uri.parse("content://mms-sms/canonical-address/" + recipient),null, null,
                        null, null);
                if (c2 != null)
                {
                    if (c2.moveToFirst()) {
                        address = c2.getString(0);
                    }
                    c2.close();
                }
            }
        }
        return contactNameCache.get(address);
    }
}
