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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mypar.gift.Adapter.custom_spinner_adapter;
import com.example.mypar.gift.Group.Group_Create_Activity;
import com.example.mypar.gift.R;
import com.example.mypar.gift.Function.ByteLengthFilter;
import com.example.mypar.gift.Structure.User_Upload;
import com.example.mypar.gift.server.Image_User_Send_Request;
import com.example.mypar.gift.server.Register_Request;
import com.example.mypar.gift.server.Vaildate_Request;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Register_Activity extends AppCompatActivity {

    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_ALBUM = 2;
    private static final int CROP_FROM_CAMERA = 3;
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private static final int MULTIPLE_PERMISSIONS = 101;
    private Uri photoUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    ImageView register_photo;

    int Max40 = 40;
    int Max20 = 20;
    final InputFilter[] filters40 = new InputFilter[] {new ByteLengthFilter(Max40, "KSC5601")};
    final InputFilter[] filters20 = new InputFilter[] {new ByteLengthFilter(Max20, "KSC5601")};

    boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);

        // ------------------------- user photo - default = unknown ----------------------------- //
        register_photo = findViewById(R.id.register_User_photo);
        register_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
                AlertDialog.Builder dialog = new AlertDialog.Builder(Register_Activity.this);
                dialog.setTitle("Get image from album?");

                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        goToAlbum();
                    }
                });

                dialog.setNegativeButton("No, I'll take a new picture", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        takePhoto();
                    }
                });
                dialog.show();
            }
        });
        mStorageRef = FirebaseStorage.getInstance().getReference("UserImage");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("UserImage");
        // ----------------------------------- Text Length Limit -------------------------------- //
        final EditText idText = findViewById(R.id.register_email);
        final EditText passwordText = findViewById(R.id.register_password);
        final EditText passwordCheck = findViewById(R.id.register_password_check);
        final EditText studentText = findViewById(R.id.register_student_id);
        final EditText phoneText = findViewById(R.id.register_phone);
        final EditText nameText = findViewById(R.id.register_student_name);
        final Spinner spinner = findViewById(R.id.profile_country);

        idText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                idText.setFilters(filters40);
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                idText.setFilters(filters40);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                idText.setFilters(filters40);
            }
        });
        passwordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordText.setFilters(filters40);
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                passwordText.setFilters(filters40);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                passwordText.setFilters(filters40);
            }
        });
        studentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                studentText.setFilters(filters20);
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                studentText.setFilters(filters20);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                studentText.setFilters(filters20);
            }
        });

        phoneText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phoneText.setFilters(filters20);
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                phoneText.setFilters(filters20);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                phoneText.setFilters(filters20);
            }
        });
        phoneText.setOnKeyListener(new View.OnKeyListener() {    // enter key setting
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return true;
                }
                return false;
            }
        });

        nameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameText.setFilters(filters20);
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                nameText.setFilters(filters20);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                nameText.setFilters(filters20);
            }
        });
    // ------------------------------------------------------------------------------------------ //
        custom_spinner_adapter  adapter = new custom_spinner_adapter(this,
                R.layout.layout_custom_spinner,
                this.getResources().getStringArray(R.array.Country));
        spinner.setAdapter(adapter);

        // ------------------------------ id duplicate check ----------------------------------- //
        Button idcheck = findViewById(R.id.register_id_check);
        idcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = idText.getText().toString();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try
                        {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success)
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Register_Activity.this);
                                builder.setMessage("You can use this ID.")
                                        .setPositiveButton("OK",null)
                                        .create()
                                        .show();
                                idText.setEnabled(false);
                                check = true;
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Register_Activity.this);
                                builder.setMessage("Duplicated ID.")
                                        .setNegativeButton("cancel",null)
                                        .create()
                                        .show();
                            }
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                };
                Vaildate_Request vaildateRequest = new Vaildate_Request(userEmail,responseListener);
                RequestQueue queue = Volley.newRequestQueue(Register_Activity.this);
                queue.add(vaildateRequest);

            }
        });
        // ------------------------------- finish button -------------------------------------- //
        Button registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!check){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register_Activity.this);
                    builder.setMessage("Please check ID duplicate.")
                            .setPositiveButton("OK", null)
                            .create()
                            .show();
                }else if(idText.getText().equals("") || idText.getText().toString().isEmpty() ||
                        studentText.getText().equals("") ||  studentText.getText().toString().isEmpty() ||
                        passwordText.getText().equals("") || passwordText.getText().toString().isEmpty() ||
                        nameText.getText().equals("") || nameText.getText().toString().isEmpty() ||
                        phoneText.getText().equals("") ||  phoneText.getText().toString().isEmpty()) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Register_Activity.this);
                    builder.setMessage("Please check the blank.")
                            .setPositiveButton("OK", null)
                            .create()
                            .show();
                }else if(!passwordCheck.getText().toString().equals(passwordText.getText().toString())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register_Activity.this);
                    builder.setMessage("Passwords are different.")
                            .setPositiveButton("OK", null)
                            .create()
                            .show();
                }else {
                    String userEmail = idText.getText().toString();
                    String userID = studentText.getText().toString();
                    String userPassword = passwordText.getText().toString();
                    String userName = nameText.getText().toString();
                    String userPhone = phoneText.getText().toString();
                    String userCountry = spinner.getSelectedItem().toString();
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                int userCode = jsonResponse.getInt("userCode");

                                if (success) {
                                    uploadImage(userCode);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Register_Activity.this);
                                    builder.setMessage("Success")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    finish();
                                                }
                                            })
                                            .create()
                                            .show();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Register_Activity.this);
                                    builder.setMessage("failed")
                                            .setNegativeButton("OK", null)
                                            .create()
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    Register_Request registerRequest = new Register_Request(userEmail, userPassword, userName, userID, userPhone, userCountry, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(Register_Activity.this);
                    queue.add(registerRequest);
                }
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
            Toast.makeText(Register_Activity.this, "Error. Please try again.", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (photoFile != null) {
            photoUri = FileProvider.getUriForFile(Register_Activity.this,
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

                register_photo = findViewById(R.id.register_User_photo);
                Picasso.with(Register_Activity.this).load(photoUri).fit().into(register_photo);
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage().toString());
            }
        } else if (requestCode == PICK_FROM_CAMERA) {
            MediaScannerConnection.scanFile(Register_Activity.this, new String[]{photoUri.getPath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                }
            });
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                Bitmap thumbImage = ThumbnailUtils.extractThumbnail(bitmap, 512, 512);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                thumbImage.compress(Bitmap.CompressFormat.JPEG, 100, bs);

                register_photo = findViewById(R.id.register_User_photo);
                Picasso.with(Register_Activity.this).load(photoUri).fit().into(register_photo);
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
                    Image_User_Send_Request imageRequest = new Image_User_Send_Request(""+userCode,imgurl, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(Register_Activity.this);
                    queue.add(imageRequest);

                    String uploadId = mDatabaseRef.push().getKey();
                    mDatabaseRef.child(uploadId).setValue(upload);
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Register_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
            Image_User_Send_Request imageRequest = new Image_User_Send_Request("" + userCode, "empty", responseListener);
            RequestQueue queue = Volley.newRequestQueue(Register_Activity.this);
            queue.add(imageRequest);
        }
    }
}