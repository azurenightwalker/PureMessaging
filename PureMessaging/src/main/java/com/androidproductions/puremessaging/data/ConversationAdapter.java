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
    private final ArrayList<ArrayList<String>> mArrayList;

    public ConversationAdapter(Context context, Cursor newCursor) {
        super(context, R.layout.message_list_item);
        this.context = context;
        mArrayList = new ArrayList<ArrayList<String>>();
        if (newCursor != null)
        {
            newCursor.moveToFirst();
            final int timeCol = newCursor.getColumnIndexOrThrow(ConversationContract.Date);
            final int bodyCol = newCursor.getColumnIndex(ConversationContract.Body);
            while(!newCursor.isAfterLast()) {
                ArrayList<String> data = new ArrayList<String>();
                data.add(String.valueOf(newCursor.getLong(timeCol)));
                data.add(newCursor.getString(bodyCol));
                mArrayList.add(data); //add the item
                newCursor.moveToNext();
            }
            Collections.reverse(mArrayList);
        }
    }

    public void appendItems(Cursor newCursor) {
        if (newCursor != null)
        {
            newCursor.moveToFirst();
            final int timeCol = newCursor.getColumnIndexOrThrow(ConversationContract.Date);
            final int bodyCol = newCursor.getColumnIndex(ConversationContract.Body);
            while(!newCursor.isAfterLast()) {
                ArrayList<String> data = new ArrayList<String>();
                data.add(String.valueOf(newCursor.getLong(timeCol)));
                data.add(newCursor.getString(bodyCol));
                mArrayList.add(data); //add the item
                newCursor.moveToNext();
            }
            Collections.reverse(mArrayList);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.message_list_item, parent, false);
        final TextView mTime = (TextView) rowView.findViewById(R.id.message_time);
        final TextView mDesc = (TextView) rowView.findViewById(R.id.message);
        ArrayList<String> data = mArrayList.get(position);
        final long time = Long.parseLong(data.get(0));
        final String body = data.get(1);

        mDesc.setText(body);
        mTime.setText(DateHelpers.GetRecentTime(time));

        return rowView;
    }
}
