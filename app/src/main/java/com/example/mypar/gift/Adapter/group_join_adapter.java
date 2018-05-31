package com.example.mypar.gift.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mypar.gift.Model.MYResponse;
import com.example.mypar.gift.Model.Notification;
import com.example.mypar.gift.Model.Sender;
import com.example.mypar.gift.R;
import com.example.mypar.gift.Retrofit.APIService;
import com.example.mypar.gift.Service.Common;
import com.example.mypar.gift.Structure.Group_Member_Data;
import com.example.mypar.gift.server.Group_Join_Request;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class group_join_adapter extends ArrayAdapter<Group_Member_Data> {
    private Context context;
    private int layoutResourceId;
    private ArrayList<Group_Member_Data> listData;
    private Group_Member_Data data;
    private int Group_code;

    public  group_join_adapter( Context context, int layoutResourceId, ArrayList<Group_Member_Data> listData, int group_code ){
        super(context, layoutResourceId, listData);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.listData = listData;
        this.Group_code = group_code;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        View row = convertView;
        if(row == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(layoutResourceId, parent, false);
        }
        data = listData.get(position);

        ImageView imv = row.findViewById(R.id.group_join_photo);
        TextView tvText1 = row.findViewById(R.id.group_join_name);
        TextView tvText2 = row.findViewById(R.id.group_join_ID);


        imv.setBackground(new ShapeDrawable(new OvalShape()));
        imv.setClipToOutline(true);

        tvText1.setText(data.getName());
        tvText2.setText(data.getID());

        if((data.getPhoto()==null) || (data.getPhoto().equals(""))){
            Picasso.with(context).load(R.drawable.main_group_default_photo).fit().into(imv);
        }else {
            Picasso.with(context).load(data.getPhoto()).fit().into(imv);
        }


        // ------------------------ accept --------------------- //
        ImageButton accept_btn = row.findViewById(R.id.group_join_accept);
        accept_btn.setTag(position);
        accept_btn.requestFocus();
        accept_btn.setFocusable(true);
        accept_btn.setEnabled(true);
        accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Would you like to invite this person to a group?");


                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Group_Member_Data accept_data = listData.get(Integer.parseInt(v.getTag().toString()));


                        Response.Listener<String> response = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try{
                                    JSONObject jsonObject = new JSONObject(response);
                                    String userToken= null,groupName=null;
                                    userToken= jsonObject.getString("userToken");
                                    groupName=jsonObject.getString("groupName");


                                    Common.currentToken =userToken;
                                    APIService mService = Common.getFCMClient();
                                    /////
                                    Notification notification = new Notification("Group join success!",groupName);
                                    Sender sender = new Sender(Common.currentToken,notification);

                                    mService.sendNotification(sender)
                                            .enqueue(new Callback<MYResponse>() {
                                                @Override
                                                public void onResponse(Call<MYResponse> call, retrofit2.Response<MYResponse> response) {
                                                    if(response.body().success==1)
                                                    {
                                                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<MYResponse> call, Throwable t) {

                                                }
                                            });
                                    //////
                                    listData.remove(Integer.parseInt(v.getTag().toString()));
                                    Refreshing();
                                }
                                catch(Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        };
                        Group_Join_Request joinRequest = new Group_Join_Request(accept_data.getUsercode(), Group_code, 1, response);
                        RequestQueue queue = Volley.newRequestQueue(context);
                        queue.add(joinRequest);
                    }
                });
                alert.setNegativeButton("Cancel", null);
                alert.show();

            }
        });

        // ------------------------ denial ---------------------- //
        ImageButton denial_btn = row.findViewById(R.id.group_join_delete);
        denial_btn.requestFocus();
        denial_btn.setTag(position);
        denial_btn.setFocusable(true);
        denial_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Do you really want to deny this person?");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //int usercode = data.getUsercode();
                        Group_Member_Data deny_data = listData.get(Integer.parseInt(v.getTag().toString()));

                        Response.Listener<String> response = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try{
                                    JSONObject jsonObject = new JSONObject(response);
                                    String userToken= null,groupName=null;
                                    userToken= jsonObject.getString("userToken");
                                    groupName=jsonObject.getString("groupName");



                                    Common.currentToken =userToken;
                                    APIService mService = Common.getFCMClient();
                                    /////
                                    Notification notification = new Notification("You are denied from group.",groupName);
                                    Sender sender = new Sender(Common.currentToken,notification);

                                    mService.sendNotification(sender)
                                            .enqueue(new Callback<MYResponse>() {
                                                @Override
                                                public void onResponse(Call<MYResponse> call, retrofit2.Response<MYResponse> response) {
                                                    if(response.body().success==1)
                                                    {
                                                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<MYResponse> call, Throwable t) {

                                                }
                                            });

                                    listData.remove(Integer.parseInt(v.getTag().toString()));
                                    Refreshing();
                                }
                                catch(Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        };
                        Group_Join_Request joinRequest = new Group_Join_Request(deny_data.getUsercode(), Group_code, 0, response);
                        RequestQueue queue = Volley.newRequestQueue(context);
                        queue.add(joinRequest);
                    }
                });
                alert.setNegativeButton("Cancel", null);
                alert.show();

            }
        });

        // ------------------------ info -------------------------- //
        ImageButton info_btn = row.findViewById(R.id.group_join_info);
        info_btn.setTag(position);
        info_btn.requestFocus();
        info_btn.setFocusable(true);
        info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Group_Member_Data info_data = listData.get(Integer.parseInt(v.getTag().toString()));

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.layout_member_data);

                TextView user_name = dialog.findViewById(R.id.member_data_User_name);       user_name.setText(info_data.getName());
                TextView user_id = dialog.findViewById(R.id.member_data_User_id);           user_id.setText(info_data.getID());
                TextView user_email = dialog.findViewById(R.id.member_data_User_email);     user_email.setText(info_data.getemail());
                TextView user_phone = dialog.findViewById(R.id.member_data_User_phone);     user_phone.setText(info_data.getPhone());
                TextView user_country = dialog.findViewById(R.id.member_data_User_country); user_country.setText(info_data.getCountry());
                ImageView user_photo = dialog.findViewById(R.id.member_data_User_photo);

                if((info_data.getPhoto()==null) || (info_data.getPhoto().equals("empty"))){
                    user_photo.setImageResource(R.drawable.main_group_default_photo);
                }else {
                    Picasso.with(context).load(info_data.getPhoto()).fit().into(user_photo);
                }
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        });
        return row;

    }
    public void Refreshing(){
        this.notifyDataSetChanged();
    }
}