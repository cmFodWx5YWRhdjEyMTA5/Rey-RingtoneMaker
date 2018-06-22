package com.mghstudio.ringtonemaker.Activities;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.mghstudio.ringtonemaker.Adapters.AllContactsAdapter;
import com.mghstudio.ringtonemaker.Adapters.ContactsAdapter;
import com.mghstudio.ringtonemaker.Models.ContactsModel;
import com.mghstudio.ringtonemaker.R;
import com.mghstudio.ringtonemaker.Ringdroid.Utils;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private SearchView mSearchView;
    private RecyclerView mRecyclerView;
    private AllContactsAdapter mContactsAdapter;
    private ArrayList<ContactsModel> mData;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact);

        mData = new ArrayList<>();
//        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.contacts);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        mRecyclerView = findViewById(R.id.contact_recyclerView);
        mRecyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getApplicationContext())
                        .color(Color.parseColor("#dadde2"))
                        .sizeResId(R.dimen.divider)
                        .marginResId(R.dimen.leftmargin, R.dimen.rightmargin)
                        .build());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        mData = Utils.getContacts(this, "");
        mData = getContacts(getApplicationContext());
        mContactsAdapter = new AllContactsAdapter(this,mData);
        mRecyclerView.setAdapter(mContactsAdapter);
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
                        ContactsContract.RawContacts.CUSTOM_RINGTONE,
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


        if (cursor != null && cursor.moveToFirst()) {
            do {
                ContactsModel contactsModel = new ContactsModel(cursor.getString(2),
                        cursor.getString(0), (cursor.getString(1)));
                contactsModels.add(contactsModel);
            } while (cursor.moveToNext());
        }
        if(cursor != null)
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
            // Do something with the title and the URI of ringtone
            list.add(title);
        }
        return list;
    }

    public void onItemClicked(int adapterPosition) {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(ContactActivity.this);
        builder.setTitle("Choose an animal");

// add a radio button list
//        String[] animals = {"horse", "cow", "camel", "sheep", "goat"};
        ArrayList<String> list = getListRingtones();
        String []animals = new String[list.size()];
        list.toArray(animals);

        int checkedItem = 1; // cow
        builder.setSingleChoiceItems(animals, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user checked an item
            }
        });

// add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked OK
            }
        });
        builder.setNegativeButton("Cancel", null);

// create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        /*ContactsModel contactsModel = mData.get(adapterPosition);

        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, contactsModel.mContactId);

        ContentValues values = new ContentValues();
//        values.put(ContactsContract.Contacts.CUSTOM_RINGTONE, mRingtoneUri.toString());
        getContentResolver().update(uri, values, null, null);

        String message =
                getResources().getText(R.string.success_contact_ringtone) +
                        " " +
                        contactsModel.mName;

        Toast.makeText(this, message, Toast.LENGTH_SHORT)
                .show();
        finish();*/
    }
}
