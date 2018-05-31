package com.example.mypar.gift.server;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by YGLab on 2018-04-04.
 */
public class CommentRequest extends StringRequest {

    final static private String URL = "http://rshak8912.cafe24.com/comment/searchComment.php";
    private Map<String,String> parameters;

    public CommentRequest(Response.Listener<String> listener)
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
