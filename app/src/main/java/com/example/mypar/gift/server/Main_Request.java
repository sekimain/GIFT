package com.example.mypar.gift.server;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Main_Request extends StringRequest {
    final static private String URL = "http://ekrcy505.cafe24.com/GiftSearch.php";
    private Map<String,String> parameters;

    public Main_Request(String userID, Response.Listener<String> listener)
    {
        super(Method.POST, URL, listener,null);
        parameters = new HashMap<>();

        parameters.put("userEmail",userID);
    }

    @Override
    public Map<String,String> getParams()
    {
        return parameters;
    }
}