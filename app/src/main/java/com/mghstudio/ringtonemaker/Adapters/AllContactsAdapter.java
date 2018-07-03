package com.mghstudio.ringtonemaker.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mghstudio.ringtonemaker.Activities.ContactActivity;
import com.mghstudio.ringtonemaker.Models.ContactsModel;
import com.mghstudio.ringtonemaker.R;

import java.util.ArrayList;

public class AllContactsAdapter extends RecyclerView.Adapter<AllContactsAdapter.ItemHolder> {

    private ContactActivity mContactActivity;
    private ArrayList<ContactsModel> mData;

    public AllContactsAdapter(ContactActivity contactActivity, ArrayList<ContactsModel> data) {
        mContactActivity = contactActivity;
        mData = data;
    }

    public AllContactsAdapter(ArrayList<ContactsModel> data) {
        mData = data;
    }


    @Override
    @NonNull
    public AllContactsAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AllContactsAdapter.ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_contacts, parent, false));
    }

    public ContactsModel getItem(int pos) {
        return mData.get(pos);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        holder.mContactName.setText(mData.get(position).mName);
        holder.mContactRingtone.setText(mData.get(position).mRingtone);
    }

    /*@Override
    public void onBindViewHolder(ContactsAdapter.ItemHolder holder, int position) {
        holder.mContactName.setText(mData.get(position).mName);

        try {
            String letter = String.valueOf(mData.get(position).mName.charAt(0));
//            holder.mOneLetter.setText(letter);
            holder.mContactImage.setBackgroundColor(Utils.getMatColor(ContactActivity.getApplicationContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

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
        private TextView mContactRingtone;
        private ImageView mContactImage;

        private ItemHolder(View itemView) {
            super(itemView);
            mContactName = itemView.findViewById(R.id.all_contact_name);
            mContactImage = itemView.findViewById(R.id.all_contact_imageview);
            mContactRingtone = itemView.findViewById(R.id.all_contact_ringtone);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mContactActivity != null) {
                mContactActivity.onItemClicked(v, getAdapterPosition());

            } else {

            }
        }
    }

}


