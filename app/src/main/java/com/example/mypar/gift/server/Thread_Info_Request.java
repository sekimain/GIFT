package com.example.mypar.gift.server;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Thread_Info_Request {
    private URL url;

    public Thread_Info_Request(String url) throws MalformedURLException { this.url = new URL(url); }

    private String readStream(InputStream in) throws IOException {
        StringBuilder jsonHtml = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line = null;

        while((line = reader.readLine()) != null)
            jsonHtml.append(line);

        reader.close();
        return jsonHtml.toString();
    }


    // 글 thread 를 위한 php requset
    public String PhPtest(final String title, final String article, final String userCode, final String groupCode, final String date, final String imageName, final String imagePath, final int file_flag) {
        try {

            int userCodeInt, groupCodeInt;
            userCodeInt = Integer.parseInt(userCode);
            groupCodeInt = Integer.parseInt(groupCode);

            String postData = "title=" + title + "&" + "article=" + article + "&" + "userCode=" + userCodeInt + "&" + "groupCode=" + groupCodeInt + "&" + "date=" + date + "&" + "imagename=" + imageName + "&" + "imagepath=" + imagePath + "&" + "fileFlag=" + file_flag;
            Log.d("PHP", postData);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(postData.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
            String result = readStream(conn.getInputStream());

            conn.disconnect();


            Log.d("PHPresult", result);

            return result;
        }
        catch (Exception e) {
            return null;
        }
    }


    // 댓글을 위한 php request
    public String PhPtest2(final String article, final String userCode, final String articleCode, final String date) {
        try {

            String postData = "article=" + article + "&" + "userCode=" + userCode + "&" + "articleCode=" + articleCode + "&" + "date=" + date;

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(postData.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
            String result = readStream(conn.getInputStream());
            conn.disconnect();
            return result;
        }
        catch (Exception e) {

            return null;
        }
    }

    // img 다운을 할때 위한 php
    public String PhPtest3(final String filename) {
        try {

            String postData = "filename=" + filename;
            Log.d("img", ""+ postData);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(postData.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
            String result = readStream(conn.getInputStream());
            Log.d("img", "Request result : "+result);
            conn.disconnect();
            return result;
        }
        catch (Exception e) {

            return null;
        }
    }

    public String PhPtest4(final String articleCode) { // 글 삭제 PHP
        try {

            Log.d("delete", "Deleteing" + articleCode);
            String postData = "articleCode=" + articleCode;

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(postData.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
            String result = readStream(conn.getInputStream());
            Log.d("delete", "Request result : "+result);
            conn.disconnect();
            return result;
        }
        catch (Exception e) {
            Log.i("PHPRequest", "request was failed.");
            return null;
        }
    }

    public String PhPtest5() { // Article Code get
        try {

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //OutputStream outputStream = conn.getOutputStream();
            //outputStream.write(postData.getBytes("UTF-8"));
            //outputStream.flush();
            //outputStream.close();
            String result = readStream(conn.getInputStream());
            Log.d("article code", "Request result : "+result);
            conn.disconnect();
            return result;
        }
        catch (Exception e) {
            Log.i("PHPRequest", "request was failed.");
            return null;
        }
    }
}