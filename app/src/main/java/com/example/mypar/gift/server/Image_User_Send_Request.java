package com.example.mypar.gift.server;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Image_User_Send_Request extends StringRequest {
    final static private String URL = "http://ekrcy505.cafe24.com/UserUrl.php";
    private Map<String,String> parameters;

    public Image_User_Send_Request(String userCode, String url,Response.Listener<String> listener)
    {
        super(Method.POST, URL, listener,null);
        parameters = new HashMap<>();

        parameters.put("userCode",userCode);
        parameters.put("url",url);
    }

    @Override
    public Map<String,String> getParams()
    {
        return parameters;
    }
}