package com.example.mypar.gift.server;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Logout_Request extends StringRequest {
    final static private String URL = "http://ekrcy505.cafe24.com/GiftLogout.php";
    private Map<String,String> parameters;

    public Logout_Request(int userCode,Response.Listener<String> listener)
    {
        super(Request.Method.POST, URL, listener,null);
        parameters = new HashMap<>();

        parameters.put("userCode",""+userCode);
    }

    @Override
    public Map<String,String> getParams()
    {
        return parameters;
    }
}