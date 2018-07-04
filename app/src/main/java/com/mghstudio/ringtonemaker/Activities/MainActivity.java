package com.mghstudio.ringtonemaker.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

import com.mghstudio.ringtonemaker.Adapters.CellAdapter;
import com.mghstudio.ringtonemaker.R;
import com.mghstudio.ringtonemaker.Ringdroid.Utils;

public class MainActivity extends AppCompatActivity {

    private GridView gridView;
    private static final String[] items = new String[]{
            "Contacts", "Ringtone", "Settings", "More"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        gridView = (GridView) findViewById(R.id.gridView);

        gridView.setAdapter(new CellAdapter(this, items));

        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        if(Utils.checkAndRequestContactsPermissions(MainActivity.this)){
                            Intent contact = new Intent(MainActivity.this, ContactActivity.class);
                            startActivity(contact);

                        }
                        break;
                    case 1:
                        Intent i = new Intent(MainActivity.this, RingdroidSelectActivity.class);
                        startActivity(i);
                        break;
                    case 2:
                        Intent j = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(j);
                }
                return;
            }
        });
    }
}
