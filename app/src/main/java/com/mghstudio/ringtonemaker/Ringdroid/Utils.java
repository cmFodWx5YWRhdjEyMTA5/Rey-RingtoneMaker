package com.mghstudio.ringtonemaker.Ringdroid;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;

import com.mghstudio.ringtonemaker.Models.ContactsModel;
import com.mghstudio.ringtonemaker.Models.SongsModel;
import com.mghstudio.ringtonemaker.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

import static com.mghstudio.ringtonemaker.Ringdroid.Constants.REQUEST_ID_MULTIPLE_PERMISSIONS;
import static com.mghstudio.ringtonemaker.Ringdroid.Constants.REQUEST_ID_READ_CONTACTS_PERMISSION;
import static com.mghstudio.ringtonemaker.Ringdroid.Constants.REQUEST_ID_RECORD_AUDIO_PERMISSION;

//import android.content.res.TypedArray;
//import android.graphics.Color;

/**
 * Created by REYANSH on 4/8/2017.
 */

public class Utils {

    private static final String[] INTERNAL_COLUMNS = new String[]{
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.IS_RINGTONE,
            MediaStore.Audio.Media.IS_ALARM,
            MediaStore.Audio.Media.IS_NOTIFICATION,
            MediaStore.Audio.Media.IS_MUSIC,
            MediaStore.Audio.Media.ALBUM_ID,
            "\"" + MediaStore.Audio.Media.INTERNAL_CONTENT_URI + "\""
    };
    private static final String[] EXTERNAL_COLUMNS = new String[]{
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.IS_RINGTONE,
            MediaStore.Audio.Media.IS_ALARM,
            MediaStore.Audio.Media.IS_NOTIFICATION,
            MediaStore.Audio.Media.IS_MUSIC,
            MediaStore.Audio.Media.ALBUM_ID,
            "\"" + MediaStore.Audio.Media.EXTERNAL_CONTENT_URI + "\""
    };

