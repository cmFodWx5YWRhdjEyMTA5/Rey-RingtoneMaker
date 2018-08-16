package com.mghstudio.ringtonemaker.Activities;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.mghstudio.ringtonemaker.R;

public class ShowAds extends AppCompatActivity {
    private static ShowAds instance;

    public static ShowAds getInstance() {
        return instance;
    }
    private  int countResume = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Wellcome");

        try
        {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fbinfo);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                setTaskDescription(new ActivityManager.TaskDescription("", bitmap,
                        ContextCompat.getColor(getApplicationContext(), R.color.tuan)));

        }
        catch (Exception e)
        {
        }

        if (instance == null)
            instance = this;
    }

    @Override
    public void onResume(){
        super.onResume();
        countResume++;
        try {
            if(countResume >= 2)
            {
                if (Build.VERSION.SDK_INT < 21) {
                    finishAffinity();
                } else {
                    finishAndRemoveTask();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
