package com.example.mypar.gift.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mypar.gift.R;
import com.example.mypar.gift.Service.Constants;
import com.example.mypar.gift.Structure.Pdf;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Pdf_Activity extends AppCompatActivity{
    //the listview
    ListView listView;
    int filecount;
    //database reference to get uploads data
    DatabaseReference mDatabaseReference;

    //list to store uploads data
    List<Pdf> pdfList;
    int articleCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        pdfList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listView);

        Intent intent = getIntent();
        articleCode = intent.getIntExtra("articleCode",0);

        //adding a clicklistener on listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the upload
                Pdf pdf = pdfList.get(i);

                //Opening the upload file in browser using the upload url
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(pdf.getUrl()));
                startActivity(intent);
            }
        });


        //getting the database reference
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS).child(""+articleCode);

        //retrieving upload data from firebase database
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Pdf pdf = postSnapshot.getValue(Pdf.class);
                    pdfList.add(pdf);
                }

                final String[] pdfs = new String[pdfList.size()];

                for (int i = 0; i < pdfs.length; i++) {
                    filecount=i+1;
                    pdfs[i] = pdfList.get(i).getFilename();
                }

                //displaying it to list
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, pdfs){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent){
                        View row = convertView;
                        if(row == null){
                            LayoutInflater inflater = LayoutInflater.from(Pdf_Activity.this);
                            row = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                        }
                        TextView ListItemShow =(TextView) row.findViewById(android.R.id.text1);
                        ListItemShow.setText(pdfs[position]);
                        ListItemShow.setTextColor(Color.BLACK);
                        return row;
                    };
                };
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
