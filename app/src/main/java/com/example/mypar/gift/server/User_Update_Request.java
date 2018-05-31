package com.example.mypar.gift.server;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class User_Update_Request extends StringRequest {
    final static private String URL = "http://ekrcy505.cafe24.com/GiftUserUpdate.php";
    private Map<String,String> parameters;

    public User_Update_Request(int userCode,String userName,String userID,String userPhone,String userCountry, Response.Listener<String> listener)
    {
        super(Method.POST, URL, listener,null);
        parameters = new HashMap<>();
        parameters.put("userName",userName);
        parameters.put("userID",userID);
        parameters.put("userPhone",userPhone);
        parameters.put("userCountry",userCountry);
        parameters.put("userCode",""+userCode);
    }

    @Override
    public Map<String,String> getParams()
    {
        return parameters;
    }
}