package com.androidproductions.puremessaging.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidproductions.puremessaging.ContactNameCache;
import com.androidproductions.puremessaging.DateHelpers;
import com.androidproductions.puremessaging.R;

import java.util.ArrayList;
import java.util.Collections;

public class ConversationCursorAdapter extends CursorAdapter {

    private ArrayList<ArrayList<String>> mArrayList;

    public ConversationCursorAdapter(final Context context) {
        super(context,null,0);
    }

    @Override
    public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final RelativeLayout ret = (RelativeLayout) inflater.inflate(R.layout.message_list_item_out, parent, false);
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
        final TextView mTime = (TextView) ret.findViewById(R.id.message_time);
        final TextView mDesc = (TextView) ret.findViewById(R.id.message);
        ArrayList<String> data = mArrayList.get(cursor.getPosition());
        final long time = Long.parseLong(data.get(0));
        final String body = data.get(1);

        mDesc.setText(body);
        mTime.setText(DateHelpers.GetRecentTime(time));
        return ret;
    }

    @Override
    public Cursor swapCursor(Cursor newCursor) {
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
        return super.swapCursor(newCursor);
    }
}
