package com.example.mypar.gift.server;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class User_Delete_Request extends StringRequest {

    final static private String URL = "http://ekrcy505.cafe24.com/GiftUserDelete.php";
    private Map<String,String> parameter;

    public User_Delete_Request(int userCode,Response.Listener<String> listener)
    {
        super(Request.Method.POST,URL,listener,null);
        parameter = new HashMap<>();
        parameter.put("userCode",""+userCode);
    }

    @Override
    public Map<String,String> getParams(){
        return parameter;
    }
}