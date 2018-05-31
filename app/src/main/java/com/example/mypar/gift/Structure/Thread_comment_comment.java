package com.example.mypar.gift.Structure;

public class Thread_comment_comment{
    private String commnet_comment_name, comment_comment_time, comment_comment_text;


    public Thread_comment_comment(String name, String time, String id){
        this.commnet_comment_name = name;
        this.comment_comment_time = time;
        this.comment_comment_text = id;
    }

    public String getName(){ return this.commnet_comment_name; }
    public String getTime(){ return this.comment_comment_time; }
    public String getText(){
        return this.comment_comment_text;
    }
}
