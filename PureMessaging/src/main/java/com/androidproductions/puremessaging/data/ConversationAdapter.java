package com.androidproductions.puremessaging.data;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidproductions.puremessaging.ContactImageCache;
import com.androidproductions.puremessaging.DateHelpers;
import com.androidproductions.puremessaging.R;

import java.util.ArrayList;
import java.util.Collections;

public class ConversationAdapter extends ArrayAdapter {
    private final Context context;
    private String address;
    private final ContactImageCache contactImageCache;

    public ConversationAdapter(Context context, Cursor newCursor) {
        super(context, R.layout.message_list_item_out);
        this.context = context;
        contactImageCache = ContactImageCache.getInstance();
        ArrayList<Message> mArrayList = new ArrayList<Message>();
        processData(newCursor,mArrayList);
        addAll(mArrayList);
        notifyDataSetChanged();
    }

    private void processData(Cursor newCursor, final ArrayList<Message> mArrayList) {
        if (newCursor != null)
        {
            newCursor.moveToFirst();
            final int timeCol = newCursor.getColumnIndexOrThrow(ConversationContract.Date);
            final int bodyCol = newCursor.getColumnIndex(ConversationContract.Body);
            final int typeCol = newCursor.getColumnIndex(ConversationContract.Type);
            address = newCursor.getString(newCursor.getColumnIndex(ConversationContract.Address));
            while(!newCursor.isAfterLast()) {
                Message mess = new Message(
                        newCursor.getLong(timeCol),
                        newCursor.getString(bodyCol),
                        newCursor.getInt(typeCol));
                if (mArrayList != null)
                {
                    mArrayList.add(mess); //add the item
                }
                else
                {
                    insert(mess,0);
                }
                newCursor.moveToNext();
            }
            if (mArrayList != null) {
                Collections.reverse(mArrayList);
            }
        }
    }

    public void appendItems(Cursor newCursor) {
        processData(newCursor,null);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final Message data = (Message) getItem(position);
        View rowView = inflater.inflate(data.IsIncoming() ? R.layout.message_list_item_in : R.layout.message_list_item_out, parent, false);
        final TextView mTime = (TextView) rowView.findViewById(R.id.message_time);
        final TextView mDesc = (TextView) rowView.findViewById(R.id.message);
        final ImageView mImage = (ImageView) rowView.findViewById(R.id.contact_image);
        mDesc.setText(data.getBody());
        mTime.setText(DateHelpers.GetRecentTime(data.getDate()));

        new Thread(new Runnable() {
            public void run() {
                mImage.setImageBitmap(data.IsIncoming() ? contactImageCache.get(address) : contactImageCache.get("SELF"));
            }
        }).run();
        return rowView;
    }
}
