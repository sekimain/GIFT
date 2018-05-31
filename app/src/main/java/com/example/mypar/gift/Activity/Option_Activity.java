package com.example.mypar.gift.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.mypar.gift.R;
import com.example.mypar.gift.Structure.UserData;

public class Option_Activity extends AppCompatActivity implements View.OnClickListener{

    private UserData userData;
    private String userPassword;
    private FrameLayout faq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_);

        Intent intent = getIntent();
        userPassword = intent.getStringExtra("userPassword");
        userData = intent.getParcelableExtra("userData");

        ImageView button = findViewById(R.id.Pro);
        button.setOnClickListener(this);

        ImageView button2 = findViewById(R.id.Account);
        button2.setOnClickListener(this);

        faq = findViewById(R.id.setting_list_faq_btn);
        faq.setOnClickListener(this);
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Pro:
                Intent intent = new Intent(Option_Activity.this, MyProfile_Activity.class);
                intent.putExtra("userData", userData);
                intent.putExtra("userPassword", userPassword);
                startActivity(intent);
                break;
            case R.id.Account:
                Intent intent2 = new Intent(Option_Activity.this, Account_Activity.class);
                intent2.putExtra("userData", userData);
                intent2.putExtra("userPassword", userPassword);
                startActivityForResult(intent2,5);
                break;
            case R.id.setting_list_faq_btn:
                Intent intent3 = new Intent(Option_Activity.this, FAQ_Activity.class);
                startActivity(intent3);
                break;
        }
    }

    @Override
    protected void onDestroy(){

        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (resultCode){
            case 5: // DELETE_ACCOUNT
                setResult(5);
                finish();
                break;
            case 1: // PROFILE_CHANGE
                setResult(2);
                break;
            default:
                break;
        }
    }
}
