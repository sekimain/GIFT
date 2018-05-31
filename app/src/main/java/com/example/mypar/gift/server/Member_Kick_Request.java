package com.example.mypar.gift.server;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Member_Kick_Request extends StringRequest {

    final static private String URL = "http://ekrcy505.cafe24.com/GiftDelete.php";
    private Map<String,String> parameters;

    public  Member_Kick_Request(int userCode,int groupCode, Response.Listener<String>listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userCode",""+ userCode);
        parameters.put("groupCode",""+groupCode);
    }

    @Override
    public Map<String,String> getParams(){
        return parameters;
    }
}