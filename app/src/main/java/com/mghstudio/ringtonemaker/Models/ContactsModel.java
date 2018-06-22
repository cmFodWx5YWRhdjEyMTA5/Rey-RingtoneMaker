package com.mghstudio.ringtonemaker.Models;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by REYANSH on 4/13/2017.
 */

public class ContactsModel {
    public String mName;
    public String mContactId;
    public Bitmap mPhoto;
    public Uri mUri;

    public ContactsModel(String name, String contactId) {
        mName = name;
        mContactId = contactId;
    }

    public ContactsModel(){}
}
