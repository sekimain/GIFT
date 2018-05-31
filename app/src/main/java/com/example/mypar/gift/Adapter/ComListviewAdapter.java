package com.example.mypar.gift.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mypar.gift.R;
import com.example.mypar.gift.Structure.commentClass;

import java.util.ArrayList;

/**
 * Created by YGLab on 2018-04-04.
 */

public class ComListviewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<commentClass> data;
    private int layout;

    public ComListviewAdapter(Context context, int layout, ArrayList<commentClass> data){
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data= data;
        this.layout = layout;
    }

    @Override
    public int getCount(){return data.size();}

    @Override
    public String getItem(int position){return data.get(position).getArticle();}

    @Override
    public long getItemId(int position){return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent){


        if(convertView == null){
            convertView = inflater.inflate(layout,parent,false);
        }

        commentClass com = data.get(position);

        TextView comment = convertView.findViewById(R.id.comBody);
        TextView owner = convertView.findViewById(R.id.comOwner);

        comment.setText(com.getArticle());
        owner.setText(com.getuserCode());
        return convertView;
    }
}
