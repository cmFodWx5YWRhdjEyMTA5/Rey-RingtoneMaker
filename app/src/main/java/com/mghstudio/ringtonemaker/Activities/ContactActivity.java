package com.mghstudio.ringtonemaker.Activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.mghstudio.ringtonemaker.Adapters.AllContactsAdapter;
import com.mghstudio.ringtonemaker.Models.ContactsModel;
import com.mghstudio.ringtonemaker.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private SearchView mSearchView;
    private RecyclerView mRecyclerView;
    private AllContactsAdapter mContactsAdapter;
    private ArrayList<ContactsModel> mData;
    Uri mRingtoneUri;
    ArrayList<String> list;
    String stringUri;
    String ringtoneName;
    private int checked = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact);

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

        mData = getContacts(getApplicationContext());
        mContactsAdapter = new AllContactsAdapter(this, mData);
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
//        Uri defaultRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        String ringToneName;
        if (defaultRingtoneUri == null) {
            // if ringtone_uri is null get Default Ringtone
            defaultRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        }
//        Ringtone ringtone = RingtoneManager.getRingtone(this, defaultRingtoneUri);
//        ringToneName = ringtone.getTitle(this);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Uri customUri;
                if (cursor.getString(1) == null) {
                    customUri = defaultRingtoneUri;
                } else {
                    customUri = Uri.parse(cursor.getString(1));
                }
                ringToneName = RingtoneManager.getRingtone(ctx, customUri).getTitle(ctx);

                ContactsModel contactsModel = new ContactsModel(cursor.getString(2),
                        cursor.getString(0), ringToneName);
                contactsModels.add(contactsModel);
            } while (cursor.moveToNext());
        }
        if (cursor != null)
            cursor.close();

        return contactsModels;
    }

    private ArrayList<String> getListRingtones() {
        ArrayList<String> list = new ArrayList<>();
//        Uri defaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(ContactActivity.this, RingtoneManager.TYPE_RINGTONE);
//        RingtoneManager.setActualDefaultRingtoneUri(getApplicationContext(),RingtoneManager.TYPE_RINGTONE, defaultRingtoneUri);
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

        list = getListRingtones();
        String[] animals = new String[list.size()];
        list.toArray(animals);
        String tempRing = mContactsAdapter.getItem(adapterPosition).mRingtone;


//         = pres.getString(tempRing, null);

        int temChecked = 0;
        for (int i = 0; i < list.size(); i++)
            if (tempRing.equalsIgnoreCase(list.get(i)))
                temChecked = i;
        checked = temChecked;


        builder.setSingleChoiceItems(animals, checked, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checked = which;
                ringtoneName = list.get(which);
                SharedPreferences pres = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                stringUri = pres.getString(ringtoneName, null);
                if (stringUri != null) {

                    mRingtoneUri = Uri.parse(stringUri);
                    MediaPlayer md = MediaPlayer.create(getApplicationContext(), mRingtoneUri);
                    md.start();
//                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), mRingtoneUri);
//                r.play();
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

                //reload data in recyclerview
                mContactsAdapter.updateData(getContacts(getApplication()));
                mContactsAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", null);
        mContactsAdapter.notifyDataSetChanged();

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
