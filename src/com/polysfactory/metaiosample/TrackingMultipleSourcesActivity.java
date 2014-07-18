package com.polysfactory.metaiosample;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.jni.TrackingValuesVector;
import com.metaio.sdk.jni.Vector3d;
import com.metaio.tools.io.AssetsManager;

public class TrackingMultipleSourcesActivity extends ARViewActivity {

    private static int MAX_DRAW_MARKERS = 10;
    private List<IGeometry> metaioManList = new ArrayList<IGeometry>();

    String trackingConfigFile;

    private MetaioSDKCallbackHandler callbackHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackHandler = new MetaioSDKCallbackHandler();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        callbackHandler.delete();
        callbackHandler = null;
    }

    @Override
    protected int getGUILayout() {
        return R.layout.tutorial_tracking_samples;
    }

    @Override
    public void onDrawFrame() {
        super.onDrawFrame();

        if (metaioSDK != null) {
            // get all detected poses/targets
            TrackingValuesVector poses = metaioSDK.getTrackingValues();

            MetaioDebug.log("detected_pose_size:" + poses.size());
            // if we have detected one, attach our metaio man to this coordinate system Id
            for (int i = 0; i < poses.size(); i++) {
                metaioManList.get(i).setCoordinateSystemID(poses.get(i).getCoordinateSystemID());
            }

        }
    }

    public void onButtonClick(View v) {
        finish();
    }

    public void onIdButtonClick(View v) {
        trackingConfigFile = AssetsManager.getAssetPath(getApplicationContext(),
                "TutorialTrackingSamples/Assets/TrackingData_Marker.xml");
        MetaioDebug.log("Tracking Config path = " + trackingConfigFile);

        boolean result = metaioSDK.setTrackingConfiguration(trackingConfigFile);
        MetaioDebug.log("Id Marker tracking data loaded: " + result);
        for (IGeometry metaioMan : metaioManList) {
            metaioMan.setScale(new Vector3d(2f, 2f, 2f));
        }
    }

    public void onPictureButtonClick(View v) {
        trackingConfigFile = AssetsManager.getAssetPath(getApplicationContext(),
                "TutorialTrackingSamples/Assets/TrackingData_PictureMarker.xml");
        MetaioDebug.log("Tracking Config path = " + trackingConfigFile);

        boolean result = metaioSDK.setTrackingConfiguration(trackingConfigFile);
        MetaioDebug.log("Picture Marker tracking data loaded: " + result);
        for (IGeometry metaioMan : metaioManList) {
            metaioMan.setScale(new Vector3d(8f, 8f, 8f));
        }

    }

    public void onMarkerlessButtonClick(View v) {
        trackingConfigFile = AssetsManager.getAssetPath(getApplicationContext(),
                "TutorialTrackingSamples/Assets/TrackingData_MarkerlessFast.xml");
        MetaioDebug.log("Tracking Config path = " + trackingConfigFile);

        boolean result = metaioSDK.setTrackingConfiguration(trackingConfigFile);
        MetaioDebug.log("Markerless tracking data loaded: " + result);
        for (IGeometry metaioMan : metaioManList) {
            metaioMan.setScale(new Vector3d(4f, 4f, 4f));
        }
    }

    @Override
    protected void loadContents() {
        try {

            // Load desired tracking data for planar marker tracking
            trackingConfigFile = AssetsManager.getAssetPath(getApplicationContext(),
                    "TutorialTrackingSamples/Assets/TrackingData_MarkerlessFast.xml");

            boolean result = metaioSDK.setTrackingConfiguration(trackingConfigFile);
            MetaioDebug.log("Markerless tracking data loaded: " + result);

            // Load all the geometries. First - Model
            String metaioManModel = AssetsManager.getAssetPath(getApplicationContext(),
                    "TutorialTrackingSamples/Assets/metaioman.md2");
            if (metaioManModel != null) {
                for (int i = 0; i < MAX_DRAW_MARKERS; i++) {
                    IGeometry metaioMan = metaioSDK.createGeometry(metaioManModel);
                    if (metaioMan != null) {
                        // Set geometry properties
                        metaioMan.setScale(new Vector3d(4.0f, 4.0f, 4.0f));
                        MetaioDebug.log("Loaded geometry " + metaioManModel);
                        metaioManList.add(metaioMan);
                    } else {
                        MetaioDebug.log(Log.ERROR, "Error loading geometry: " + metaioManModel);
                    }
                }
            }

        } catch (Exception e) {

        }
    }

    @Override
    protected void onGeometryTouched(IGeometry geometry) {
        // TODO Auto-generated method stub

    }

    @Override
    protected IMetaioSDKCallback getMetaioSDKCallbackHandler() {
        return callbackHandler;
    }

    final class MetaioSDKCallbackHandler extends IMetaioSDKCallback {

        @Override
        public void onSDKReady() {
            // show GUI
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mGUIView.setVisibility(View.VISIBLE);
                }
            });
        }
    }

}
