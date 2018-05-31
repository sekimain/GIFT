package com.example.mypar.gift.server;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by watoz on 2018-05-18.
 */

public class Article_Request2 extends StringRequest {

    final static private String URL = "http://rshak8912.cafe24.com/boardlist2.php";
    private Map<String,String> parameters;

    public Article_Request2( Response.Listener<String> listener)
    {
        super(Request.Method.POST, URL, listener,null);
        parameters = new HashMap<>();
    }

    @Override
    public Map<String,String> getParams()
    {
        return parameters;
    }
}
