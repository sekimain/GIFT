package com.example.mypar.gift.Group;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mypar.gift.Adapter.group_join_adapter;
import com.example.mypar.gift.Function.LoadUserImage;
import com.example.mypar.gift.R;
import com.example.mypar.gift.Structure.Group_Member_Data;
import com.example.mypar.gift.Structure.Main_Group_List;
import com.example.mypar.gift.server.Group_Join_Member_Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Group_Join_Management_Activity extends AppCompatActivity {

    private ArrayList<Group_Member_Data> listdataArray;
    private SwipeRefreshLayout srl;
    private group_join_adapter adapter;
    private ListView listView;
    private Main_Group_List group_data;
    private Map<Integer, String> urlMap = new HashMap<Integer, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group__join__management_);
        listdataArray = new ArrayList<>();
        listView = findViewById(R.id.group_join_list_view);
        srl = findViewById(R.id.group_join_swipe_refresh);

        Intent intent = getIntent();
        group_data = intent.getParcelableExtra("Group_data");
        final int group_code = group_data.getGroup_Code();
        adapter = new group_join_adapter(this, R.layout.layout_grouo_join_list, listdataArray, group_code);

        Join_List_Call(group_code);

        //----------------- Swipe Refreshing Layout --------------//
        srl = findViewById(R.id.group_join_swipe_refresh);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Swipe_Refreshing(group_code);
            }
        });
    }
    public void Swipe_Refreshing(int code){
        Join_List_Call(code);
        adapter.notifyDataSetChanged();
        srl.setRefreshing(false);
    }
    public void Join_List_Call(int group_code){
        listdataArray.clear();
        urlMap.clear();

        LoadUserImage loaduserimage = new LoadUserImage(Group_Join_Management_Activity.this);
        urlMap = loaduserimage.getUrlMap();

        // ------------------------------ Read Member Data ----------------------------------- //

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    String getjsonGName,getjsonID,getjsonCountry,getjsonEmail,getjsonPhone;
                    int getjsonCode;

                    int count =0;
                    while(count<jsonArray.length())
                    {
                        JSONObject object = jsonArray.getJSONObject(count);
                        getjsonEmail = object.getString("userEmail");
                        getjsonGName = object.getString("userName");
                        getjsonID = object.getString("userID");
                        getjsonCountry = object.getString("userCountry");
                        getjsonPhone = object.getString("userPhone");
                        getjsonCode = object.getInt("userCode");

                        Group_Member_Data group = new Group_Member_Data(getjsonGName,getjsonEmail,getjsonPhone,getjsonID,getjsonCountry,urlMap.get(getjsonCode),getjsonCode);

                        listdataArray.add(group);
                        count++;
                    }

                    listView.setAdapter(adapter);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        Group_Join_Member_Request mygroupRequest = new Group_Join_Member_Request(group_code,responseListener);
        RequestQueue queue = Volley.newRequestQueue(Group_Join_Management_Activity.this);
        queue.add(mygroupRequest);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(7);
    }
}
