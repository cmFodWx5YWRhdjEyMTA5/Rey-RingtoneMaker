package com.mghstudio.ringtonemaker.Adapters;

import android.media.RingtoneManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mghstudio.ringtonemaker.Activities.ChooseContactActivity;
import com.mghstudio.ringtonemaker.Models.ContactsModel;
import com.mghstudio.ringtonemaker.R;

import java.util.ArrayList;

/**
 * Created by REYANSH on 4/13/2017.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ItemHolder> {

    private ChooseContactActivity mChooseContactActivity;
    private ArrayList<ContactsModel> mData;

    public ContactsAdapter(ChooseContactActivity ringdroidSelectActivity, ArrayList<ContactsModel> data) {
        mChooseContactActivity = ringdroidSelectActivity;
        mData = data;
    }

    public ContactsAdapter(ArrayList<ContactsModel> data) {
        mData = data;
    }


    @Override
    public ContactsAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactsAdapter.ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contacts, parent, false));
    }

    @Override
    public void onBindViewHolder(ContactsAdapter.ItemHolder holder, int position) {
        holder.mContactName.setText(mData.get(position).mName);
        if (mData.get(position).mRingtone == null) {
            mData.get(position).mRingtone = RingtoneManager.getRingtone(mChooseContactActivity, mData.get(position).mUri).getTitle(mChooseContactActivity);
            holder.mContactDefault.setText(mData.get(position).mRingtone);
        } else
            holder.mContactDefault.setText(mData.get(position).mRingtone);

    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void updateData(ArrayList<ContactsModel> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mContactName;
        private ImageView mContactImage;
        private TextView mContactDefault;

        private ItemHolder(View itemView) {
            super(itemView);
            mContactName = itemView.findViewById(R.id.contact_name);
            mContactImage = itemView.findViewById(R.id.one_letter);
            mContactDefault = itemView.findViewById(R.id.contact_default);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mChooseContactActivity != null) {
                mChooseContactActivity.onItemClicked(getAdapterPosition());
            } else {

            }
        }
    }
}
