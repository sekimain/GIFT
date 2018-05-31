package com.example.mypar.gift.Group;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mypar.gift.Activity.Pdf_Activity;
import com.example.mypar.gift.Adapter.ComListviewAdapter;
import com.example.mypar.gift.Function.NetworkUtil;
import com.example.mypar.gift.Model.MYResponse;
import com.example.mypar.gift.Model.Notification;
import com.example.mypar.gift.Model.Sender;
import com.example.mypar.gift.R;
import com.example.mypar.gift.Retrofit.APIService;
import com.example.mypar.gift.Service.Common;
import com.example.mypar.gift.Service.Constants;
import com.example.mypar.gift.Structure.Main_Group_List;
import com.example.mypar.gift.Structure.Pdf;
import com.example.mypar.gift.Structure.Thread_Main;
import com.example.mypar.gift.Structure.UserData;
import com.example.mypar.gift.Structure.commentClass;
import com.example.mypar.gift.server.CommentRequest;
import com.example.mypar.gift.server.Thread_Info_Request;
import com.example.mypar.gift.server.Thread_Push_Request;
import com.example.mypar.gift.server.Thread_UserCode_Update_Request;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class Group_Thread_Activity extends AppCompatActivity {

    private Thread_Main article;

    private String title, body, date, file;
    private int owner, code, articleCode;
    private  ComListviewAdapter adapter;
    private TextView Title, Body, Owner, Date, File_count_txt;
    private EditText text;
    private ImageView btn;
    private UserData userData;
    private Main_Group_List group_data;
    private ListView view;
    private ImageView ImgView, button_accept;
    private ImageView button_fileview;
    private commentClass com;
    private ArrayList comArray = new ArrayList<commentClass>();
    private SwipeRefreshLayout srl;
    private int filecount = 0;
    private List<Pdf> pdfList;
    //database reference to get uploads data
    private DatabaseReference mDatabaseReference;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group__thread);
        NetworkUtil.setNetworkPolicy();

        Intent intent = getIntent();
        userData = intent.getParcelableExtra("UserData");
        group_data = intent.getParcelableExtra("GroupData");
        article = (Thread_Main) getIntent().getSerializableExtra("object");

        articleCode = article.getarticleCode(); // 글 코드

        code = article.getarticleCode();
        title = article.getTitle();
        body = article.getArticle();
        owner = article.getuserCode();
        date = article.getDate();
        file = article.getFile();

        Title = findViewById(R.id.Title);
        Title.setText(title);
        Title.setFocusableInTouchMode(true);
        Title.requestFocus();

        Body = findViewById(R.id.Body);
        Body.setText(body);

        Date = findViewById(R.id.Date);

        Date.setText( date.replace(".000000", ""));
        /////////////////////////////////////////////////////////////
        text = findViewById(R.id.comEdit);
        btn = findViewById(R.id.comBtn);
        button_accept = findViewById(R.id.thread_button_accept);
        button_accept.setClickable(false);
        srl = findViewById(R.id.thread_swipe_refresh);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Swipe_Refreshing();
            }
        });

        if((userData.getUsercode() != article.getuserCode())&&(userData.getUsercode()==group_data.getGroup_Admin())){
            button_accept.setVisibility(View.VISIBLE);
            button_accept.setClickable(true);
            button_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Group_Thread_Activity.this);
                    builder.setMessage("Do you want to open this thread?")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    button_accept.setVisibility(View.GONE);
                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonResponse = new JSONObject(response);
                                                boolean success = jsonResponse.getBoolean("success");


                                                if (success) {
                                                    Push(group_data);
                                                } else {

                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    };
                                    Thread_UserCode_Update_Request updateRequest = new Thread_UserCode_Update_Request(article.getarticleCode(),group_data.getGroup_Admin(), responseListener);
                                    RequestQueue queue = Volley.newRequestQueue(Group_Thread_Activity.this);
                                    queue.add(updateRequest);
                                }
                            })
                            .setNegativeButton("No",null)
                            .create()
                            .show();
                }
            });
        }
        else{
            button_accept.setVisibility(View.GONE);
        }

        //getting the database reference
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS).child(""+code);

        pdfList = new ArrayList<>();
        //retrieving upload data from firebase database
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Pdf pdf = postSnapshot.getValue(Pdf.class);
                    pdfList.add(pdf);
                }

                final String[] pdfs = new String[pdfList.size()];

                for (int i = 0; i < pdfs.length; i++) {
                    filecount=i+1;
                }

                if(filecount!=0){
                    button_fileview = findViewById(R.id.file_btn);
                    button_fileview.setVisibility(View.VISIBLE);
                    button_fileview.setClickable(true);
                    File_count_txt = findViewById(R.id.file_count);
                    File_count_txt.setVisibility(View.VISIBLE);
                    File_count_txt.setClickable(true);
                    File_count_txt.setText(filecount+" attached file");

                    button_fileview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Group_Thread_Activity.this, Pdf_Activity.class);
                            intent.putExtra("articleCode",articleCode);
                            startActivity(intent);

                        }
                    });
                   File_count_txt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Group_Thread_Activity.this, Pdf_Activity.class);
                            intent.putExtra("articleCode",articleCode);
                            startActivity(intent);

                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        view = findViewById(R.id.listview);

        ImgView = findViewById(R.id.ArticleImg);

        if(!(file.equals(""))){ // 글에 저장된 파일정보가 존재할경우
            ImgView.setVisibility(View.VISIBLE);
            try {
                Thread_Info_Request request = new Thread_Info_Request("http://rshak8912.cafe24.com/image/imagedownload.php");
                Log.d("img", "After Request" + title);
                String result = request.PhPtest3(String.valueOf(String.valueOf(articleCode)));

                Log.d("img", result);

                if (result.equals("fail")) { // 실패
                    Toast.makeText(getApplication(), "Image download fail.", Toast.LENGTH_SHORT).show();
                } else { // base64 형식 이미지 가져옴

                    byte[] decodedString = Base64.decode(result, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    ImgView.setImageBitmap(decodedByte);
                }
                InputMethodManager mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                mInputMethodManager.hideSoftInputFromWindow(text.getWindowToken(), 0);
                text.setText("");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        Comment_List_Call();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(text.getText().toString().replace(" ", "").equals("") // no usercode , groupcode now so temp
                            /*|| usercode.getText().toString().replace(" ", "").equals("") || groupcode.getText().toString().replace(" ", "").equals("")*/)
                    Toast.makeText(getApplication(), "Fill in the blank", Toast.LENGTH_SHORT).show();
                else {
                    try {

                        Thread_Info_Request request = new Thread_Info_Request("http://rshak8912.cafe24.com/comment/comment.php");

                        Calendar m = Calendar.getInstance();
                        int year = m.get(Calendar.YEAR);
                        int month = m.get(Calendar.MONTH)+1;
                        int day = m.get(Calendar.DATE);
                        int hour = m.get(Calendar.HOUR_OF_DAY);
                        int min = m.get(Calendar.MINUTE);
                        int sec = m.get(Calendar.SECOND);

                        String date = year+"."+month+"."+day+" "+hour+":"+min+":"+sec;

                        String result = request.PhPtest2(String.valueOf(text.getText()), userData.getName(), String.valueOf(articleCode),  date);

                        if (result.equals("success")) {
                            adapter = new ComListviewAdapter(getApplicationContext(), R.layout.layout_comment, comArray);
                            Comment_List_Call();
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getApplication(), "Fail.", Toast.LENGTH_SHORT).show();
                        }
                        InputMethodManager mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        mInputMethodManager.hideSoftInputFromWindow(text.getWindowToken(), 0);
                        text.setText("");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    public void Comment_List_Call(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    comArray.clear();

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    String getjsonArticle;
                    String getjsonDate;
                    String getjsonCode;
                    int getjsonACode;
                    /*
                    int getjsonGCode;
                    int articleCode;*/
                    int count =0;

                    while(count<jsonArray.length()) // comment 댓글을 가져오기 위함
                    {
                        JSONObject object = jsonArray.getJSONObject(count);

                        getjsonArticle = object.getString("article");
                        getjsonACode = Integer.parseInt(object.getString("articleCode"));
                        //getjsonGCode = Integer.parseInt(object.getString("groupCode"));
                        getjsonCode = object.getString("userCode");
                        getjsonDate = object.getString("date");
                        count++;

                        if(getjsonACode == articleCode) {
                            com = new commentClass(getjsonArticle, getjsonCode, getjsonACode, getjsonDate);

                            comArray.add(com);
                        }
                        else continue;


                    }
                    Collections.reverse(comArray);
                    adapter = new ComListviewAdapter(getApplicationContext(), R.layout.layout_comment, comArray);
                    view.setAdapter(adapter);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        };

        CommentRequest commentRequest= new CommentRequest(responseListener);
        RequestQueue queueCom = Volley.newRequestQueue(Group_Thread_Activity.this);
        queueCom.add(commentRequest);
    }

    public void Swipe_Refreshing() {
        Comment_List_Call();;
        adapter = new ComListviewAdapter(getApplicationContext(), R.layout.layout_comment, comArray);
        adapter.notifyDataSetChanged();
        srl.setRefreshing(false);
    }
    void Push(final Main_Group_List group_data) {

        Response.Listener<String> responseListener_push = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {


                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    String getToken;
                    int count = 0;
                    while (count < jsonArray.length()) {
                        JSONObject object = jsonArray.getJSONObject(count);
                        getToken = object.getString("token");
                        APIService mService = Common.getFCMClient();
                        Notification notification = new Notification("Thread is uploaded", group_data.getName());
                        Sender sender = new Sender(getToken, notification);

                        mService.sendNotification(sender)
                                .enqueue(new Callback<MYResponse>() {
                                    @Override
                                    public void onResponse(Call<MYResponse> call, retrofit2.Response<MYResponse> response) {
                                        if (response.body().success == 1) {
                                            Toast.makeText(getApplicationContext(), "Thread is uploaded.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<MYResponse> call, Throwable t) {

                                    }
                                });
                        count++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread_Push_Request pushRequest = new Thread_Push_Request(group_data.getGroup_Code(), responseListener_push);
        RequestQueue queue_push = Volley.newRequestQueue(Group_Thread_Activity.this);
        queue_push.add(pushRequest);


    }
}

