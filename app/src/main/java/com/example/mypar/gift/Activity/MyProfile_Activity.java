package com.example.mypar.gift.Activity;

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
import android.view.KeyEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mypar.gift.Adapter.custom_spinner_adapter;
import com.example.mypar.gift.Function.ByteLengthFilter;
import com.example.mypar.gift.Group.Group_Create_Activity;
import com.example.mypar.gift.R;
import com.example.mypar.gift.Structure.UserData;
import com.example.mypar.gift.Structure.User_Upload;
import com.example.mypar.gift.server.Image_User_Send_Request;
import com.example.mypar.gift.server.User_Image_Update_Request;
import com.example.mypar.gift.server.User_Update_Request;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyProfile_Activity extends AppCompatActivity {

    UserData user;
    String userPassword;

    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_ALBUM = 2;
    private static final int CROP_FROM_CAMERA = 3;
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private static final int MULTIPLE_PERMISSIONS = 101;
    private Uri photoUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private ImageView userPhoto;
    private EditText userName, userPhone, userId;
    private Spinner userCountry;

    int Max20 = 20;
    final InputFilter[] filters20 = new InputFilter[] {new ByteLengthFilter(Max20, "KSC5601")};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_);

        Intent intent = getIntent();
        userPassword = intent.getStringExtra("userPassword");
        user= intent.getParcelableExtra("userData");


        final TextView userEmail =  findViewById(R.id.profile_email);
        userName = findViewById(R.id.profile_name);
        userPhone = findViewById(R.id.profile_phone);
        userId = findViewById(R.id.profile_ID);
        userCountry = findViewById(R.id.profile_country);
        userPhoto = findViewById(R.id.profile_photo);
        String country_code[] = getResources().getStringArray(R.array.Country);
        // --------------------------- setting value -------------------------------- //
        userEmail.setText(user.getEmail());
        userId.setText(user.getID());
        userName.setText(user.getName());
        userPhone.setText(user.getPhone());
        if((user.getPhoto()==null) || (user.getPhoto().equals("empty"))){
            userPhoto.setImageResource(R.drawable.main_group_default_photo);
        }else {
            Picasso.with(MyProfile_Activity.this).load(user.getPhoto()).fit().into(userPhoto);
        }
        custom_spinner_adapter adapter = new custom_spinner_adapter(this,
                R.layout.layout_custom_spinner,
                country_code);
        userCountry.setAdapter(adapter);

        int position = 0;
        for(String code : country_code){
            if(code.equals(user.getCountry())){
                userCountry.setSelection(position);
            }
            position++;
        }


        // ------------------------------------- Byte limit -------------------------------- //
        userId.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userId.setFilters(filters20);
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                userId.setFilters(filters20);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                userId.setFilters(filters20);
            }
        });


        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userName.setFilters(filters20);
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                userName.setFilters(filters20);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                userName.setFilters(filters20);
            }
        });

        userPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userPhone.setFilters(filters20);
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                userPhone.setFilters(filters20);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                userPhone.setFilters(filters20);
            }
        });

        userPhoto.setClickable(true);
        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
                AlertDialog.Builder dialog = new AlertDialog.Builder(MyProfile_Activity.this);
                dialog.setTitle("Get image from album?");

                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        goToAlbum();
                    }
                });

                dialog.setNegativeButton("No. I'll take a new picture", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        takePhoto();
                    }
                });
                dialog.show();
            }
        });
        mStorageRef = FirebaseStorage.getInstance().getReference("UserImage");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("UserImage");

        Button finish = findViewById(R.id.profile_finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ChangeName = userName.getText().toString();
                String ChangeId = userId.getText().toString();
                String ChagePhone = userPhone.getText().toString();
                String ChangeCountry = userCountry.getSelectedItem().toString();
                int userCode = user.getUsercode();
                uploadImage(userCode);
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                Toast.makeText(MyProfile_Activity.this, "Update success", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(MyProfile_Activity.this, "Update failed. Please try again.", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                User_Update_Request updateRequest = new User_Update_Request(userCode, ChangeName, ChangeId, ChagePhone, ChangeCountry, responseListener);
                RequestQueue queue = Volley.newRequestQueue(MyProfile_Activity.this);
                queue.add(updateRequest);
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
            Toast.makeText(MyProfile_Activity.this, "Error. Please try again.", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (photoFile != null) {
            photoUri = FileProvider.getUriForFile(MyProfile_Activity.this,
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

                userPhoto = findViewById(R.id.profile_photo);
                Picasso.with(MyProfile_Activity.this).load(photoUri).fit().into(userPhoto);
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage().toString());
            }
        } else if (requestCode == PICK_FROM_CAMERA) {
            MediaScannerConnection.scanFile(MyProfile_Activity.this, new String[]{photoUri.getPath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                }
            });
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                Bitmap thumbImage = ThumbnailUtils.extractThumbnail(bitmap, 512, 512);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                thumbImage.compress(Bitmap.CompressFormat.JPEG, 100, bs);

                userPhoto = findViewById(R.id.profile_photo);
                Picasso.with(MyProfile_Activity.this).load(photoUri).fit().into(userPhoto);
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

    private void uploadImage(final int userCode) {
        if (photoUri != null) {
            StorageReference fileReference = mStorageRef.child(userCode+ "." + getFileExtension(photoUri));
            fileReference.putFile(photoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {}
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
                    User_Image_Update_Request imageRequest = new  User_Image_Update_Request(""+userCode,imgurl, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(MyProfile_Activity.this);
                    queue.add(imageRequest);

                    String uploadId = mDatabaseRef.push().getKey();
                    mDatabaseRef.child(uploadId).setValue(upload);
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MyProfile_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
            User_Image_Update_Request imageRequest = new  User_Image_Update_Request("" + userCode, "empty", responseListener);
            RequestQueue queue = Volley.newRequestQueue(MyProfile_Activity.this);
            queue.add(imageRequest);
        }
    }
}
