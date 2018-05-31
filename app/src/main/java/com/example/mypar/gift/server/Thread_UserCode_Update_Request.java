package com.example.mypar.gift.server;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class Thread_UserCode_Update_Request extends StringRequest {
    final static private String URL = "http://rshak8912.cafe24.com/GiftThread1.php";
    private Map<String,String> parameters;

    public Thread_UserCode_Update_Request(int articleCode, int adminCode, Response.Listener<String> listener)
    {
        super(Request.Method.POST, URL, listener,null);
        parameters = new HashMap<>();

        parameters.put("articleCode",""+articleCode);
        parameters.put("adminCode",""+adminCode);
    }
    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
