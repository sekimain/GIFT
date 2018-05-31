package com.example.mypar.gift.Structure;

import java.io.Serializable;

/**
 * Created by YGLab on 2018-04-04.
 */

public class commentClass implements Serializable {

    private String Article;
    private String userCode;
    private int articleCode;
    private String Date;

    public commentClass(String article, String usercode,int articlecode, String date){

        Article = article;
        userCode = usercode;
        articleCode = articlecode;
        Date = date;
    }
    public String getArticle(){return Article;}
    public String getuserCode(){return userCode;}
    public int getarticleCode(){return articleCode;}
    public String getDate(){return Date;}
}
