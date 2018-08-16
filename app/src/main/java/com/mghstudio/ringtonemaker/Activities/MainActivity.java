package com.mghstudio.ringtonemaker.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mghstudio.ringtonemaker.Adapters.CellAdapter;
import com.mghstudio.ringtonemaker.Models.AdsConfig;
import com.mghstudio.ringtonemaker.R;
import com.mghstudio.ringtonemaker.Ringdroid.Utils;
import com.mghstudio.ringtonemaker.service.MyService;
import com.mghstudio.ringtonemaker.utils.AppConstants;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.mghstudio.ringtonemaker.Ringdroid.Constants.REQUEST_ID_MULTIPLE_PERMISSIONS;
import static com.mghstudio.ringtonemaker.Ringdroid.Constants.REQUEST_ID_READ_CONTACTS_PERMISSION;

public class MainActivity extends AppCompatActivity {
    private AdView adView;
    private SharedPreferences mPrefs;
    private static final String[] items = new String[]{
            "Contacts", "Ringtone", "Settings", "More apps"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // time when installing app

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
        GridView gridView = findViewById(R.id.gridView);

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
                        Intent a = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=DINH+VIET+HUNG"));
                        startActivity(a);
                }
            }
        });

        getAppConfig();
    }

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

    private void getAppConfig()
    {
        mPrefs = getSharedPreferences("adsserver", 0);
        String uuid;
        if (mPrefs.contains("uuid")) {
            uuid = mPrefs.getString("uuid", UUID.randomUUID().toString());
        } else {
            uuid = UUID.randomUUID().toString();
            mPrefs.edit().putString("uuid", "ring"+uuid).commit();
        }

        OkHttpClient client = new OkHttpClient();
        Request okRequest = new Request.Builder()
                .url(AppConstants.URL_CONFIG)
                .build();

        client.newCall(okRequest).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                Gson gson = new GsonBuilder().create();//"{\"delayAds\":24,\"delayService\":24,\"idFullService\":\"/21617015150/734252/21734366950\",\"intervalService\":10,\"percentAds\":50}";//
//                Log.d("caomui111111", result + "");
//                AdsConfig jsonConfig = gson.fromJson(result, AdsConfig.class);
                JsonObject jsonObject = new JsonParser().parse(response.body().string()).getAsJsonObject();
//                Log.d("caomui===", jsonObject.get("delayService").getAsString());
//                Log.d("caomui11", jsonConfig.intervalService + "");
//                Log.d("caomui22", jsonObject.get("idFullService").getAsString());
//                Log.d("caomui33",  jsonConfig.delayService + "");

                mPrefs.edit().putInt("intervalService",jsonObject.get("intervalService").getAsInt()).commit();
                mPrefs.edit().putString("idFullService",jsonObject.get("idFullService").getAsString()).commit();
                mPrefs.edit().putInt("delayService",jsonObject.get("delayService").getAsInt()).commit();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent myIntent = new Intent(MainActivity.this, MyService.class);
                        startService(myIntent);
                    }
                });

            }
        });
    }
}
