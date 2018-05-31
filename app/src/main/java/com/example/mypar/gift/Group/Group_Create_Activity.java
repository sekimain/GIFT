package com.example.mypar.gift.Group;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mypar.gift.Activity.Register_Activity;
import com.example.mypar.gift.Adapter.custom_spinner_adapter;
import com.example.mypar.gift.Function.CollegeSelect;
import com.example.mypar.gift.R;
import com.example.mypar.gift.Function.ByteLengthFilter;
import com.example.mypar.gift.Structure.Group_Member_Data;
import com.example.mypar.gift.Structure.Upload;
import com.example.mypar.gift.server.Create_Request;
import com.example.mypar.gift.server.Group_Join_Request;
import com.example.mypar.gift.server.Image_Send_Request;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.acl.Group;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Group_Create_Activity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_ALBUM = 2;
    private static final int CROP_FROM_CAMERA = 3;
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private static final int MULTIPLE_PERMISSIONS = 101;
    private EditText groupName, groupProfessor, groupEmail;
    private EditText groupIntro;
    private Spinner spin_collge, spin_major;
    private Button btn_create;
    private ImageView btn_image;
    private Uri photoUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    int Max40 = 40;
    int Max100 = 100;
    final InputFilter[] filters40 = new InputFilter[]{new ByteLengthFilter(Max40, "KSC5601")};
    final InputFilter[] filters100 = new InputFilter[]{new ByteLengthFilter(Max100, "KSC5601")};

    int groupCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group__create_);
        // NetworkUtil.setNetworkPolicy();

        groupName = findViewById(R.id.create_group_name);
        groupIntro = findViewById(R.id.create_group_intro);
        groupProfessor = findViewById(R.id.create_group_professor);
        groupEmail = findViewById(R.id.create_group_email);
        btn_image = findViewById(R.id.GroupImage);
        btn_create = findViewById(R.id.create_ok_button);
        mStorageRef = FirebaseStorage.getInstance().getReference("GroupImage");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("GroupImage");

        groupIntro.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                groupIntro.setFilters(filters100);
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                groupIntro.setFilters(filters100);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                groupIntro.setFilters(filters100);
            }
        });
        groupName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                groupName.setFilters(filters40);
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                groupName.setFilters(filters40);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                groupName.setFilters(filters40);
            }
        });
        groupEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                groupEmail.setFilters(filters40);
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                groupEmail.setFilters(filters40);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                groupEmail.setFilters(filters40);
            }
        });
        groupProfessor.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                groupProfessor.setFilters(filters40);
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                groupProfessor.setFilters(filters40);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                groupProfessor.setFilters(filters40);
            }
        });

        // ----------------------------------------- select image ---------------------------------- //

        Picasso.with(Group_Create_Activity.this).load(R.mipmap.ic_launcher_round).fit().into(btn_image);
        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions();
                AlertDialog.Builder dialog = new AlertDialog.Builder(Group_Create_Activity.this);
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
            }
        });

        // --------------------------------------- spinner event ------------------------------------ //
        spin_collge = findViewById(R.id.create_group_college);
        spin_major = findViewById(R.id.create_group_class);

        spin_collge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CollegeSelect select = new CollegeSelect(Group_Create_Activity.this, position);
                ArrayAdapter<String> majoradaptor = select.changeSpinner();

                spin_major.setAdapter(majoradaptor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                CollegeSelect select = new CollegeSelect(Group_Create_Activity.this, 0);
                ArrayAdapter<String> majoradaptor = select.changeSpinner();

                spin_major.setAdapter(majoradaptor);
            }
        });
        // ---------------------------------------- finish button ------------------------------------ //

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(Group_Create_Activity.this);
                alert.setTitle("Are you sure?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            String NameText = groupName.getText().toString();
                            String IntroText = groupIntro.getText().toString();
                            String ProfessorText = groupProfessor.getText().toString();
                            String EmailText = groupEmail.getText().toString();
                            String College = spin_collge.getSelectedItem().toString();
                            String Major = spin_major.getSelectedItem().toString();
                            Calendar m = Calendar.getInstance();
                            int year = m.get(Calendar.YEAR);
                            int month = m.get(Calendar.MONTH) + 1;
                            int day = m.get(Calendar.DATE);
                            final String DayText = year + "." + month + "." + day;

                            Intent intent = getIntent();
                            int userID = intent.getIntExtra("userCode", 0);

                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonResponse = new JSONObject(response);
                                        boolean success = jsonResponse.getBoolean("success");
                                        groupCode = jsonResponse.getInt("groupCode");

                                        if (success) {
                                            uploadImage(groupCode + 1);

                                            AlertDialog.Builder builder = new AlertDialog.Builder(Group_Create_Activity.this);
                                            builder.setMessage("Success")
                                                    .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            finish();
                                                        }
                                                    })
                                                    .create()
                                                    .show();

                                        } else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(Group_Create_Activity.this);
                                            builder.setMessage("Failed")
                                                    .setNegativeButton("Try again", null)
                                                    .create()
                                                    .show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            Create_Request createRequest = new Create_Request(userID, NameText, IntroText, ProfessorText, EmailText, userID, DayText, College, Major, responseListener);
                            RequestQueue queue = Volley.newRequestQueue(Group_Create_Activity.this);
                            queue.add(createRequest);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
                alert.setNegativeButton("No", null);
                alert.show();
            }
        });

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
            Toast.makeText(Group_Create_Activity.this, "Error. Please try again.", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (photoFile != null) {
            photoUri = FileProvider.getUriForFile(Group_Create_Activity.this,
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

                btn_image = findViewById(R.id.GroupImage);
                Picasso.with(Group_Create_Activity.this).load(photoUri).fit().into(btn_image);
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage().toString());
            }
        } else if (requestCode == PICK_FROM_CAMERA) {
            MediaScannerConnection.scanFile(Group_Create_Activity.this, new String[]{photoUri.getPath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                }
            });
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                Bitmap thumbImage = ThumbnailUtils.extractThumbnail(bitmap, 512, 512);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                thumbImage.compress(Bitmap.CompressFormat.JPEG, 100, bs);

                btn_image = findViewById(R.id.GroupImage);
                Picasso.with(Group_Create_Activity.this).load(photoUri).fit().into(btn_image);
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage().toString());
            }
        }
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadImage(final int groupCode) {
        if (photoUri != null) {
            StorageReference fileReference = mStorageRef.child(groupCode + "." + getFileExtension(photoUri));
            fileReference.putFile(photoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                        }
                    }, 5000);

                    Upload upload = new Upload(taskSnapshot.getDownloadUrl().toString(), groupCode);
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
                    Image_Send_Request imageRequest = new Image_Send_Request("" + groupCode, imgurl, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(Group_Create_Activity.this);
                    queue.add(imageRequest);

                    String uploadId = mDatabaseRef.push().getKey();
                    mDatabaseRef.child(uploadId).setValue(upload);
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Group_Create_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
            Image_Send_Request imageRequest = new Image_Send_Request("" + groupCode, "empty", responseListener);
            RequestQueue queue = Volley.newRequestQueue(Group_Create_Activity.this);
            queue.add(imageRequest);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(1);
    }
}