package com.mghstudio.ringtonemaker.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mghstudio.ringtonemaker.Adapters.ContactsAdapter;
import com.mghstudio.ringtonemaker.Models.ContactsModel;
import com.mghstudio.ringtonemaker.R;
import com.mghstudio.ringtonemaker.Ringdroid.Utils;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;


/**
 * After a ringtone has been saved, this activity lets you pick a contact
 * and assign the ringtone to that contact.
 */
public class ChooseContactActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private Uri mRingtoneUri;

    /**
     * Called when the activity is first created.
     */
    private Toolbar mToolbar;
    private SearchView mSearchView;
    private RecyclerView mRecyclerView;
    private ContactsAdapter mContactsAdapter;
    private ArrayList<ContactsModel> mData;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Intent intent = getIntent();
        mRingtoneUri = intent.getData();
        setContentView(R.layout.choose_contact);

        mData = new ArrayList<>();
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(R.string.contacts);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getApplicationContext())
                        .color(Color.parseColor("#dadde2"))
                        .sizeResId(R.dimen.divider)
                        .marginResId(R.dimen.leftmargin, R.dimen.rightmargin)
                        .build());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mData = Utils.getContacts(this);
        mContactsAdapter = new ContactsAdapter(this, mData);
        mRecyclerView.setAdapter(mContactsAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem search = menu.findItem(R.id.menu_search);
        mSearchView = (SearchView) search.getActionView();

        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint(getString(R.string.search_library));

        mSearchView.setIconifiedByDefault(false);
        mSearchView.setIconified(false);

        MenuItem menuItem = menu.findItem(R.id.menu_search);
        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                finish();
                return false;
            }
        });

        menuItem.expandActionView();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mData = Utils.getContacts(this, newText);
        mContactsAdapter.updateData(mData);
        return false;
    }


    public void onItemClicked(int adapterPosition) {

        ContactsModel contactsModel = mData.get(adapterPosition);

        Uri uri = Uri.withAppendedPath(Contacts.CONTENT_URI, contactsModel.mContactId);

        ContentValues values = new ContentValues();
        values.put(Contacts.CUSTOM_RINGTONE, mRingtoneUri.toString());
        getContentResolver().update(uri, values, null, null);

        String message =
                getResources().getText(R.string.success_contact_ringtone) +
                        " " +
                        contactsModel.mName;

        Toast.makeText(this, message, Toast.LENGTH_SHORT)
                .show();
        finish();
    }


}