package com.androidproductions.puremessaging;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.androidproductions.puremessaging.data.ConversationContract;
import com.androidproductions.puremessaging.data.ConversationSummaryCursorAdapter;
import com.androidproductions.puremessaging.data.MessageHelper;


public class ConversationDetailActivity extends Activity implements Callbacks, LoaderManager.LoaderCallbacks<Cursor> {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ConversationSummaryCursorAdapter mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_detail);

        // Show the Up button in the action bar.
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            switchFragment(getIntent().getLongExtra(ConversationContract.ThreadId, 0L));
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.leftList);
        mTitle = getTitle();
        mDrawerTitle = getResources().getString(R.string.app_name);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // Set the adapter for the list view
        mAdapter = new ConversationSummaryCursorAdapter(this);
        mDrawerList.setAdapter(mAdapter);
        this.getLoaderManager().initLoader(1, null, this);
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                return super.onOptionsItemSelected(item);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        openDrawer();

    }

    private void openDrawer() {
        mDrawerLayout.openDrawer(Gravity.LEFT);
        getActionBar().setTitle(mDrawerTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void switchFragment(long threadId) {
        if (fragment != null)
            getFragmentManager().beginTransaction()
                .remove(fragment).commit();
        if (threadId == 0L)
        {
            fragment = new EmptyFragment();
        }
        else
        {
            Bundle arguments = new Bundle();
            arguments.putLong(ConversationContract.ThreadId,
                    threadId);
            fragment = new ConversationDetailFragment();
            fragment.setArguments(arguments);
        }
        getFragmentManager().beginTransaction()
                .add(R.id.conversation_detail_container, fragment)
                .commit();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(final int position) {
        mDrawerList.setItemChecked(position, true);
        setTitle(mAdapter.getName(position,this));
        switchFragment(mAdapter.getItemId(position));
        final Runnable r = new Runnable()
        {
            public void run()
            {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
            }
        };
        Handler handler = new Handler();
        handler.post(r);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int i, final Bundle bundle) {
        return MessageHelper.getAllConversationsAsync(this);
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> cursorLoader, final Cursor cursor) {
        if(mAdapter!=null && cursor!=null)
            mAdapter.swapCursor(cursor);
        else
            Log.v("PM", "OnLoadFinished: mAdapter is null");
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> cursorLoader) {
        if(mAdapter!=null)
            mAdapter.swapCursor(null);
        else
            Log.v("PM","OnLoadFinished: mAdapter is null");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Callback method from {@link Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(long id) {

    }

    /**
     * Callback method from {@link Callbacks}
     * indicating that the group with the given ID was selected.
     * Not used in this activity
     */
    @Override
    public void onGroupSelected(long id) {

    }
}
