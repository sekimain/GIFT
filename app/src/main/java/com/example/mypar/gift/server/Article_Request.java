package com.example.mypar.gift.server;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Article_Request extends StringRequest {

    final static private String URL = "http://rshak8912.cafe24.com/board/search.php";
    private Map<String,String> parameters;

    public Article_Request( Response.Listener<String> listener)
    {
        super(Method.POST, URL, listener,null);

        parameters = new HashMap<>();
    }

    @Override
    public Map<String,String> getParams()
    {
        return parameters;
    }
}