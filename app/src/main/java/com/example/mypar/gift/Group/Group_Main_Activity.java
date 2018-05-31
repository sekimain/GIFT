package com.example.mypar.gift.Group;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mypar.gift.Adapter.group_member_adapter;
import com.example.mypar.gift.Adapter.group_thread_adapter;
import com.example.mypar.gift.Function.ByteLengthFilter;
import com.example.mypar.gift.Function.CollegeSelect;
import com.example.mypar.gift.Function.LoadUserImage;
import com.example.mypar.gift.R;
import com.example.mypar.gift.Structure.Group_Member_Data;
import com.example.mypar.gift.Structure.Main_Group_List;
import com.example.mypar.gift.Structure.Thread_Main;
import com.example.mypar.gift.Structure.UserData;
import com.example.mypar.gift.Structure.User_Upload;
import com.example.mypar.gift.server.Article_Request;
import com.example.mypar.gift.server.Group_Delete_Request;
import com.example.mypar.gift.server.Group_Image_Update_Request;
import com.example.mypar.gift.server.Group_Update_Request;
import com.example.mypar.gift.server.Member_Kick_Request;
import com.example.mypar.gift.server.Member_Request;
import com.example.mypar.gift.server.Thread_Info_Request;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.text.InputType.TYPE_CLASS_TEXT;

public class Group_Main_Activity extends AppCompatActivity implements View.OnClickListener {

    private SwipeRefreshLayout srl_thread;
    private SwipeRefreshLayout srl_member;
    private group_thread_adapter adapter_thread;
    private group_member_adapter adapter_member;
    private ArrayList<Thread_Main> listDataArray_thread = new ArrayList<>();
    private ArrayList<Group_Member_Data> listDataArray_member = new ArrayList<>();
    private Spinner spin_collge, spin_major;
    private ListView listView_member, listView_thread;
    private TextView gr_name, gr_professor, gr_email, gr_intro;
    private ImageButton gr_photo;
    private Main_Group_List group_data;
    private UserData usercode;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_ALBUM = 2;
    private static final int CROP_FROM_CAMERA = 3;
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private static final int MULTIPLE_PERMISSIONS = 101;
    private Uri photoUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private Thread_Main articleinfo;

    final InputFilter[] filters40 = new InputFilter[]{new ByteLengthFilter(40, "KSC5601")};
    final InputFilter[] filters100 = new InputFilter[]{new ByteLengthFilter(100, "KSC5601")};

