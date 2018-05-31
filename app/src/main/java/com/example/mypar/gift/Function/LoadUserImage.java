package com.example.mypar.gift.Function;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mypar.gift.Activity.MainActivity;
import com.example.mypar.gift.server.Image_User_Receive_Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoadUserImage {
    private Map<Integer, String> urlMap = new HashMap<Integer, String>();
    private Context row;

    public LoadUserImage(Context context){
        this.row = context;
    }

    public Map<Integer, String> getUrlMap(){
        Response.Listener<String> responseListener_url = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");

                    String getjsonurl;
                    int getjsonCode;
                    int count1 = 0;
                    while(count1<jsonArray.length())
                    {
                        JSONObject object = jsonArray.getJSONObject(count1);
                        getjsonCode = Integer.parseInt(object.getString("userCode"));

                        getjsonurl = object.getString("url");
                        urlMap.put(getjsonCode, getjsonurl);
                        count1++;
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        Image_User_Receive_Request imgurlRequest = new Image_User_Receive_Request(responseListener_url);
        RequestQueue queue_url = Volley.newRequestQueue(this.row);
        queue_url.add(imgurlRequest);

        return urlMap;
    }
}
