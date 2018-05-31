package com.example.mypar.gift.Group;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mypar.gift.R;
import com.example.mypar.gift.Structure.Main_Group_List;
import com.squareup.picasso.Picasso;

public class User_Group_Info extends AppCompatActivity {

    private Main_Group_List group_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__group__info);

        Intent intent = getIntent();
        group_data = intent.getParcelableExtra("Group_Name");

        TextView name, intro, professor, email;
        ImageView photo;

        name = findViewById(R.id.user_info__name);
        intro = findViewById(R.id.user_info__Intro);
        professor = findViewById(R.id.user_info_professor);
        email = findViewById(R.id.user_info_email);
        photo = findViewById(R.id.user_info_photo);

        name.setText(group_data.getName());
        intro.setText(group_data.getIntro());
        professor.setText(group_data.getProffesor());
        email.setText(group_data.getEmail());

        if((group_data.getPhoto()==null) || (group_data.getPhoto().equals("empty"))){
            Picasso.with(User_Group_Info.this).load(R.drawable.main_group_default_photo).fit().into(photo);
        }else {
            Picasso.with(User_Group_Info.this).load(group_data.getPhoto()).fit().into(photo);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(6);
    }
}
