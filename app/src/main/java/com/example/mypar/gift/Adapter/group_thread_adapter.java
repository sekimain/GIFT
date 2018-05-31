package com.example.mypar.gift.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mypar.gift.R;
import com.example.mypar.gift.Structure.Thread_Main;

import java.util.ArrayList;

public class group_thread_adapter extends ArrayAdapter<Thread_Main> {
    private Context context;
    private int layoutResourceId, adminCode, Usercode;
    private ArrayList<Thread_Main> listData;
    private ImageView accept;

    public group_thread_adapter(Context context, int layoutResourceId, ArrayList<Thread_Main> listData, int adminCode, int usercode) {
        super(context, layoutResourceId, listData);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.listData = listData;
        this.adminCode = adminCode;
        this.Usercode = usercode;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(layoutResourceId, parent, false);
        }

        TextView tvText1 = row.findViewById(R.id.group_thread_Author);
        TextView tvText2 = row.findViewById(R.id.group_thread_text);
        accept = row.findViewById(R.id.just_display);

        if (listData.get(position).getuserCode() != adminCode) {
            accept.setVisibility(View.VISIBLE);
        }

        tvText1.setText(listData.get(position).getTitle());
        tvText2.setText(listData.get(position).getDate());

        if (listData.get(position).getFileFlag()!=0){
            ImageView h_file = row.findViewById(R.id.group_thread_file);
            h_file.setVisibility(View.VISIBLE);
        }

        if (!listData.get(position).getFile().equals("")) {
            ImageView h_image = row.findViewById(R.id.group_thread_image);
            h_image.setVisibility(View.VISIBLE);
        }
        return row;
    }
}
