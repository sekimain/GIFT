package com.example.mypar.gift.server;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mypar on 2018-03-22.
 */

public class Group_Image_Update_Request extends StringRequest {
    final static private String URL = "http://ekrcy505.cafe24.com/ImageUpdate.php";
    private Map<String,String> parameters;

    public Group_Image_Update_Request(String groupCode, String url,Response.Listener<String> listener)
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