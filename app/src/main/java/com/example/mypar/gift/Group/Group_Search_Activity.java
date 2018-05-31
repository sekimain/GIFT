package com.example.mypar.gift.Group;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mypar.gift.Adapter.main_group_adapter;
import com.example.mypar.gift.Function.ByteLengthFilter;
import com.example.mypar.gift.Function.CollegeSelect;
import com.example.mypar.gift.Function.LoadGroupImage;
import com.example.mypar.gift.R;
import com.example.mypar.gift.Structure.Main_Group_List;
import com.example.mypar.gift.server.Group_Enter_Request;
import com.example.mypar.gift.server.MyGroup_Get_Request;
import com.example.mypar.gift.server.Search_Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Group_Search_Activity extends AppCompatActivity {

    private main_group_adapter adapter;
    private ArrayList<Main_Group_List> listDataArray = new ArrayList<>();
    private ArrayList<Main_Group_List> saveList = new ArrayList<>();
    private ArrayList<Integer> except = new ArrayList<>();
    private SwipeRefreshLayout srl_text, srl_spinner;
    private ListView listview_text, listView_spinner;
    private Spinner spin_college, spin_major;
    private EditText search_group;
    private int userCode;
    int maxByte = 40;
    private Map<Integer, String> urlMap = new HashMap<Integer, String>();
    final InputFilter[] filters = new InputFilter[] {new ByteLengthFilter(maxByte, "KSC5601")};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group__search_);

        Intent intent = getIntent();
        userCode = intent.getIntExtra("userCode",0);
        // ------------------ give focus to title ------------------- //
        Toolbar toolbar = findViewById(R.id.Group_search_toolbar);
        toolbar.setTitle("Search New Group");
        toolbar.setFocusable(true);   toolbar.requestFocus();

        TabHost tabs = (TabHost) findViewById(R.id.tabHost);
        tabs.setup();

        TabHost.TabSpec spec1 = tabs.newTabSpec("Text");
        spec1.setIndicator("이름으로 찾기");
        spec1.setContent(R.id.tab1);
        ConstraintLayout tab1 = findViewById(R.id.tab1);
        tab1.performClick(); tab1.callOnClick();
        tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Swipe_Refreshing_text();
            }
        });

        TabHost.TabSpec spec2 = tabs.newTabSpec("Spinner");
        spec2.setIndicator("학과로 찾기");
        spec2.setContent(R.id.tab2);
        ConstraintLayout tab2 = findViewById(R.id.tab2);
        tab2.performClick(); tab1.callOnClick();
        tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Swipe_Refreshing_spinner();
            }
        });
        tabs.addTab(spec1);
        tabs.addTab(spec2);

        // --------------------------------------- spinner event ------------------------------------ //
        spin_college = findViewById(R.id.group_search_college);
        spin_major = findViewById(R.id.group_search_class);

        spin_college.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CollegeSelect select = new CollegeSelect(Group_Search_Activity.this, position);
                ArrayAdapter<String> majoradaptor = select.changeSpinner();
                searchGroup_spinner(spin_college.getSelectedItem().toString(), spin_major.getSelectedItem().toString());
                spin_major.setAdapter(majoradaptor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                CollegeSelect select = new CollegeSelect(Group_Search_Activity.this, 0);
                ArrayAdapter<String> majoradaptor = select.changeSpinner();
                searchGroup_spinner(spin_college.getSelectedItem().toString(), spin_major.getSelectedItem().toString());
                spin_major.setAdapter(majoradaptor);
            }
        });
        spin_major.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchGroup_spinner(spin_college.getSelectedItem().toString(), spin_major.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                searchGroup_spinner(spin_college.getSelectedItem().toString(), spin_major.getSelectedItem().toString());
            }
        });


        //----------------- Swipe Refreshing Layout --------------//
        srl_text = findViewById(R.id.main_swipe_refresh);
        srl_text.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Swipe_Refreshing_text();
            }
        });
        srl_spinner = findViewById(R.id.serach_swipe_refresh);
        srl_spinner.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Swipe_Refreshing_spinner();
            }
        });
        //----------------- List View Click Listener ---------------------//
        listview_text = findViewById(R.id.group_search_list_view);
        listview_text.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView_Click_Action(position, userCode);
            }
        });
        listView_spinner = findViewById(R.id.group_search_list_view_spinner);
        listView_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView_Click_Action(position, userCode);
            }
        });
        // --------------------- attach adapter --------------------------- //
        adapter = new main_group_adapter(Group_Search_Activity.this, R.layout.layout_main_group_list, userCode, listDataArray);
        Group_List_Call();

        // ------------------ Search text with byte limit setting ---------------- //
        search_group = findViewById(R.id.editText_Search_New_Group);
        search_group.setOnKeyListener(new View.OnKeyListener() {    // enter key setting
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    searchGroup_text(search_group.getText().toString());
                    return true;
                }
                return false;
            }
        });

        search_group.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchGroup_text(search_group.getText().toString());
                    return true;
                }else{return false;}
            }
        });
        search_group.addTextChangedListener(new TextWatcher() {     // text changed setting
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchGroup_text(s.toString());
                search_group.setFilters(filters);
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                search_group.setFilters(filters);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                search_group.setFilters(filters);
            }
        });
    }

    public void Swipe_Refreshing_text(){
        Group_List_Call();
        adapter = new main_group_adapter(Group_Search_Activity.this, R.layout.layout_main_group_list, userCode, listDataArray);
        adapter.notifyDataSetChanged();

        search_group = findViewById(R.id.editText_Search_New_Group);
        searchGroup_text(search_group.getText().toString());

        srl_text = findViewById(R.id.main_swipe_refresh);
        srl_text.setRefreshing(false);
    }
    public void Swipe_Refreshing_spinner(){
        Group_List_Call();
        adapter = new main_group_adapter(Group_Search_Activity.this, R.layout.layout_main_group_list, userCode, listDataArray);
        adapter.notifyDataSetChanged();

        spin_college = findViewById(R.id.group_search_college);
        spin_major = findViewById(R.id.group_search_class);
        searchGroup_spinner(spin_college.getSelectedItem().toString(), spin_major.getSelectedItem().toString());

        srl_spinner = findViewById(R.id.serach_swipe_refresh);
        srl_spinner.setRefreshing(false);
    }
    public void Group_List_Call(){
        GetMygroup();
        listview_text = findViewById(R.id.group_search_list_view);
        listView_spinner = findViewById(R.id.group_search_list_view_spinner);

        // ------------------ Group List View ------------------ //
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    listDataArray.clear();
                    saveList.clear();
                    urlMap.clear();
                    LoadGroupImage loadgroupimage = new LoadGroupImage(Group_Search_Activity.this);
                    urlMap = loadgroupimage.getUrlMap();

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    String getjsonGName,getjsonIntro,getjsonProfessor,getjsonEmail,getjsonDate, getjsonCollege, getjsonMajor;
                    int getjsonAdmin,getjsonCode;
                    int count =0;

                    while(count<jsonArray.length())
                    {
                        JSONObject object = jsonArray.getJSONObject(count);
                        getjsonGName = object.getString("groupName");
                        getjsonIntro= object.getString("groupIntro");
                        getjsonProfessor = object.getString("groupProfessor");
                        getjsonEmail = object.getString("groupEmail");
                        getjsonDate = object.getString("groupDate");
                        getjsonCode = Integer.parseInt(object.getString("groupCode"));
                        getjsonAdmin = Integer.parseInt(object.getString("groupAdmin"));
                        getjsonCollege = object.getString("groupCollege");
                        getjsonMajor = object.getString("groupMajor");

                        if (!except.contains(getjsonCode)) {
                            Main_Group_List group = new Main_Group_List(getjsonGName,getjsonIntro,getjsonProfessor,getjsonEmail,getjsonDate,urlMap.get(getjsonCode),getjsonCollege, getjsonMajor, getjsonCode,getjsonAdmin);
                            listDataArray.add(group);
                            saveList.add(group);
                        }
                        count++;
                    }
                    adapter = new main_group_adapter(Group_Search_Activity.this, R.layout.layout_main_group_list, userCode, listDataArray);

                    listview_text.setAdapter(adapter);
                    listView_spinner.setAdapter(adapter);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        Search_Request searchRequest = new Search_Request("",responseListener);
        RequestQueue queue = Volley.newRequestQueue(Group_Search_Activity.this);
        queue.add(searchRequest);
    }

    // -------------------------------- List View Listener ------------------------------- //
    public void ListView_Click_Action(final int position, final int user_Code) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(Group_Search_Activity.this);
        dialog.setTitle("Join this Group?");

        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);

                            boolean success = jsonResponse.getBoolean("success");

                            if(success)
                            {
                                Toast.makeText(Group_Search_Activity.this, "Completed", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(Group_Search_Activity.this, "You entered this group already.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                };
               Group_Enter_Request enterRequest = new Group_Enter_Request(user_Code,listDataArray.get(position).getGroup_Code(),responseListener);
               RequestQueue queue = Volley.newRequestQueue(Group_Search_Activity.this);
               queue.add(enterRequest);
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
    public void searchGroup_text(String search)
    {
        listDataArray.clear();
        for(int i=0;i<saveList.size();i++)
        {
            if(saveList.get(i).getName().contains(search))
            {
                listDataArray.add(saveList.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }
    public void searchGroup_spinner(String college, String major)
    {
        listDataArray.clear();
        for(int i=0;i<saveList.size();i++)
        {
            if((saveList.get(i).getCollege().equals(college)) && (saveList.get(i).getMajor().equals(major)))
            {
                listDataArray.add(saveList.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }
    public void GetMygroup() {
        except.clear();

        Response.Listener<String> responseListener_GetMyGroup = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");

                    int getjsonCode;

                    int count = 0;
                    while (count < jsonArray.length()) {
                        JSONObject object = jsonArray.getJSONObject(count);
                        getjsonCode = Integer.parseInt(object.getString("groupCode"));
                        except.add(getjsonCode);
                        count++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        MyGroup_Get_Request MyGroupGetRequest = new MyGroup_Get_Request(userCode, responseListener_GetMyGroup);
        RequestQueue queue_my = Volley.newRequestQueue(Group_Search_Activity.this);
        queue_my.add(MyGroupGetRequest);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        setResult(0);
    }
}
