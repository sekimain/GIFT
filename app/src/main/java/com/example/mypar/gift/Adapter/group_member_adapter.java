package com.example.mypar.gift.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mypar.gift.R;
import com.example.mypar.gift.Structure.Group_Member_Data;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class group_member_adapter extends ArrayAdapter<Group_Member_Data> {
    private Context context;
    private int layoutResourceId;
    private ArrayList<Group_Member_Data> listData;

    public  group_member_adapter( Context context, int layoutResourceId, ArrayList<Group_Member_Data> listData) {
        super(context, layoutResourceId, listData);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.listData = listData;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;

        if(row == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(layoutResourceId, parent, false);
        }

        ImageView imv = row.findViewById(R.id.group_member_photo);
        TextView tvText1 = row.findViewById(R.id.group_member_name);
        TextView tvText2 = row.findViewById(R.id.group_member_ID);

        imv.setBackground(new ShapeDrawable(new OvalShape()));
        if(Build.VERSION.SDK_INT>=21){
            imv.setClipToOutline(true);
        }
        tvText1.setText(listData.get(position).getName());
        tvText2.setText(listData.get(position).getID());
        if((listData.get(position).getPhoto()==null) || (listData.get(position).getPhoto().equals("empty"))){
            Picasso.with(context).load(R.drawable.main_group_default_photo).fit().into(imv);
        }else {
            Picasso.with(context).load(listData.get(position).getPhoto()).fit().into(imv);
         }
        return row;
    }
}