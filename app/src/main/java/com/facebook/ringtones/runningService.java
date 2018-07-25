package com.facebook.ringtones;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.mghstudio.ringtonemaker.Activities.ShowAds;
import com.rvalerio.fgchecker.AppChecker;

import java.util.ArrayList;

//import com.facebook.ads.Ad;
//import com.facebook.ads.AdError;
//import com.facebook.ads.InterstitialAd;
//import com.facebook.ads.InterstitialAdListener;

public class runningService extends Service {
    public static boolean check = false;
    private ArrayList<String> processLis;
    private Runnable runnableCode;
    private String firstFG;
    private String nextFG;
    private AppChecker appChecker;
    //    private InterstitialAd interstitialAd;
//    private InterstitialAd mInterstitialAd;
    private String TAG = "TAG foreground";
    private MyBroadcast myBroadcast;

    @Override
    public void onCreate() {
        super.onCreate();
        myBroadcast = new MyBroadcast();
        IntentFilter filter = new IntentFilter("android.intent.action.USER_PRESENT");
        registerReceiver(myBroadcast, filter);
//        ShowAds.mInterstitialAd.loadAd();
       /* // Google Admob
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
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                check = false;
                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
                        .getInstance(runningService.this);
                localBroadcastManager.sendBroadcast(new Intent(
                        getPackageName() + ".closeapp"));
            }
        });*/
        showAdWithDelay();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private void showAdWithDelay() {
        final Handler handler = new Handler();
        runnableCode = new Runnable() {
            @Override
            public void run() {
                if ((check)) {


//                    mInterstitialAd.show();
                    final InterstitialAd mInterstitialAd;
                    mInterstitialAd = new InterstitialAd(runningService.this);
                    mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());

                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            // Code to be executed when an ad finishes loading.
                            Intent showAds = new Intent(getApplicationContext(), ShowAds.class);
                            showAds.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(showAds);
                            mInterstitialAd.show();
//                            ShowAds.getInstance().showAds(mInterstitialAd);
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
//                            mInterstitialAd.loadAd(new AdRequest.Builder().build());
//                            runningService.check = false;

//                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
//                        .getInstance(runningService.this);
//                localBroadcastManager.sendBroadcast(new Intent(
//                        getPackageName() + ".closeapp"));
                            check = false;
                            android.os.Process.killProcess(android.os.Process.myPid());

                        }
                    });

                }
                handler.postDelayed(runnableCode, 1000 * 10);
//                handler.postDelayed(runnableCode, 1000*3*60);
            }
        };
        handler.post(runnableCode);
    }

    public class MyBroadcast extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("OnScreen", " U 've opened app");
            check = true;

        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
