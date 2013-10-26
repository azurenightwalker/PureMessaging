package com.androidproductions.puremessaging.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidproductions.puremessaging.R;

public class ConversationCursorAdapter extends CursorAdapter {

    public ConversationCursorAdapter(final Context context) {
        super(context,null,0);
    }

    @Override
    public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final RelativeLayout ret = (RelativeLayout) inflater.inflate(R.layout.message_list_item, parent, false);
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
        final TextView mDesc = (TextView) ret.findViewById(R.id.message);

        final int addressCol = cursor.getColumnIndexOrThrow(ConversationContract.Address);
        final int bodyCol = cursor.getColumnIndex(ConversationContract.Body);

        final String address = cursor.getString(addressCol);
        final String body = cursor.getString(bodyCol);

        String name = address;

        final Uri uri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(address)
        );
        if (uri != null)
        {
            final Cursor c = context.getContentResolver().query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID},null,null,null);
            if (c != null)
            {
                if (c.moveToFirst())
                {
                    name = c.getString(c.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                }
                c.close();
            }
        }

        mDesc.setText(body);
        mName.setText(name);
        return ret;
    }
}
