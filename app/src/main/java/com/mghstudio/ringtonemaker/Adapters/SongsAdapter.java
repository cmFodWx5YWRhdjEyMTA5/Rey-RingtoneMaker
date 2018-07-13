package com.mghstudio.ringtonemaker.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mghstudio.ringtonemaker.Activities.RingdroidSelectActivity;
import com.mghstudio.ringtonemaker.Models.SongsModel;
import com.mghstudio.ringtonemaker.R;
import com.mghstudio.ringtonemaker.Ringdroid.Constants;
import com.mghstudio.ringtonemaker.Ringdroid.Utils;
import com.mghstudio.ringtonemaker.Views.BubbleTextGetter;

import java.util.ArrayList;

/**
 * Created by REYANSH on 4/8/2017.
 */

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.ItemHolder> implements BubbleTextGetter {

    public SongsAdapter(RingdroidSelectActivity ringdroidSelectActivity, ArrayList<SongsModel> data) {
        mRingdroidSelectActivity = ringdroidSelectActivity;
        mData = data;
    }

    private RingdroidSelectActivity mRingdroidSelectActivity;
    private ArrayList<SongsModel> mData;


    @Override
    public SongsAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_songs, parent, false));
    }

    @Override
    public void onBindViewHolder(SongsAdapter.ItemHolder holder, int position) {

        holder.mDuration.setText(Utils.makeShortTimeString(mRingdroidSelectActivity.getApplicationContext(),
                Integer.parseInt(mData.get(position).mDuration) / 1000));


        holder.mSongName.setText(mData.get(position).mSongsName);
        holder.mArtistName.setText(mData.get(position).mArtistName);
        if (mData.get(position).mFileType.equalsIgnoreCase(Constants.IS_MUSIC)) {
            holder.mSongsImage.setImageResource(R.drawable.default_art);

        } else {
            holder.mSongsImage.setImageResource(R.drawable.bell_50);
        }
//            ImageLoader.getInstance().displayImage(Utils.getAlbumArtUri(Long.parseLong(mData.get(position).mAlbumId)).toString(),
//                    holder.mSongsImage,
//                    new DisplayImageOptions.Builder().cacheInMemory(true)
//                            .showImageOnFail(R.drawable.default_art)
//                            .showImageOnLoading(R.drawable.default_art)
//                            .resetViewBeforeLoading(true)
//                            .build());
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public String getTextToShowInBubble(int pos) {
        try {
            return String.valueOf(mData.get(pos).mSongsName.charAt(0));
        } catch (Exception e) {
            e.printStackTrace();
            return "-";
        }
    }

    public void updateData(ArrayList<SongsModel> data) {
        this.mData=data;
        notifyDataSetChanged();
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mSongsImage;
        private TextView mSongName;
        private TextView mArtistName;
        private TextView mDuration;
        private ImageView mPopUpMenu;

        public ItemHolder(View itemView) {
            super(itemView);
            mSongsImage = itemView.findViewById(R.id.album_art_image_view);
            mSongName = itemView.findViewById(R.id.song_name);
            mArtistName = itemView.findViewById(R.id.artist_name);
            mDuration = itemView.findViewById(R.id.song_duration);
            mPopUpMenu = itemView.findViewById(R.id.overflow);
            mPopUpMenu.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.overflow:
                    mRingdroidSelectActivity.onPopUpMenuClickListener(v, getAdapterPosition());
                    return;
            }
            mRingdroidSelectActivity.onItemClicked(getAdapterPosition());
        }
    }
}
