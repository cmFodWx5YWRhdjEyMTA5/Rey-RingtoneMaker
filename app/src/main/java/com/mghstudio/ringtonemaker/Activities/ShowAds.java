package com.mghstudio.ringtonemaker.Activities;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.mghstudio.ringtonemaker.R;

public class ShowAds extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.about_50);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            setTaskDescription(new ActivityManager.TaskDescription("", bitmap,
                    ContextCompat.getColor(getApplicationContext(), R.color.tuan)));
    }

}