    @SuppressLint("UseSparseArrays")
    private Map<Integer, String> urlMap = new HashMap<Integer, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group__main_);

        Intent intent = getIntent();
        group_data = intent.getParcelableExtra("Group_Name");
        usercode = intent.getParcelableExtra("User_Code");

        Toolbar toolbar = findViewById(R.id.Group_main_toolbar);
        toolbar.setTitle(group_data.getName());

        FloatingActionButton fab = findViewById(R.id.Group_main_write);
        fab.setImageResource(R.drawable.icon_thread_write);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent posting_intent = new Intent(getApplicationContext(), Thread_Posting_Activity.class);
                posting_intent.putExtra("Group_Name", group_data);
                posting_intent.putExtra("User_Code", usercode);

                startActivityForResult(posting_intent, 6);
            }
        });

        TabHost tabs = findViewById(R.id.tabHost);
        tabs.setup();

        TabHost.TabSpec spec1 = tabs.newTabSpec("Thread");
        spec1.setIndicator("Thread");
        spec1.setContent(R.id.tab1);

        TabHost.TabSpec spec2 = tabs.newTabSpec("Member");
        spec2.setIndicator("Member");
        spec2.setContent(R.id.tab2);

        TabHost.TabSpec spec3 = tabs.newTabSpec("Setting");
        spec3.setIndicator("Setting");
        spec3.setContent(R.id.tab3);

        tabs.addTab(spec1);
        tabs.addTab(spec2);
        tabs.addTab(spec3);

        gr_name = findViewById(R.id.group_setting_name_title);
        gr_name.setClickable(true);
        gr_name.setOnClickListener(this);
        gr_professor = findViewById(R.id.group_setting_professor_title);
        gr_professor.setClickable(true);
        gr_professor.setOnClickListener(this);
        gr_email = findViewById(R.id.group_setting_email_title);
        gr_email.setClickable(true);
        gr_email.setOnClickListener(this);
        gr_intro = findViewById(R.id.group_setting_intro_title);
        gr_intro.setClickable(true);
        gr_intro.setOnClickListener(this);

        gr_name = findViewById(R.id.group_setting_name);
        gr_name.setText(group_data.getName());
        gr_name.setClickable(true);
        gr_name.setOnClickListener(this);
        gr_professor = findViewById(R.id.group_setting_professor);
        gr_professor.setText(group_data.getProffesor());
        gr_professor.setClickable(true);
        gr_professor.setOnClickListener(this);
        gr_email = findViewById(R.id.group_setting_email);
        gr_email.setText(group_data.getEmail());
        gr_email.setClickable(true);
        gr_email.setOnClickListener(this);
        gr_intro = findViewById(R.id.group_setting_Intro);
        gr_intro.setText(group_data.getIntro());
        gr_intro.setClickable(true);
        gr_intro.setOnClickListener(this);


        gr_photo = findViewById(R.id.group_setting_photo);
        Picasso.with(Group_Main_Activity.this).load(group_data.getPhoto()).into(gr_photo);

        listView_thread = findViewById(R.id.group_thread_listView);
        listView_thread.setFocusable(false);
        TextView empty_text_thread = findViewById(R.id.group_thread_empty_message);
        listView_thread.setEmptyView(empty_text_thread);

        listView_member = findViewById(R.id.group_member_listView);
        listView_member.setFocusable(false);
        TextView empty_text_member = findViewById(R.id.group_member_empty_message);
        listView_member.setEmptyView(empty_text_member);

        // --------------------- Attach Adapter ----------------------------- //
        adapter_thread = new group_thread_adapter(this, R.layout.layout_group_thread, listDataArray_thread, group_data.getGroup_Admin(), usercode.getUsercode());
        listView_thread.setAdapter(adapter_thread);

        adapter_member = new group_member_adapter(this, R.layout.layout_group_member_list, listDataArray_member);

        // --------------- Join Management button Listener -------------------- //
        Button join_btn = findViewById(R.id.group_member_btn);
        join_btn.setFocusable(false);
        join_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent group_intent = new Intent(getApplicationContext(), Group_Join_Management_Activity.class);
                group_intent.putExtra("Group_data", group_data);
                startActivityForResult(group_intent, 7);
                //////////////////////////////////////////////////////////////////////////////////////
            }
        });
        //----------------- Swipe Refreshing Layout --------------//
        srl_thread = findViewById(R.id.thread_swipe_refresh);
        srl_member = findViewById(R.id.member_swipe_refresh);
        srl_thread.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Swipe_Refreshing(0, 0);
            }
        });
        srl_member.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Swipe_Refreshing(1, group_data.getGroup_Code());
            }
        });

        // Thread //
        Thread_List_Call();

        // Member //
        Member_List_Call(group_data.getGroup_Code());

        // Setting //
        ImageButton setting_reset = findViewById(R.id.group_setting_reset);
        ImageButton setting_done = findViewById(R.id.group_setting_Done);
        ImageButton setting_delete = findViewById(R.id.group_setting_Delete);
        Picasso.with(Group_Main_Activity.this).load(group_data.getPhoto()).fit().into(gr_photo);

        gr_photo.setOnClickListener(this);
        setting_done.setOnClickListener(this);
        setting_delete.setOnClickListener(this);
        setting_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr_name.setText(group_data.getName());
                gr_intro.setText(group_data.getIntro());
                gr_email.setText(group_data.getEmail());
                gr_professor.setText(group_data.getProffesor());
                Picasso.with(Group_Main_Activity.this).load(group_data.getPhoto()).fit().into(gr_photo);
            }
        });

        //---------------------- List View Click Listener -----------------------//
        listView_thread.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), Group_Thread_Activity.class);
                intent.putExtra("object", listDataArray_thread.get(position));
                intent.putExtra("UserData", usercode);
                intent.putExtra("GroupData", group_data);

                startActivity(intent);
            }
        });

        listView_thread.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int pos, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(Group_Main_Activity.this);
                alert.setTitle("Do you really want Delete this Thread ?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 삭제
                        try {

                            Thread_Info_Request request = new Thread_Info_Request("http://rshak8912.cafe24.com/board/delete.php");

                            String result = request.PhPtest4(String.valueOf(listDataArray_thread.get(pos).getarticleCode()));

                            if (result.equals("success")) {
                                Toast.makeText(getApplicationContext(), "Delete success", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getApplicationContext(), "Delete fail", Toast.LENGTH_SHORT).show();
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                });
                alert.setNegativeButton("No", null);
                alert.show();
                return true;
            }
        });

        // --------------------------------------- spinner event ------------------------------------ //
        spin_collge = findViewById(R.id.create_group_college);
        spin_major = findViewById(R.id.create_group_class);

        String college_code[] = getResources().getStringArray(R.array.College);
        // --------------------------- setting value -------------------------------- //
        int position = 0;
        for (String code : college_code) {
            if (code.equals(group_data.getCollege())) {
                spin_collge.setSelection(position);
                final CollegeSelect select = new CollegeSelect(Group_Main_Activity.this, position);
                ArrayAdapter<String> majoradaptor = select.changeSpinner();

                spin_major.setAdapter(majoradaptor);
                spin_major.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int position_m = 0;
                        for (String major : select.majorChanger()) {
                            if (major.equals(group_data.getMajor())) {

                                spin_major.setSelection(position_m);
                            }
                            position_m++;
                        }
                    }
                }, 1000);
            }
            position++;
        }


        spin_collge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CollegeSelect select = new CollegeSelect(Group_Main_Activity.this, position);
                ArrayAdapter<String> majoradaptor = select.changeSpinner();

                spin_major.setAdapter(majoradaptor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                CollegeSelect select = new CollegeSelect(Group_Main_Activity.this, 0);
                ArrayAdapter<String> majoradaptor = select.changeSpinner();

                spin_major.setAdapter(majoradaptor);
            }
        });

        // ---------------- Member List Click, LongClick Listener ------------------- //
        listView_member.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Dialog dialog = new Dialog(Group_Main_Activity.this);
                dialog.setContentView(R.layout.layout_member_data);

                TextView user_name = dialog.findViewById(R.id.member_data_User_name);
                user_name.setText(listDataArray_member.get(position).getName());
                TextView user_id = dialog.findViewById(R.id.member_data_User_id);
                user_id.setText(listDataArray_member.get(position).getID());
                TextView user_email = dialog.findViewById(R.id.member_data_User_email);
                user_email.setText(listDataArray_member.get(position).getemail());
                TextView user_phone = dialog.findViewById(R.id.member_data_User_phone);
                user_phone.setText(listDataArray_member.get(position).getPhone());
                TextView user_country = dialog.findViewById(R.id.member_data_User_country);
                user_country.setText(listDataArray_member.get(position).getCountry());
                ImageView user_photo = dialog.findViewById(R.id.member_data_User_photo);
                if ((listDataArray_member.get(position).getPhoto() == null) || (listDataArray_member.get(position).getPhoto().equals(""))) {
                    user_photo.setImageResource(R.drawable.main_group_default_photo);
                } else {
                    Picasso.with(Group_Main_Activity.this).load(listDataArray_member.get(position).getPhoto()).into(user_photo);
                }
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        });
        listView_member.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int pos, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(Group_Main_Activity.this);
                alert.setTitle("Do you really want to kick out '" + listDataArray_member.get(pos).getName() + "'?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Member_Kick(pos, listDataArray_member.get(pos).getUsercode(), group_data.getGroup_Code());
                    }
                });
                alert.setNegativeButton("No", null);
                alert.show();
                return true;
            }
        });
    }

    public void Swipe_Refreshing(int position, int gr_code) {
        switch (position) {
            case 0:
                Thread_List_Call();
                adapter_thread.notifyDataSetChanged();
                srl_thread.setRefreshing(false);
                break;
            case 1:
                Member_List_Call(gr_code);
                adapter_member.notifyDataSetChanged();
                srl_member.setRefreshing(false);
                break;
        }
    }

    public void Thread_List_Call() {
        listDataArray_thread.clear();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    String getjsonTitle;
                    String getjsonArticle;
                    String getjsonDate;
                    String getjsonFile;
                    String getjsonPath;
                    int getjsonFileFlag;
                    int getjsonGCode, getjsonCode, getjsonACode;
                    int count = 0;

                    while (count < jsonArray.length()) {
                        JSONObject object = jsonArray.getJSONObject(count);
                        getjsonTitle = object.getString("title");
                        getjsonArticle = object.getString("article");
                        getjsonCode = Integer.parseInt(object.getString("userCode"));
                        getjsonGCode = Integer.parseInt(object.getString("groupCode"));
                        getjsonACode = Integer.parseInt(object.getString("articleCode"));
                        getjsonDate = object.getString("date");
                        getjsonDate = getjsonDate.replace(":00.000000", "");
                        getjsonFileFlag = Integer.parseInt(object.getString("fileFlag"));
                        count++;
                        getjsonFile = object.getString("imagename");


                        articleinfo = new Thread_Main(getjsonACode, getjsonTitle, getjsonArticle, getjsonCode, getjsonGCode, getjsonDate, getjsonFile, getjsonFileFlag);
                        if (group_data.getGroup_Code() == articleinfo.getgroupCode()) {
                            if (usercode.getUsercode() == group_data.getGroup_Admin()) {
                                listDataArray_thread.add(articleinfo);
                            } else {
                                if ((usercode.getUsercode() == articleinfo.getuserCode()) || (group_data.getGroup_Admin() == articleinfo.getuserCode())) {
                                    listDataArray_thread.add(articleinfo);
                                }
                            }
                        }
                    }
                    group_thread_adapter adapter = new group_thread_adapter(getApplicationContext(), R.layout.layout_group_thread, listDataArray_thread, group_data.getGroup_Admin(), usercode.getUsercode());
                    listView_thread.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Article_Request ArticleRequest = new Article_Request(responseListener);
        RequestQueue queue = Volley.newRequestQueue(Group_Main_Activity.this);
        queue.add(ArticleRequest);
    }

    public void Member_List_Call(int code) {
        listDataArray_member.clear();
        LoadUserImage loaduserimage = new LoadUserImage(Group_Main_Activity.this);
        urlMap = loaduserimage.getUrlMap();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    String getjsonEmail, getjsonID, getjsonPassword, getjsonName, getjsonCountry, getjsonPhone;
                    int getjsonAge, getjsonCode;
                    int count = 0;
                    while (count < jsonArray.length()) {
                        JSONObject object = jsonArray.getJSONObject(count);
                        getjsonEmail = object.getString("userEmail");
                        getjsonPassword = object.getString("userPassword");
                        getjsonName = object.getString("userName");
                        getjsonID = object.getString("userID");
                        getjsonCountry = object.getString("userCountry");
                        getjsonPhone = object.getString("userPhone");
                        getjsonCode = Integer.parseInt(object.getString("userCode"));

                        Group_Member_Data user = new Group_Member_Data(getjsonName, getjsonEmail, getjsonPhone, getjsonID, getjsonCountry, urlMap.get(getjsonCode), getjsonCode);
                        listDataArray_member.add(user);
                        count++;
                    }
                    listDataArray_member.remove(0);
                    listView_member.setAdapter(adapter_member);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Member_Request memberRequest = new Member_Request(code, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Group_Main_Activity.this);
        queue.add(memberRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.group_setting_name:
                Dialog_setting_text(v, true);
                break;
            case R.id.group_setting_professor:
                Dialog_setting_text(v, true);
                break;
            case R.id.group_setting_email:
                Dialog_setting_text(v, true);
                break;
            case R.id.group_setting_Intro:
                Dialog_setting_text(v, false);
                break;
            case R.id.group_setting_Done:

                spin_collge = findViewById(R.id.create_group_college);
                spin_major = findViewById(R.id.create_group_class);

                String NameText = gr_name.getText().toString();
                String IntroText = gr_intro.getText().toString();
                String ProfessorText = gr_professor.getText().toString();
                String EmailText = gr_email.getText().toString();
                uploadImage(group_data.getGroup_Code());
                int groupCode = this.group_data.getGroup_Code();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                Toast.makeText(Group_Main_Activity.this, "Update success", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Group_Main_Activity.this, "Update failed. Please try again.", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                Group_Update_Request updateRequest = new Group_Update_Request(groupCode, NameText, IntroText, ProfessorText, EmailText, spin_collge.getSelectedItem().toString(), spin_major.getSelectedItem().toString(), responseListener);
                RequestQueue queue = Volley.newRequestQueue(Group_Main_Activity.this);
                queue.add(updateRequest);
                finish();
                break;
            case R.id.group_setting_Delete:
                final int group_delete = this.group_data.getGroup_Code();
                AlertDialog.Builder alert = new AlertDialog.Builder(Group_Main_Activity.this);
                alert.setTitle("Do you really want to delete this group?");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Response.Listener<String> responseListenera = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");

                                    if (success) {
                                        finish();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        Group_Delete_Request deleteRequest = new Group_Delete_Request(group_delete, responseListenera);
                        RequestQueue queue2 = Volley.newRequestQueue(Group_Main_Activity.this);
                        queue2.add(deleteRequest);
                    }
                });
                alert.setNegativeButton("Cancel", null);
                alert.show();

                break;
            case R.id.group_setting_photo:
                checkPermissions();
                AlertDialog.Builder dialog = new AlertDialog.Builder(Group_Main_Activity.this);
                dialog.setTitle("Get image from album?");

                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        goToAlbum();
                    }
                });

                dialog.setNegativeButton("No, I'll take a new picture.", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        takePhoto();
                    }
                });
                dialog.show();

                mStorageRef = FirebaseStorage.getInstance().getReference("UserImage");
                mDatabaseRef = FirebaseDatabase.getInstance().getReference("UserImage");
                break;
        }
    }

    public void Dialog_setting_text(View v, final boolean position) {
        final TextView inputvalue = findViewById(v.getId());
        final EditText dialog_edit = new EditText(Group_Main_Activity.this);

        dialog_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (position) {
                    dialog_edit.setFilters(filters40);
                } else {
                    dialog_edit.setFilters(filters100);
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (position) {
                    dialog_edit.setFilters(filters40);
                } else {
                    dialog_edit.setFilters(filters100);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (position) {
                    dialog_edit.setFilters(filters40);
                } else {
                    dialog_edit.setFilters(filters100);
                }
            }
        });
        if (position) {
            dialog_edit.setInputType(TYPE_CLASS_TEXT);
        }
        AlertDialog.Builder alert = new AlertDialog.Builder(Group_Main_Activity.this);
        alert.setTitle("Set value that you want");
        alert.setView(dialog_edit);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                inputvalue.setText(dialog_edit.getText().toString());
            }
        });
        alert.setNegativeButton("Cancel", null);
        alert.show();
    }

    public void Member_Kick(final int i, final int user_code, final int group_code) {
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        listDataArray_member.remove(i);
                        adapter_member.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Member_Kick_Request outRequest = new Member_Kick_Request(user_code, group_code, response);
        RequestQueue queue = Volley.newRequestQueue(Group_Main_Activity.this);
        queue.add(outRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(3);
    }

    // -------------------------- check permissons -------------------------- //
    private boolean checkPermissions() {
        int result;
        List<String> permissionList = new ArrayList<>();
        for (String pm : permissions) {
            result = ContextCompat.checkSelfPermission(this, pm);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(pm);
            }
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (permissions[i].equals(this.permissions[0])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();
                            }
                        } else if (permissions[i].equals(this.permissions[1])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        } else if (permissions[i].equals(this.permissions[2])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        }
                    }
                } else {
                    showNoPermissionToastAndFinish();
                }
                return;
            }
        }
    }

    private void showNoPermissionToastAndFinish() {
        Toast.makeText(this, "It is available when you agree with the request of permission. Please give permission by setting.", Toast.LENGTH_SHORT).show();
        finish();
    }

    // -------------------------- take a picture ---------------------------- //
    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            finish();
        }
        if (photoFile != null) {
            photoUri = FileProvider.getUriForFile(Group_Main_Activity.this,
                    "com.example.mypar.gift.provider", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, PICK_FROM_CAMERA);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "IP" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Gift_picture/");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        return image;
    }

    private void goToAlbum() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {

        }
        if (requestCode == PICK_FROM_ALBUM) {
            if (data == null) {
                return;
            }
            photoUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                Bitmap thumbImage = ThumbnailUtils.extractThumbnail(bitmap, 512, 512);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                thumbImage.compress(Bitmap.CompressFormat.JPEG, 100, bs);

                gr_photo = findViewById(R.id.group_setting_photo);
                Picasso.with(Group_Main_Activity.this).load(photoUri).fit().into(gr_photo);
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage().toString());
            }
        } else if (requestCode == PICK_FROM_CAMERA) {
            MediaScannerConnection.scanFile(Group_Main_Activity.this, new String[]{photoUri.getPath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                }
            });
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                Bitmap thumbImage = ThumbnailUtils.extractThumbnail(bitmap, 512, 512);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                thumbImage.compress(Bitmap.CompressFormat.JPEG, 100, bs);

                gr_photo = findViewById(R.id.group_setting_photo);
                Picasso.with(Group_Main_Activity.this).load(photoUri).fit().into(gr_photo);
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage().toString());
            }
        } else {
            Swipe_Refreshing(0, 0);
            Swipe_Refreshing(1, group_data.getGroup_Code());
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadImage(final int userCode) {
        if (photoUri != null) {
            StorageReference fileReference = mStorageRef.child(userCode + "." + getFileExtension(photoUri));
            fileReference.putFile(photoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                        }
                    }, 5000);

                    User_Upload upload = new User_Upload(taskSnapshot.getDownloadUrl().toString(), userCode);
                    String imgurl = taskSnapshot.getDownloadUrl().toString();

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    Group_Image_Update_Request imageRequest = new Group_Image_Update_Request("" + userCode, imgurl, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(Group_Main_Activity.this);
                    queue.add(imageRequest);

                    String uploadId = mDatabaseRef.push().getKey();
                    mDatabaseRef.child(uploadId).setValue(upload);
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Group_Main_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            Group_Image_Update_Request imageRequest = new Group_Image_Update_Request("" + userCode, "empty", responseListener);
            RequestQueue queue = Volley.newRequestQueue(Group_Main_Activity.this);
            queue.add(imageRequest);
        }
    }
}
