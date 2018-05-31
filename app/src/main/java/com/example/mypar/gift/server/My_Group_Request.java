package com.example.mypar.gift.server;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class My_Group_Request extends StringRequest {
    final static private String URL = "http://ekrcy505.cafe24.com/GiftMygroup.php";
    private Map<String,String> parameters;

    public My_Group_Request(int userID, Response.Listener<String> listener)
    {
        super(Method.POST, URL, listener,null);
        parameters = new HashMap<>();

        parameters.put("userID",""+userID);
    }

    @Override
    public Map<String,String> getParams()
    {
        return parameters;
    }
}