package com.example.mypar.gift.Function;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.mypar.gift.Group.Group_Create_Activity;
import com.example.mypar.gift.R;


public class CollegeSelect {

    private Context row;
    private int position = 0;
    private ArrayAdapter<String> spin_major_adapter;
    private String major[];

    public CollegeSelect(Context context, int position)
    {
        this.row = context;
        this.position = position;
    }

    public ArrayAdapter<String> changeSpinner(){
        switch(this.position){
            case 0:
                spin_major_adapter = new ArrayAdapter<>(row,
                        android.R.layout.simple_spinner_item,
                        row.getResources().getStringArray(R.array.major_0));
                break;
            case 1:
                spin_major_adapter = new ArrayAdapter<>(row,
                        android.R.layout.simple_spinner_item,
                        row.getResources().getStringArray(R.array.major_1));
                break;
            case 2:
                spin_major_adapter = new ArrayAdapter<>(row,
                        android.R.layout.simple_spinner_item,
                        row.getResources().getStringArray(R.array.major_2));
                break;
            case 3:
                spin_major_adapter = new ArrayAdapter<>(row,
                        android.R.layout.simple_spinner_item,
                        row.getResources().getStringArray(R.array.major_3));
                break;
            case 4:
                spin_major_adapter = new ArrayAdapter<>(row,
                        android.R.layout.simple_spinner_item,
                        row.getResources().getStringArray(R.array.major_4));
                break;
            case 5:
                spin_major_adapter = new ArrayAdapter<>(row,
                        android.R.layout.simple_spinner_item,
                        row.getResources().getStringArray(R.array.major_5));
                break;
            case 6:
                spin_major_adapter = new ArrayAdapter<>(row,
                        android.R.layout.simple_spinner_item,
                        row.getResources().getStringArray(R.array.major_6));
                break;
            case 7:
                spin_major_adapter = new ArrayAdapter<>(row,
                        android.R.layout.simple_spinner_item,
                        row.getResources().getStringArray(R.array.major_7));
                break;
            case 8:
                spin_major_adapter = new ArrayAdapter<>(row,
                        android.R.layout.simple_spinner_item,
                        row.getResources().getStringArray(R.array.major_8));
                break;
            case 9:
                spin_major_adapter = new ArrayAdapter<>(row,
                        android.R.layout.simple_spinner_item,
                        row.getResources().getStringArray(R.array.major_9));
                break;
            case 10:
                spin_major_adapter = new ArrayAdapter<>(row,
                        android.R.layout.simple_spinner_item,
                        row.getResources().getStringArray(R.array.major_10));
                break;
            case 11:
                spin_major_adapter = new ArrayAdapter<>(row,
                        android.R.layout.simple_spinner_item,
                        row.getResources().getStringArray(R.array.major_11));
                break;
        }
        return spin_major_adapter;
    }

    public String[] majorChanger(){
        switch(this.position){
            case 0:
                major = row.getResources().getStringArray(R.array.major_0);
                break;
            case 1:
                major = row.getResources().getStringArray(R.array.major_1);
                break;
            case 2:
                major = row.getResources().getStringArray(R.array.major_2);
                break;
            case 3:
                major = row.getResources().getStringArray(R.array.major_3);
                break;
            case 4:
                major = row.getResources().getStringArray(R.array.major_4);
                break;
            case 5:
                major = row.getResources().getStringArray(R.array.major_5);
                break;
            case 6:
                major = row.getResources().getStringArray(R.array.major_6);
                break;
            case 7:
                major = row.getResources().getStringArray(R.array.major_7);
                break;
            case 8:
                major = row.getResources().getStringArray(R.array.major_8);
                break;
            case 9:
                major = row.getResources().getStringArray(R.array.major_9);
                break;
            case 10:
                major = row.getResources().getStringArray(R.array.major_10);
                break;
            case 11:
                major = row.getResources().getStringArray(R.array.major_11);
                break;
        }
        return major;
    }
}
