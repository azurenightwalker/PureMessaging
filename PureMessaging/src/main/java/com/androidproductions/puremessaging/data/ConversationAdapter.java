package com.androidproductions.puremessaging.data;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidproductions.puremessaging.ContactNameCache;
import com.androidproductions.puremessaging.DateHelpers;
import com.androidproductions.puremessaging.R;

import java.util.ArrayList;
import java.util.Collections;

public class ConversationAdapter extends ArrayAdapter {
    private final Context context;

    public ConversationAdapter(Context context, Cursor newCursor) {
        super(context, R.layout.message_list_item);
        this.context = context;
        processData(newCursor);
    }

    private void processData(Cursor newCursor) {
        if (newCursor != null)
        {
            final ArrayList<Message> mArrayList = new ArrayList<Message>();
            newCursor.moveToFirst();
            final int timeCol = newCursor.getColumnIndexOrThrow(ConversationContract.Date);
            final int bodyCol = newCursor.getColumnIndex(ConversationContract.Body);
            while(!newCursor.isAfterLast()) {
                mArrayList.add(
                        new Message(
                                newCursor.getLong(timeCol),
                                newCursor.getString(bodyCol))
                ); //add the item
                newCursor.moveToNext();
            }
            Collections.reverse(mArrayList);
            addAll(mArrayList);
        }
    }

    public void appendItems(Cursor newCursor) {
        processData(newCursor);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.message_list_item, parent, false);
        final TextView mTime = (TextView) rowView.findViewById(R.id.message_time);
        final TextView mDesc = (TextView) rowView.findViewById(R.id.message);
        final Message data = (Message) getItem(position);
        mDesc.setText(data.getBody());
        mTime.setText(DateHelpers.GetRecentTime(data.getDate()));

        return rowView;
    }
}
