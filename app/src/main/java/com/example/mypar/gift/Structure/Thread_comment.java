package com.example.mypar.gift.Structure;

import java.util.ArrayList;

/**
 * Created by mypar on 2018-03-01.
 */

public class Thread_comment{
    private String comment_name, comment_time, comment_text;
    private ArrayList<Thread_comment_comment> arraylist = new ArrayList<>();


    public Thread_comment(String name, String time, String text,  ArrayList<Thread_comment_comment> list){
        this.comment_name = name;
        this.comment_text = text;
        this.comment_time = time;
        this.arraylist = list;
    }
    public String getName(){ return this.comment_name; }
    public String getTime(){ return this.comment_time; }
    public String getText(){
        return this.comment_text;
    }
    public ArrayList<Thread_comment_comment> getArrayList() { return this.arraylist; }
}
