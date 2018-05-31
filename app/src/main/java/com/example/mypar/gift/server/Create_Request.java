package com.example.mypar.gift.server;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Create_Request extends StringRequest {
    final static private String URL = "http://ekrcy505.cafe24.com/GiftCreate.php";
    private Map<String,String> parameters;

    public Create_Request(int userCode,String groupName,String groupIntro,String groupProfessor,String groupEmail,int groupAdmin,String GroupData,String groupCollege,String groupMajor,Response.Listener<String> listener)
    {
        super(Method.POST, URL, listener,null);
        parameters = new HashMap<>();
        parameters.put("userCode",""+userCode);
        parameters.put("groupName",groupName);
        parameters.put("groupIntro",groupIntro);
        parameters.put("groupProfessor",groupProfessor);
        parameters.put("groupEmail",groupEmail);
        parameters.put("groupAdmin",""+groupAdmin);
        parameters.put("groupDate",GroupData);
        parameters.put("groupCollege",groupCollege);
        parameters.put("groupMajor",groupMajor);
    }

    @Override
    public Map<String,String> getParams()
    {
        return parameters;
    }
}