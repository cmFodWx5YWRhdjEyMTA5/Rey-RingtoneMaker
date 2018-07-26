package com.mghstudio.ringtonemaker.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ringtones.runningService;
import com.mghstudio.ringtonemaker.Adapters.CellAdapter;
import com.mghstudio.ringtonemaker.R;
import com.mghstudio.ringtonemaker.Ringdroid.Utils;

import java.util.HashMap;
import java.util.Map;

import static com.mghstudio.ringtonemaker.Ringdroid.Constants.REQUEST_ID_MULTIPLE_PERMISSIONS;
import static com.mghstudio.ringtonemaker.Ringdroid.Constants.REQUEST_ID_READ_CONTACTS_PERMISSION;

public class MainActivity extends AppCompatActivity {

    private GridView gridView;
    private AdView adView;
    private Bitmap bitmap;
    private static final String[] items = new String[]{
            "Contacts", "Ringtone", "Settings", "More apps"};

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_READ_CONTACTS_PERMISSION: {
                Map<String, Integer> perms = new HashMap<>();
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Intent contact = new Intent(getApplicationContext(), ContactActivity.class);
                        startActivity(contact);
                    }
                }
                break;
            }

            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);

                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                        Intent select = new Intent(getApplicationContext(), RingdroidSelectActivity2.class);
                        startActivity(select);
                    }
                }
                break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // time when installing app
        SharedPreferences pref = getApplicationContext().getSharedPreferences("DataCountService", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong("timeInstall", System.currentTimeMillis());
        editor.apply();

        Intent myIntent = new Intent(MainActivity.this, runningService.class);
        startService(myIntent);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        adView = new AdView(this, "2199797023369826_2199797623369766", AdSize.RECTANGLE_HEIGHT_250);
        // Find the Ad Container
        RelativeLayout adContainer = findViewById(R.id.AdView);

        // Add the ad view to your activity layout
        adContainer.addView(adView);

        // Request an ad
        adView.loadAd();


        gridView = findViewById(R.id.gridView);

        gridView.setAdapter(new CellAdapter(this, items));

        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        if (Utils.checkAndRequestContactsPermissions(MainActivity.this)) {
                            Intent contact = new Intent(MainActivity.this, ContactActivity.class);
                            startActivity(contact);

                        }
                        break;
                    case 1:
                        if (Utils.checkAndRequestPermissions(MainActivity.this, true)) {
                            Intent i = new Intent(MainActivity.this, RingdroidSelectActivity2.class);
                            startActivity(i);
                        }

                        break;
                    case 2:
                        Intent j = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(j);
                        break;
                    case 3:
                        Intent a = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Mini+Apps+VN"));
                        startActivity(a);
                }
            }
        });

    }
}
