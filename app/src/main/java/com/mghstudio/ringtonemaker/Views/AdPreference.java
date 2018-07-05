package com.mghstudio.ringtonemaker.Views;

import android.app.Activity;
import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;

public class AdPreference extends Preference {

    public AdPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AdPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AdPreference(Context context) {
        super(context);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        // this will create the linear layout defined in ads_layout.xml
        View view = super.onCreateView(parent);

        // the context is a PreferenceActivity
        Activity activity = (Activity) getContext();

        AdView adView;
        // Create the adView
        adView = new AdView(activity, "2199797023369826_2269184119764449", AdSize.BANNER_HEIGHT_50);

       /* ((LinearLayout)view).addView(adView);

        // Initiate a generic request to load it with an ad
        AdRequest request = new AdRequest();
        adView.loadAd(request);*/


        // Find the Ad Container
//        RelativeLayout adContainer = findViewById(R.id.baner2);
        ((LinearLayout) view).addView(adView);

        // Add the ad view to your activity layout
//        adContainer.addView(adView);

        // Request an ad
        adView.loadAd();

        return view;
    }
}