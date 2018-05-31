package com.example.mypar.gift.server;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Image_Send_Request extends StringRequest {
    final static private String URL = "http://ekrcy505.cafe24.com/GroupUrl.php";
    private Map<String,String> parameters;

    public Image_Send_Request(String groupCode, String url,Response.Listener<String> listener)
    {
        super(Method.POST, URL, listener,null);
        parameters = new HashMap<>();

        parameters.put("groupCode",groupCode);
        parameters.put("url",url);
    }

    @Override
    public Map<String,String> getParams()
    {
        return parameters;
    }
}