package com.example.mypar.gift.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mypar.gift.Adapter.main_group_adapter;
import com.example.mypar.gift.Function.LoadGroupImage;
import com.example.mypar.gift.Function.LoadUserImage;
import com.example.mypar.gift.Function.TestService;
import com.example.mypar.gift.Group.Group_Create_Activity;
import com.example.mypar.gift.Group.Group_Main_Activity;
import com.example.mypar.gift.Group.Group_Search_Activity;
import com.example.mypar.gift.Group.User_Main_Activity;
import com.example.mypar.gift.R;
import com.example.mypar.gift.Retrofit.APIService;
import com.example.mypar.gift.Service.Common;
import com.example.mypar.gift.Service.TokenRequest;
import com.example.mypar.gift.Structure.Main_Group_List;
import com.example.mypar.gift.Structure.UserData;
import com.example.mypar.gift.server.Logout_Request;
import com.example.mypar.gift.server.Main_Request;
import com.example.mypar.gift.server.My_Group_Request;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private SwipeRefreshLayout srl;
    private main_group_adapter adapter;
    private ArrayList<Main_Group_List> listDataArray = new ArrayList<>();
    private UserData user;
    private String userID, UserPassword;
    private ListView listview;
    private TextView empty_text;
    private int userCode;
    @SuppressLint("UseSparseArrays")
    private Map<Integer, String> urlMap_user = new HashMap<Integer, String>();
    @SuppressLint("UseSparseArrays")
    private Map<Integer, String> urlMap_group = new HashMap<Integer, String>();


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this,TestService.class));

        // ------------------------------- Get User Data from server -------------------------- //
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        UserPassword = intent.getStringExtra("userPassword");

        // --------------------------- 유저 데이터 로드 ------------------------- //
        LoadUserImage loaduserimage = new LoadUserImage(this);
        urlMap_user = loaduserimage.getUrlMap();
        user_Call();

        //--------- Attach Adapter -----------//
        listview = findViewById(R.id.main_list_view);
        empty_text = findViewById(R.id.main_group_empty_message);

        listview.setEmptyView(empty_text);
        // ---------------------------- 새 그룹 생성 ---------------------------- //
        Button create_new_group = findViewById(R.id.create_new_group);
        create_new_group.setOnClickListener(this);

        // ---------------------------- 그룹 탐색 ------------------------------//
        final ImageView group_search = findViewById(R.id.group_search_btn);

        group_search.setClickable(true);
        group_search.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                    Picasso.with(MainActivity.this).load(R.drawable.icon_group_search_clicked).fit().into(group_search);
                } else if (action == MotionEvent.ACTION_UP) {
                    Picasso.with(MainActivity.this).load(R.drawable.icon_group_search).fit().into(group_search);
                } else if (action == MotionEvent.ACTION_MOVE) {
                }
                return false;
            }
        });
        group_search.setOnClickListener(this);

        // ---------------------------- 옵션 -----------------------------------//
        final ImageView Option_btn = findViewById(R.id.option_btn);
        Option_btn.setClickable(true);
        Option_btn.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                    Picasso.with(MainActivity.this).load(R.drawable.icon_option_clicked).fit().into(Option_btn);
                } else if (action == MotionEvent.ACTION_UP) {
                    Picasso.with(MainActivity.this).load(R.drawable.icon_option).fit().into(Option_btn);
                } else if (action == MotionEvent.ACTION_MOVE) {
                }
                return false;
            }
        });
        Option_btn.setOnClickListener(this);

        //----------------- Swipe Refreshing Layout --------------//
        srl = findViewById(R.id.main_swipe_refresh);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Swipe_Refreshing();
            }
        });

        //----------------- List View Click Listener ---------------------//
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView_Click_Action(position);
            }
        });
    }

    // -------------------------------- Activity Result ---------------------------- //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        List_Call();
        switch (resultCode) {
            case 0: // SEARCH_RESULT
                break;
            case 1: // CREATE_RESULT
                break;
            case 2: // PROFILE_CHANGE
                urlMap_user.clear();
                LoadUserImage loaduserimage = new LoadUserImage(this);
                urlMap_user = loaduserimage.getUrlMap();
                user_Call();
                break;
            case 3: // GROUP_ADMIN
                break;
            case 4: // GROUP_MEMBER
                break;
            case 5: // DELETE_ACCOUNT or LOGOUT
                Intent intent_Login = new Intent(getApplicationContext(), Login_Activity.class);
                startActivity(intent_Login);
                finish();
                break;
        }
    }

    // -------------------------------- Button Click Listener ---------------------- //
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.group_search_btn:
                Intent intent_group_search = new Intent(getApplicationContext(), Group_Search_Activity.class);
                intent_group_search.putExtra("userCode", user.getUsercode());
                startActivityForResult(intent_group_search, 0);
                break;
            case R.id.create_new_group:
                Intent intent_create_new_group = new Intent(getApplicationContext(), Group_Create_Activity.class);
                intent_create_new_group.putExtra("userCode", user.getUsercode());
                startActivityForResult(intent_create_new_group, 1);
                break;
            case R.id.option_btn:
                Intent intent_option = new Intent(getApplicationContext(), Option_Activity.class);
                intent_option.putExtra("userData", user);
                intent_option.putExtra("userPassword", UserPassword);
                startActivityForResult(intent_option, 2);
                break;
        }
    }

    public void Swipe_Refreshing() {
        List_Call();
        adapter = new main_group_adapter(MainActivity.this, R.layout.layout_main_group_list,userCode, listDataArray);
        adapter.notifyDataSetChanged();
        srl.setRefreshing(false);
    }

    // -------------------------------- 리스트 뷰 리스너 ------------------------------- //
    public void ListView_Click_Action(int position) {

        Main_Group_List group_data = listDataArray.get(position);
        if (group_data.getGroup_Admin() == user.getUsercode()) {
            Intent intent = new Intent(getApplicationContext(), Group_Main_Activity.class);
            intent.putExtra("Group_Name", group_data);
            intent.putExtra("User_Code", user);
            startActivityForResult(intent, 3);
        } else {
            Intent intent = new Intent(getApplicationContext(), User_Main_Activity.class);
            intent.putExtra("Group_Name", group_data);
            intent.putExtra("usercode", user);
            startActivityForResult(intent, 4);
        }
    }

    public void user_Call() {

        Response.Listener<String> responseListener_user = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    String getjsonEmail, getjsonID, getjsonPassword, getjsonName, getjsonCountry, getjsonPhone;
                    int getjsonCode;
                    int count = 0;
                        JSONObject object = jsonArray.getJSONObject(count);
                        getjsonEmail = object.getString("userEmail");
                        getjsonPassword = object.getString("userPassword");
                        getjsonName = object.getString("userName");
                        getjsonID = object.getString("userID");
                        getjsonCountry = object.getString("userCountry");
                        getjsonPhone = object.getString("userPhone");
                        userCode = Integer.parseInt(object.getString("userCode"));

                        user = new UserData(getjsonEmail, getjsonName, getjsonPhone, getjsonID, getjsonCountry, urlMap_user.get(userCode), userCode);
                        count++;
                    getToken();
                    List_Call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Main_Request mainRequest = new Main_Request(userID, responseListener_user);
        RequestQueue queue_user = Volley.newRequestQueue(MainActivity.this);
        queue_user.add(mainRequest);

    }
    /////////////////////push token받아오기///////////////////
    APIService mService;
    public void getToken()
    {
        Common.currentToken = FirebaseInstanceId.getInstance().getToken();
        mService = Common.getFCMClient();
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

        TokenRequest tokenRequest = new TokenRequest(user.getUsercode(),Common.currentToken,responseListener_user);
        RequestQueue queue_user = Volley.newRequestQueue(MainActivity.this);
        queue_user.add(tokenRequest);


    }


    ////////////////////////////////////////////////////


    // ---------------------------- 그룹 리스트 표시 --------------------------- //
    public void List_Call() {
        listDataArray.clear();
        urlMap_group.clear();
        // ------------------------- get Group photo data from server ------------------------- //
        LoadGroupImage loadgroupimage = new LoadGroupImage(this);
        urlMap_group = loadgroupimage.getUrlMap();

        // ------------------------ Get Group Data from Server ------------------------ //
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    String getjsonGName,getjsonIntro,getjsonProfessor,getjsonEmail,getjsonDate, getjsonCollege, getjsonMajor;
                    int getjsonAdmin,getjsonCode;

                    int count =0;
                        while(count<jsonArray.length())
                        {
                        JSONObject object = jsonArray.getJSONObject(count);
                        getjsonGName = object.getString("groupName");
                        getjsonIntro= object.getString("groupIntro");
                        getjsonProfessor = object.getString("groupProfessor");
                        getjsonEmail = object.getString("groupEmail");
                        getjsonDate = object.getString("groupDate");
                        getjsonCode = Integer.parseInt(object.getString("groupCode"));
                        getjsonAdmin = Integer.parseInt(object.getString("groupAdmin"));
                        getjsonCollege = object.getString("groupCollege");
                        getjsonMajor = object.getString("groupMajor");

                        Main_Group_List group = new Main_Group_List(getjsonGName,getjsonIntro,getjsonProfessor,getjsonEmail,getjsonDate,urlMap_group.get(getjsonCode),getjsonCollege, getjsonMajor, getjsonCode,getjsonAdmin);

                        adapter = new main_group_adapter(MainActivity.this, R.layout.layout_main_group_list,userCode, listDataArray);


                        listDataArray.add(group);
                        count++;
                    }

                    listview.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        My_Group_Request mygroupRequest = new My_Group_Request(user.getUsercode(), responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(mygroupRequest);

    }

    protected void onDestroy() {
        super.onDestroy();

        Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_SHORT).show();
        try {
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("response");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            Logout_Request logoutRequest = new Logout_Request(user.getUsercode(), responseListener);
            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
            queue.add(logoutRequest);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
