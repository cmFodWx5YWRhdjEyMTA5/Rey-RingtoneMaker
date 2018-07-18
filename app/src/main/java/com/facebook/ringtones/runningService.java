package com.facebook.ringtones;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.rvalerio.fgchecker.AppChecker;

import java.util.ArrayList;
import java.util.List;

//import com.facebook.ads.Ad;
//import com.facebook.ads.AdError;
//import com.facebook.ads.InterstitialAd;
//import com.facebook.ads.InterstitialAdListener;

public class runningService extends Service {
    //    private InterstitialAd interstitialAd;
    private InterstitialAd mInterstitialAd;
    private String TAG = "TAG foreground";
    private ArrayList<String> processLis;
    private Runnable runnableCode;
    private String firstFG;
    private String nextFG;
    private AppChecker appChecker;

    public static boolean isForeground(Context ctx, String myPackage) {
        ActivityManager manager = (ActivityManager) ctx.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfo = manager.getRunningTasks(1);

        ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
        Log.d("TAG foreground", "size task" + runningTaskInfo.size());

        return componentInfo.getPackageName().equals(myPackage);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        processLis = new ArrayList<>();
//        if(true)
//            return;
        //Special one


//        appChecker = new AppChecker();
//        firstFG = appChecker.getForegroundApp(getApplicationContext());
//        Handler handler1 = new Handler();
//        handler1.post(new Runnable() {
//            @Override
//            public void run() {
//                nextFG = appChecker.getForegroundApp(getApplicationContext());
//                if (!firstFG.equalsIgnoreCase(nextFG)) {
//                    if (mInterstitialAd.isLoaded()) {
//                        mInterstitialAd.show();
//                    }
//                }
//            }
//        });

        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfo = am.getRunningAppProcesses();

        for (int i = 0; i < runningAppProcessInfo.size(); i++) {
//            if (runningAppProcessInfo.get(i).processName.equals("com.the.app.you.are.looking.for")) {
//                // Do you stuff
//            }

            processLis.add(runningAppProcessInfo.get(i).processName);
            if (isForeground(getApplicationContext(), runningAppProcessInfo.get(i).processName)) {

            }
            Log.d(TAG, "size" + runningAppProcessInfo.size());
            Log.d(TAG, "tuanvn " + runningAppProcessInfo.get(i).processName + "is foreground");
        }


        // Google Admob
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
//                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });


        //show ads
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }

        if (runningAppProcessInfo.size() > 0) {
//            showAdWithDelay();
//            if (mInterstitialAd.isLoaded()) {
//                mInterstitialAd.show();
//            }

            // Create the Handler object (on the main thread by default)
//            final Handler handler = new Handler();
//            // Define the code block to be executed
//            runnableCode = new Runnable() {
//                @Override
//                public void run() {
//                    // Do something here on the main thread
//                    Log.d("Handlers", "Called on main thread");
//                    if (mInterstitialAd.isLoaded()) {
//                        mInterstitialAd.show();
//                    }
//                    // Repeat this the same runnable code block again another 2 seconds
//                    handler.postDelayed(runnableCode, 5000);
//                }
//            };
//            // Start the initial runnable task by posting through the handler
//            handler.post(runnableCode);

        }

        /*interstitialAd = new InterstitialAd(this, "2199797023369826_2199798263369702");

        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                interstitialAd.loadAd();
                Log.e(TAG, "Interstitial ad dismissed.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        });

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd.loadAd();
*/
    }

    private void showAdWithDelay() {
        /**
         * Here is an example for displaying the ad with delay;
         * Please do not copy the Handler into your project
         */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
              /*  // Check if interstitialAd has been loaded successfully
                if (interstitialAd == null || !interstitialAd.isAdLoaded()) {
                    return;
                }
                // Check if ad is already expired or invalidated, and do not show ad if that is the case. You will not get paid to show an invalidated ad.
                if (interstitialAd.isAdInvalidated()) {
                    return;
                }
                // Show the ad
                interstitialAd.show();*/

                if (mInterstitialAd == null || !mInterstitialAd.isLoaded()) {
                    return;
                }

                if (mInterstitialAd.isLoaded())
                    mInterstitialAd.show();
            }
        }, 1000 * 2); // Show the ad after 15 minutes
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
