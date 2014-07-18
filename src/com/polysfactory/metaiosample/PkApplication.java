package com.polysfactory.metaiosample;

import java.io.IOException;

import android.app.Application;
import android.util.Log;

import com.metaio.sdk.MetaioDebug;
import com.metaio.tools.io.AssetsManager;

public class PkApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MetaioDebug.enableLogging(true);
        try {
            AssetsManager.extractAllAssets(this, false);
        } catch (IOException e) {
            Log.e("PK", "asset copy error", e);
        }
    }
}
