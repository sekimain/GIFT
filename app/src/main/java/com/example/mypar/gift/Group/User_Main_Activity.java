package com.example.mypar.gift.Group;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mypar.gift.Adapter.group_thread_adapter;
import com.example.mypar.gift.R;
import com.example.mypar.gift.Service.Constants;
import com.example.mypar.gift.Structure.Main_Group_List;
import com.example.mypar.gift.Structure.Pdf;
import com.example.mypar.gift.Structure.Thread_Main;
import com.example.mypar.gift.Structure.Thread_comment;
import com.example.mypar.gift.Structure.UserData;
import com.example.mypar.gift.server.Article_User_Request;
import com.example.mypar.gift.server.Group_secession_Request;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class User_Main_Activity extends AppCompatActivity {

    private Toolbar toolbar;
    private SwipeRefreshLayout srl;
    private group_thread_adapter adapter;
    private ArrayList<Thread_Main> listDataArray = new ArrayList<>();
    private ListView listView;
    private ImageView setting_btn;
    private Thread_Main articleinfo;
    private Main_Group_List group_data;
    private UserData userData;
    private ImageView logout_btn;
    //database reference to get uploads data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__main_);

        Intent intent = getIntent();
        group_data = intent.getParcelableExtra("Group_Name");
        userData = intent.getParcelableExtra("usercode");
        toolbar = findViewById(R.id.User_main_toolbar);
        toolbar.setTitle(group_data.getName());
        logout_btn = findViewById(R.id.LogoutButton);
        // group information button //
        setting_btn = findViewById(R.id.user_main_option);
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting = new Intent(getApplicationContext(), User_Group_Info.class);

                setting.putExtra("Group_Name", group_data);
                startActivity(setting);
            }
        });
        logout_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(User_Main_Activity.this);
                builder.setMessage("Do you want leave this group?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logout_btn.setVisibility(View.INVISIBLE);

                                Response.Listener<String> responseListener_push = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Response.Listener<String> responseListener_user = new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        };
                                    }
                                };
                                Group_secession_Request article_request2 = new Group_secession_Request(userData.getUsercode(), group_data.getGroup_Code(), responseListener_push);
                                RequestQueue queue_push = Volley.newRequestQueue(User_Main_Activity.this);
                                queue_push.add(article_request2);
                                finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .create()
                        .show();


            }
        });
        listView = findViewById(R.id.User_thread_listView);
        listView.setFocusable(false);
        TextView empty_text = findViewById(R.id.group_thread_empty_message);
        listView.setEmptyView(empty_text);


        Thread_List_Call();


        //----------------- Swipe Refreshing Layout --------------//
        srl = findViewById(R.id.User_swipe_refresh);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Swipe_Refreshing();
            }
        });

        adapter = new group_thread_adapter(User_Main_Activity.this, R.layout.layout_group_thread, listDataArray, group_data.getGroup_Admin(), userData.getUsercode());

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), Group_Thread_Activity.class);
                intent.putExtra("object", listDataArray.get(position));
                intent.putExtra("UserData", userData);
                intent.putExtra("GroupData", group_data);
                startActivityForResult(intent, 6);
            }
        });

        FloatingActionButton fab = findViewById(R.id.Group_user_write);
        fab.setImageResource(R.drawable.icon_thread_write);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent posting_intent = new Intent(getApplicationContext(), Thread_Posting_Activity.class);
                posting_intent.putExtra("Group_Name", group_data);
                posting_intent.putExtra("User_Code", userData);

                startActivityForResult(posting_intent, 6);
            }
        });
    }

    // -------------------------------- Activity Result ---------------------------- //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Thread_List_Call();
        adapter.notifyDataSetChanged();
    }


    public void Thread_List_Call() {
        listDataArray.clear();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    String getjsonTitle;
                    String getjsonArticle;
                    String getjsonDate;
                    String getjsonFile;
                    String getjsonPath;
                    int getjsonGCode, getjsonCode, getjsonACode, getjsonfileflag;
                    int count = 0;


                    while (count < jsonArray.length()) {
                        JSONObject object = jsonArray.getJSONObject(count);
                        getjsonTitle = object.getString("title");
                        getjsonArticle = object.getString("article");
                        getjsonCode = Integer.parseInt(object.getString("userCode"));
                        getjsonGCode = Integer.parseInt(object.getString("groupCode"));
                        getjsonACode = Integer.parseInt(object.getString("articleCode"));
                        getjsonDate = object.getString("date");
                        getjsonDate = getjsonDate.replace(":00.000000", "");

                        getjsonFile = object.getString("imagename");
                        getjsonfileflag = Integer.parseInt(object.getString("fileFlag"));

                        count++;

                        articleinfo = new Thread_Main(getjsonACode, getjsonTitle, getjsonArticle, getjsonCode, getjsonGCode, getjsonDate, getjsonFile, getjsonfileflag);
                        if (articleinfo.getgroupCode() == group_data.getGroup_Code()) {
                            if (articleinfo.getuserCode() == group_data.getGroup_Admin()) {
                                listDataArray.add(articleinfo);
                            } else if (articleinfo.getuserCode() == userData.getUsercode()) {
                                listDataArray.add(articleinfo);
                            }
                        }
                    }
                    adapter = new group_thread_adapter(User_Main_Activity.this, R.layout.layout_group_thread, listDataArray, group_data.getGroup_Admin(), userData.getUsercode());
                    listView.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Article_User_Request ArticleRequest = new Article_User_Request(group_data.getGroup_Admin(), userData.getUsercode(), responseListener);
        RequestQueue queue = Volley.newRequestQueue(User_Main_Activity.this);
        queue.add(ArticleRequest);
    }

    public void Swipe_Refreshing() {
        adapter.notifyDataSetChanged();
        srl.setRefreshing(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(4);
    }
}
