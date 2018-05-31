package com.example.mypar.gift.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mypar.gift.R;
import com.example.mypar.gift.Structure.Main_Group_List;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class main_group_adapter extends ArrayAdapter<Main_Group_List>{
    private Context context;
    private int layoutResourceId, adminCode;
    private ArrayList<Main_Group_List> listData;

    public main_group_adapter( Context context, int layoutResourceId, int userCode, ArrayList<Main_Group_List> listData) {
        super(context, layoutResourceId, listData);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.listData = listData;
        this.adminCode = userCode;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        View row = convertView;

        if(row == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(layoutResourceId, parent, false);
        }
        TextView tvText1 = row.findViewById(R.id.main_group_name);
        TextView tvText2 = row.findViewById(R.id.main_group_intro);
        final ImageView imgView = row.findViewById(R.id.main_group_photo);

        tvText1.setText(listData.get(position).getName());
        tvText2.setText(listData.get(position).getIntro());
        imgView.setBackground(new ShapeDrawable(new OvalShape()));
        if(Build.VERSION.SDK_INT>=21){
            imgView.setClipToOutline(true);
        }
        if((listData.get(position).getPhoto()==null) || (listData.get(position).getPhoto().equals("empty"))){
            Picasso.with(context).load(R.drawable.ic_launcher_round).fit().into(imgView);
        }else {
            Picasso.with(context).load(listData.get(position).getPhoto()).fit().into(imgView);
        }

        if(adminCode == listData.get(position).getGroup_Admin()){
            ImageView master = row.findViewById(R.id.main_group_master);
            master.setVisibility(View.VISIBLE);
        }

        return row;
    }
}
