package com.androidproductions.puremessaging;

import android.app.Application;

public class PureMessagingApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        InitializeSingletons();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    private void InitializeSingletons()
    {
        ContactNameCache.Initialize(getApplicationContext(),10);
        ContactImageCache.Initialize(getApplicationContext(),10);
    }
}
