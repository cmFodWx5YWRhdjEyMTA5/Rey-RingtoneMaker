package com.mghstudio.ringtonemaker.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mghstudio.ringtonemaker.R;

public class CellAdapter extends BaseAdapter {
    private Context context;
    private final String[] items;

    public CellAdapter(Context context, String [] item){
    this.context = context;
    this.items = item;
    }
    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

//            gridView = new View(context);

            // get layout from cell.xml
            gridView = inflater.inflate(R.layout.cell, null);

            // set value into textview
            TextView textView = gridView
                    .findViewById(R.id.grid_item_textview);
            textView.setText(items[position]);

            // set image based on selected text
            ImageView imageView = gridView
                    .findViewById(R.id.grid_item_image);

            String item = items[position];

            if (item.equals("Contacts")) {
                imageView.setImageResource(R.drawable.btn_contact);
            } else if (item.equals("Ringtone")) {
                imageView.setImageResource(R.drawable.btn_ringtone);
            } else if (item.equals("Settings")) {
                imageView.setImageResource(R.drawable.btn_setting);
            } else {
                imageView.setImageResource(R.drawable.btn_more);
            }

        } else {
            gridView = convertView;
        }

        return gridView;
    }
}
