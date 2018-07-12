package com.mghstudio.ringtonemaker.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
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


    private static final String[] items = new String[]{
            "Contacts", "Ringtone", "Settings", "More"};

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

                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Intent select = new Intent(getApplicationContext(), RingdroidSelectActivity.class);
                        startActivity(select);
                    }
                }
                break;
            }
        }
    }

    void requestUsageStatsPermission() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && !hasUsageStatsPermission(this)) {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    boolean hasUsageStatsPermission(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow("android:get_usage_stats",
                android.os.Process.myUid(), context.getPackageName());
        boolean granted = mode == AppOpsManager.MODE_ALLOWED;
        return granted;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestUsageStatsPermission();
        Intent myIntent = new Intent(MainActivity.this, runningService.class);
        this.startService(myIntent);
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
                            Intent i = new Intent(MainActivity.this, RingdroidSelectActivity.class);
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
                return;
            }
        });

    }
}
