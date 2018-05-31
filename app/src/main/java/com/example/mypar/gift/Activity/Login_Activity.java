package com.example.mypar.gift.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mypar.gift.Function.TestService;
import com.example.mypar.gift.R;
import com.example.mypar.gift.server.Login_Request;

import org.json.JSONObject;

public class Login_Activity extends AppCompatActivity {

    private CheckBox login_check;
    private EditText login_email, login_password ;
    private Boolean auto_check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


                login_check = findViewById(R.id.auto_checkBox);
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        startService(new Intent(this,TestService.class));

        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);

        Button login_btn = findViewById(R.id.login_btn);
        Button sign_btn = findViewById(R.id.sign_btn);

        String auto_email, auto_password;
        auto_email = auto.getString("inputID", "");
        auto_password = auto.getString("inputPass", "");
        auto_check = auto.getBoolean("inputCheck", false);

        if(auto_check){
            login_check.setChecked(true);
        } else {login_check.setChecked(false);}

        if((!auto_email.equals("")) && (!login_password.equals(""))){
            login_email.setText(auto_email);
            login_password.setText(auto_password);
            startlogin();
        }

        login_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startlogin();
            }
        });

        sign_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), Register_Activity.class);
                startActivity(intent);
            }
        });
    }

    public void startlogin(){
        String userID = login_email.getText().toString();
        String userPassword = login_password.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    int success = jsonResponse.getInt("success");

                    String userID = jsonResponse.getString("userEmail");

                    if(success == 2)
                    {
                        if(login_check.isChecked()){
                            SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor autologin = auto.edit();
                            autologin.putBoolean("inputCheck", true);
                            autologin.putString("inputID", login_email.getText().toString());
                            autologin.putString("inputPass", login_password.getText().toString());
                            autologin.apply();

                        }

                        Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                        intent.putExtra("userID", userID);
                        Login_Activity.this.startActivity(intent);
                        finish();
                    }
                    else if(success == 1){
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login_Activity.this);
                        builder.setMessage("Currently loged in account")
                                .setNegativeButton("cancel",null)
                                .create()
                                .show();
                    }
                    else if(success == 0)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login_Activity.this);
                        builder.setMessage("There is no ID or wrong password.")
                                .setNegativeButton("cancel",null)
                                .create()
                                .show();
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        Login_Request loginRequest= new Login_Request(userID,userPassword,responseListener);
        RequestQueue queue = Volley.newRequestQueue(Login_Activity.this);
        queue.add(loginRequest);
    }
}
