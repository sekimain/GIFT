package com.example.mypar.gift.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mypar.gift.Group.Group_Main_Activity;
import com.example.mypar.gift.R;
import com.example.mypar.gift.Structure.UserData;
import com.example.mypar.gift.server.Group_Delete_Request;
import com.example.mypar.gift.server.Logout_Request;
import com.example.mypar.gift.server.User_Delete_Request;

import org.json.JSONArray;
import org.json.JSONObject;

public class Account_Activity extends AppCompatActivity {

    UserData user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_);

        Intent intent = getIntent();
        user= intent.getParcelableExtra("userData");

        TextView Logout = findViewById(R.id.textView9);
        ImageView ImgLogout = findViewById(R.id.Logout);

        TextView Delete = findViewById(R.id.textView10);
        ImageView ImgDelete = findViewById(R.id.Delete);

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Account_Logout();
            }
        });

        ImgLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Account_Logout();
            }
        });

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Account_Delete();
            }
        });

        ImgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Account_Delete();
            }
        });
    }

    private void Account_Logout(){
        AlertDialog.Builder alert = new AlertDialog.Builder(Account_Activity.this);
        alert.setTitle("Do you really want to logout this account?");
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor autologin = auto.edit();
                autologin.putString("inputID","");
                autologin.putString("inputPass","");
                autologin.putBoolean("inputCheck",false);
                autologin.apply();

                setResult(5);
                finish();
            }
        });
        alert.setNegativeButton("Cancel", null);
        alert.show();
    }

    private void Account_Delete() {
        final int user_delete = user.getUsercode();

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        SharedPreferences.Editor autologin = auto.edit();
        autologin.putString("inputID", null);
        autologin.putString("inputPass", null);
        autologin.putBoolean("inputCheck",false);
        autologin.apply();

        AlertDialog.Builder alert = new AlertDialog.Builder(Account_Activity.this);
        alert.setTitle("Do you really want to delete this account?");
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Response.Listener<String> responseListenera = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if(success)
                            {
                                setResult(5);
                                finish();
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                };
                User_Delete_Request deleteRequest= new User_Delete_Request(user_delete,responseListenera);
                RequestQueue queue2 = Volley.newRequestQueue(Account_Activity.this);
                queue2.add(deleteRequest);
            }
        });
        alert.setNegativeButton("Cancel", null);
        alert.show();

    }
    protected void onDestroy()
    {
        super.onDestroy();

    }
}