    public static ArrayList<SongsModel> getSongList(Context context, boolean internal, String searchString) {

        String[] selectionArgs = null;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"
                + " OR " + MediaStore.Audio.Media.IS_RINGTONE + "!=0"
//               + " OR " +  MediaStore.Audio.Media.IS_ALARM + "!=0"
//               + " OR " +  MediaStore.Audio.Media.IS_NOTIFICATION + "!=0"
                ;
//        String selection = MediaStore.Audio.Media.DATA + " like ? ";
        if (searchString != null && searchString.length() > 0) {
            selection = "title LIKE ?";

            selectionArgs = new String[]{"%" + searchString + "%"};
        }

        ArrayList<SongsModel> songsModels = new ArrayList<>();
        Uri CONTENT_URI;
        String[] COLUMNS;

        if (internal) {
            CONTENT_URI = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
            COLUMNS = INTERNAL_COLUMNS;
        } else {
            CONTENT_URI =  MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            COLUMNS = EXTERNAL_COLUMNS;
        }
//        context.getContentResolver().q
        Cursor cursor = context.getContentResolver().query(
                CONTENT_URI,
                COLUMNS,
                selection,
                selectionArgs,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        if (cursor != null && cursor.moveToFirst()) {
            do {

                String fileType;
                try {
                    if (cursor.getString(6).equalsIgnoreCase("1")) {
                        fileType = Constants.IS_RINGTONE;
                    } else if (cursor.getString(7).equalsIgnoreCase("1")) {
                        fileType = Constants.IS_ALARM;
                    } else if (cursor.getString(8).equalsIgnoreCase("1")) {
                        fileType = Constants.IS_NOTIFICATION;
                    } else {
                        fileType = Constants.IS_MUSIC;
                    }
                } catch (Exception e) {
                    //lets assume its ringtone.
                    fileType = Constants.IS_RINGTONE;
                }

                SongsModel song = new SongsModel(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(10),
                        fileType);
                if (song.mDuration != null)
                    if (Integer.parseInt(song.mDuration) > 1000)
                        songsModels.add(song);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return songsModels;
    }

   /* public static Uri getAlbumArtUri(long paramInt) {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), paramInt);
    }

    public static String getAlbumArtUriString(long paramInt) {
        return "content://media/external/audio/albumart" + paramInt;
    }*/


    public static String makeShortTimeString(final Context context, long secs) {
        long hours, mins;

        hours = secs / 3600;
        secs %= 3600;
        mins = secs / 60;
        secs %= 60;

        final String durationFormat = context.getResources().getString(
                hours == 0 ? R.string.durationformatshort : R.string.durationformatlong);
        return String.format(durationFormat, hours, mins, secs);
    }

    public static void initImageLoader(Context context) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_art)
                .showImageForEmptyUri(R.drawable.default_art)
                .showImageOnFail(R.drawable.default_art)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .displayer(new FadeInBitmapDisplayer(500))
                .handler(new Handler()).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCache(new WeakMemoryCache()).defaultDisplayImageOptions(options).memoryCacheSizePercentage(13).build();
        ImageLoader.getInstance().init(config);
    }

    public static boolean checkSystemWritePermission(final Activity context) {
        boolean retVal = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            retVal = Settings.System.canWrite(context);
            if (retVal) {
                Log.d("TAG", "Can Write Settings ");
            } else {
                new AlertDialog.Builder(context)
                        .setTitle(R.string.set_ringtone)
                        .setMessage(context.getString(R.string.write_setting_text))
                        .setPositiveButton(R.string.alert_ok_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                                intent.setData(Uri.parse("package:" + context.getPackageName()));
                                context.startActivity(intent);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        }
        return retVal;
    }

    public static int getDimensionInPixel(Context context, int dp) {
        return (int) TypedValue.applyDimension(0, dp, context.getResources().getDisplayMetrics());
    }

    public static ArrayList<ContactsModel> getContacts(Context ctx) {
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

        Uri defaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(ctx, RingtoneManager.TYPE_RINGTONE);
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

    public static ArrayList<ContactsModel> getContacts(Context context, String searchQuery) {

        String selection = "(DISPLAY_NAME LIKE \"%" + searchQuery + "%\")";

        ArrayList<ContactsModel> contactsModels = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                new String[]{
                        ContactsContract.Contacts._ID,
                        ContactsContract.Contacts.CUSTOM_RINGTONE,
                        ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.Contacts.LAST_TIME_CONTACTED,
                        ContactsContract.Contacts.STARRED,
                        ContactsContract.Contacts.TIMES_CONTACTED},
                selection,
                null,
                "STARRED DESC, " +
                        "TIMES_CONTACTED DESC, " +
                        "LAST_TIME_CONTACTED DESC, " +
                        "DISPLAY_NAME ASC");

        Uri defaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE);
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


    /*public static int getMatColor(Context context) {
        int returnColor;
        {
            TypedArray colors = context.getResources().obtainTypedArray(R.array.mdcolor_500);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.BLACK);
            colors.recycle();
        }
        return returnColor;
    }*/

    public static boolean checkAndRequestPermissions(Activity activity, boolean ask) {
        int modifyAudioPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int readContacts = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (modifyAudioPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (readContacts != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            if (ask) {
                ActivityCompat.requestPermissions(activity,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                        REQUEST_ID_MULTIPLE_PERMISSIONS);
                return false;
            } else {
                return false;
            }
        }
        return true;
    }

    public static boolean checkAndRequestAudioPermissions(Activity activity) {
        int modifyAudioPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
        if (modifyAudioPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_ID_RECORD_AUDIO_PERMISSION);
            return false;
        }
        return true;
    }

    public static boolean checkAndRequestContactsPermissions(Activity activity) {
        int contactPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS);
        int modifyAudioPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (modifyAudioPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (contactPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_READ_CONTACTS_PERMISSION);
            return false;
        }
        return true;
    }

}
