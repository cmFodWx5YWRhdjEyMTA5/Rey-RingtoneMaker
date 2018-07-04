package com.mghstudio.ringtonemaker.Models;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by REYANSH on 4/13/2017.
 */

public class ContactsModel {
    public String mName;
    public String mContactId;
    public String mRingtone;
    public Bitmap mPhoto;
    public Uri mUri;

    public ContactsModel(String name, String contactId, String ringtone) {
        mName = name;
        mContactId = contactId;
        mRingtone = ringtone;
    }

    public ContactsModel(String name, String contactId, Uri uri) {
        mName = name;
        mContactId = contactId;
        mUri = uri;
    }

    public ContactsModel(){}
}
