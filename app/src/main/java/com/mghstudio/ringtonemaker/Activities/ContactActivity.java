package com.mghstudio.ringtonemaker.Activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.mghstudio.ringtonemaker.Adapters.AllContactsAdapter;
import com.mghstudio.ringtonemaker.Models.ContactsModel;
import com.mghstudio.ringtonemaker.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity {


    private AllContactsAdapter mContactsAdapter;
    private ArrayList<ContactsModel> mData;
    Uri mRingtoneUri;
    ArrayList<String> list = new ArrayList<>();
    String stringUri;
    String ringtoneName;
    private int checked = 0;

    private MediaPlayer md;
    private boolean isClicked = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact);
        AdView adView;

        adView = new AdView(this, "2199797023369826_2269185393097655", AdSize.BANNER_HEIGHT_50);

        // Find the Ad Container
        RelativeLayout adContainer = findViewById(R.id.baner2);

        // Add the ad view to your activity layout
        adContainer.addView(adView);

        // Request an ad
        adView.loadAd();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.contacts);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView mRecyclerView;
        mRecyclerView = findViewById(R.id.contact_recyclerView);
        mRecyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getApplicationContext())
                        .color(Color.parseColor("#dadde2"))
                        .sizeResId(R.dimen.divider)
                        .marginResId(R.dimen.leftmargin, R.dimen.rightmargin)
                        .build());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mData = getContacts(getApplicationContext());
        mContactsAdapter = new AllContactsAdapter(this, mData);
        mRecyclerView.setAdapter(mContactsAdapter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (md != null) {
            md.release();
            md = null;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public ArrayList<ContactsModel> getContacts(Context ctx) {
        ArrayList<ContactsModel> contactsModels = new ArrayList<>();

        Cursor cursor = ctx.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                new String[]{
                        ContactsContract.Contacts._ID,
                        ContactsContract.Contacts.CUSTOM_RINGTONE,
                        ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.Contacts.LAST_TIME_CONTACTED,
                        ContactsContract.Contacts.STARRED,
                        ContactsContract.Contacts.TIMES_CONTACTED},
                null,
                null,
                "STARRED DESC, " +
                        "TIMES_CONTACTED DESC, " +
                        "LAST_TIME_CONTACTED DESC, " +
                        "DISPLAY_NAME ASC");

        Uri defaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(ContactActivity.this, RingtoneManager.TYPE_RINGTONE);
        if (defaultRingtoneUri == null) {
            // if ringtone_uri is null get Default Ringtone
            defaultRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        }
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Uri customUri;
                if (cursor.getString(1) == null) {
                    customUri = defaultRingtoneUri;
                } else {
                    customUri = Uri.parse(cursor.getString(1));
                }
                ContactsModel contactsModel = new ContactsModel(cursor.getString(2), cursor.getString(0), customUri);
                contactsModels.add(contactsModel);
            } while (cursor.moveToNext());
        }
        if (cursor != null)
            cursor.close();

        return contactsModels;
    }

    private ArrayList<String> getListRingtones() {
        ArrayList<String> list = new ArrayList<>();

        RingtoneManager manager = new RingtoneManager(this);
        manager.setType(RingtoneManager.TYPE_RINGTONE);
        Cursor cursor = manager.getCursor();
        while (cursor.moveToNext()) {
            String title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            String uri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX);
            String id = cursor.getString(RingtoneManager.ID_COLUMN_INDEX);
            // Do something with the title and the URI of ringtone
            SharedPreferences pres = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            pres.edit().putString(title, uri + "/" + id).apply();
            list.add(title);
        }
        return list;
    }

    public void onItemClicked(View v, final int adapterPosition) {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(ContactActivity.this);
        builder.setTitle("Choose a Ringtone");
        String defaultRingtone = "Default ringtone";
        list.clear();
        list.add(defaultRingtone);
        list.addAll(getListRingtones());
        String[] songs = new String[list.size()];
        list.toArray(songs);
        String tempRing = mContactsAdapter.getItem(adapterPosition).mRingtone;

        int temChecked = 0;
        for (int i = 0; i < list.size(); i++)
            if (tempRing.equalsIgnoreCase(list.get(i)))
                temChecked = i;
        checked = temChecked;


        builder.setSingleChoiceItems(songs, checked, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checked = which;
                ringtoneName = list.get(which);
                SharedPreferences pres = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                if (which == 0) {
                    mRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(ContactActivity.this, RingtoneManager.TYPE_RINGTONE);
                    if (isClicked && md != null) {
                        md.release();
                    }
                    if (mRingtoneUri == null) {
                        mRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                        Ringtone ringtoneAlarm = RingtoneManager.getRingtone(getApplicationContext(), mRingtoneUri);
                        ringtoneAlarm.play();
                    } else {
                        try {
                            md = new MediaPlayer();
                            md.setDataSource(getApplicationContext(), mRingtoneUri);
                            md.prepare();
                            md.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    isClicked = true;
                } else {
                    stringUri = pres.getString(ringtoneName, null);
                    if (stringUri != null) {
                        mRingtoneUri = Uri.parse(stringUri);
                        if (isClicked && md != null) {
                            md.release();
                        }
                        md = MediaPlayer.create(getApplicationContext(), mRingtoneUri);
                        md.start();
                        isClicked = true;

                    }
                }


            }
        });


// add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked OK
                ListView lw = ((AlertDialog) dialog).getListView();
                Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                Log.d("Selected", checkedItem.toString() + "");
                checked = which;

                //set ringtone for contact
                ContactsModel contactsModel = mData.get(adapterPosition);

                Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, contactsModel.mContactId);

                ContentValues values = new ContentValues();
                values.put(ContactsContract.Contacts.CUSTOM_RINGTONE, mRingtoneUri.toString());
                getContentResolver().update(uri, values, null, null);

                if (md != null) {
                    md.release();
                    isClicked = false;
                }


                //reload data in recyclerview
                mContactsAdapter.updateData(getContacts(getApplication()));
                mContactsAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (md != null) {
                    md.release();
                    isClicked = false;
                }
            }
        });
        mContactsAdapter.notifyDataSetChanged();

// create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
