package com.example.mypar.gift.Structure;


import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

public class Thread_Main implements Serializable {
    private String Title;
    private String Article;
    private int userCode;
    private int groupCode;
    private String Date;
    private int articleCode;
    private String File;
    private int fileflag;
    public Thread_Main(int articlecode, String title, String article, int usercode, int groupcode, String date, String file, int fileflag){
        Title = title;
        Article = article;
        userCode = usercode;
        groupCode = groupcode;
        Date = date;
        articleCode = articlecode;
        File = file;
        this.fileflag = fileflag;
    }

    public String getTitle(){return Title;}
    public String getArticle(){return Article;}
    public int getuserCode(){return userCode;}
    public int getgroupCode(){return groupCode;}
    public String getDate(){return Date;}
    public int getarticleCode(){return articleCode;}
    public String getFile(){return File;}
    public int getFileFlag(){return fileflag;}
}