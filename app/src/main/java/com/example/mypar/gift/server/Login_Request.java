package com.example.mypar.gift.server;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class Login_Request extends StringRequest {
    final static private String URL = "http://ekrcy505.cafe24.com/GiftLogin.php";
    private Map<String,String> parameters;

    public Login_Request(String userID, String userPassword,Response.Listener<String> listener)
    {
        super(Method.POST, URL, listener,null);
        parameters = new HashMap<>();

        parameters.put("userEmail",userID);
        parameters.put("userPassword",userPassword);
    }

    @Override
    public Map<String,String> getParams()
    {
        return parameters;
    }
}