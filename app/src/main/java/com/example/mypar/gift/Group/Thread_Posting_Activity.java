package com.example.mypar.gift.Group;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mypar.gift.Activity.Register_Activity;
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
import com.example.mypar.gift.Structure.UserData;
import com.example.mypar.gift.server.Thread_Info_Request;
import com.example.mypar.gift.server.Thread_Push_Request;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;

public class Thread_Posting_Activity extends AppCompatActivity {
    final static int PICK_PDF_CODE = 2342;

    private EditText title, article;
    private String usercode;
    private UserData userdata;
    private Main_Group_List group_data;
    private ImageView btn1;
    private TextView imgView, fileView;
    private String[] permissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA};
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    public static final int RequestPermissionCode = 1;
    private static int file_flag= 0;
    private Bitmap bitmap;

    private Uri photoUri;

    private ImageView ImageViewHolder;
    private ProgressDialog progressDialog;
    boolean check = true;

    //private EditText imageName;

    String ImageNameFieldOnServer = "image_name";
    String GetImageNameFromEditText;
    String ImagePathFieldOnServer = "image_path";
    String imageEncoded;

    String ImageUploadPathOnSever = "http://rshak8912.cafe24.com/board/imageupload.php";

    String ImgPath = "http://rshak8912.cafe24.com/image/";

    private boolean ImgFlag = false;
    private int articleCode = 0;

    StorageReference mStorageReference;
    DatabaseReference mDatabaseReference;
    Uri PDFData = null;
    ArrayList<Uri> PdfDatas = new ArrayList<>();
    ArrayList<String> filenames = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_posting_);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);


        NetworkUtil.setNetworkPolicy();

        Intent intent = getIntent();
        group_data = intent.getParcelableExtra("Group_Name");
        userdata = intent.getParcelableExtra("User_Code");

        usercode = String.valueOf(userdata.getUsercode());
        title = findViewById(R.id.title);
        article = findViewById(R.id.article);

        btn1 = findViewById(R.id.send);

        imgView = findViewById(R.id.thread_posting_camera);
        imgView.setClickable(true);
        fileView = (TextView) findViewById(R.id.thread_posting_file);

        ImageViewHolder = (ImageView) findViewById(R.id.Artimage);
        PdfDatas.clear();
        filenames.clear();


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.getText().toString().replace(" ", "").equals("") || article.getText().toString().replace(" ", "").equals(""))
                    Toast.makeText(getApplication(), "Fill in the blank", Toast.LENGTH_SHORT).show();
                else {
                    try {

                        Thread_Info_Request request = new Thread_Info_Request("http://rshak8912.cafe24.com/boardlist.php");

                        Calendar m = Calendar.getInstance();
                        int year = m.get(Calendar.YEAR);
                        int month = m.get(Calendar.MONTH) + 1;
                        int day = m.get(Calendar.DATE);
                        int hour = m.get(Calendar.HOUR_OF_DAY);
                        int min = m.get(Calendar.MINUTE);

                        String date = year + "." + month + "." + day + " " + hour + ":" + min;

                        getCode();

                        if (!ImgFlag) {
                            String result = request.PhPtest(String.valueOf(title.getText()), String.valueOf(article.getText()), String.valueOf(usercode), String.valueOf("" + group_data.getGroup_Code()), date, "", "", file_flag);

                            if (result.equals("success")) {
                                Push(group_data);

                                if (PDFData != null)
                                    //uploadFile(PDFData);
                                    uploadFile2();
                                finish();
                            } else {
                                Toast.makeText(getApplication(), "Thread post fail..", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            String result = request.PhPtest(String.valueOf(title.getText()), String.valueOf(article.getText()), String.valueOf(usercode), String.valueOf("" + group_data.getGroup_Code()), date, String.valueOf(articleCode), ImgPath + String.valueOf(articleCode + ".png"), file_flag);
                            //Toast.makeText(Posting.this, "yes", Toast.LENGTH_LONG).show();
                            if (result.equals("success")) {
                                Push(group_data);
                                GetImageNameFromEditText = String.valueOf(articleCode);
                                ImageEncode();
                                //데이터없는경우

                                if (PDFData != null)
                                    //uploadFile(PDFData);
                                    uploadFile2();

                            } else {
                                Toast.makeText(getApplication(), "Thread post fail", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                }
            }

        });


        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoMenu();
                //Toast.makeText(Posting.this, "Testing",Toast.LENGTH_SHORT).show();
            }
        });
        fileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPDF();
            }
        });
    }

    public void getCode() {
        try {

            Thread_Info_Request request = new Thread_Info_Request("http://rshak8912.cafe24.com/board/getCode.php");

            String result = request.PhPtest5();
            if (result == "") articleCode = 0;
            else
                articleCode = Integer.parseInt(result);
            articleCode += 1;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void EnableRuntimePermissionToAccessCamera() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(Thread_Posting_Activity.this,
                permissions[2])) {

        } else {
            ActivityCompat.requestPermissions(Thread_Posting_Activity.this, new String[]{permissions[2]}, RequestPermissionCode);
        }
    }

    public String filename;


    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    //Toast.makeText(Posting.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(Thread_Posting_Activity.this, "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(6);
    }

    public void photoMenu() {

        EnableRuntimePermissionToAccessCamera();

        final CharSequence[] items = {"Camera", "Album", "cancel"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose one")        // 제목 설정
                .setItems(items, new DialogInterface.OnClickListener() {    // 목록 클릭시 설정
                    public void onClick(DialogInterface dialog, int index) {
                        if (index == 0) {
                            Toast.makeText(Thread_Posting_Activity.this, items[index] + " is selected", Toast.LENGTH_LONG).show();
                            doTakePhotoAction();
                        } else if (index == 1) {
                            Toast.makeText(Thread_Posting_Activity.this, items[index] + " is selected", Toast.LENGTH_LONG).show();
                            doTakeAlbumAction();
                        } else {
                            dialog.cancel();
                        }
                    }
                });
        AlertDialog dialog = builder.create();    // 알림창 객체 생성
        dialog.show();    // 알림창 띄우기
    }

    public void doTakePhotoAction() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {

        }
        if (photoFile != null) {
            photoUri = FileProvider.getUriForFile(Thread_Posting_Activity.this,
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
    public void doTakeAlbumAction() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(Posting.this, " Image test " + requestCode + " " + resultCode + " " + data.getData(), Toast.LENGTH_SHORT).show();

        if (resultCode == RESULT_OK) {
            ImageViewHolder.setVisibility(View.VISIBLE);

            if (requestCode == PICK_FROM_CAMERA) {
                MediaScannerConnection.scanFile(Thread_Posting_Activity.this, new String[]{photoUri.getPath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
                try {
                    Bitmap bitmap_ = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);

                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);

                    bitmap = Bitmap.createBitmap(bitmap_, 0, 0, bitmap_.getWidth(), bitmap_.getHeight(), matrix, true);
                    bitmap_.recycle();
                    ImageViewHolder.setImageBitmap(bitmap);
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage().toString());
                }

                ImgFlag = true; // set Flag true - 이미지 있다고 설정
            } else if (requestCode == PICK_FROM_ALBUM) {
                if (data != null) {
                    Log.e("Test", "result = " + data);

                    Uri thumbnail = data.getData();

                    if (thumbnail != null) {
                        //ImageViewHolder.setImageURI(thumbnail);
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), thumbnail);
                            ImageViewHolder.setImageBitmap(bitmap);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                ImgFlag = true; // set Flag true - 이미지 있다고 설정
            } else if (requestCode == PICK_PDF_CODE && data != null && data.getData() != null) {
                if (data.getData() != null) {
                    //uploading the file
                    filenames.add(getfileName(data.getData()));
                    Toast.makeText(getApplicationContext(), getfileName(data.getData()), Toast.LENGTH_LONG).show();
                    PDFData = data.getData();
                    PdfDatas.add(data.getData());
                    file_flag = 1;
                } else {
                    Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            //finish();
        }
    }

    private String getfileName(Uri uri) {
        String[] projection = {MediaStore.Images.ImageColumns.DISPLAY_NAME};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    private void uploadFile2() {

        for (int i = 0; i < PdfDatas.size(); i++) {
            Uri data = PdfDatas.get(i);
            final String filename = getfileName(data);


            StorageReference sRef = mStorageReference.child("pdf/" + articleCode + "/" + filename);
            sRef.putFile(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("VisibleForTests")
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // Toast.makeText(Thread_Posting_Activity.this, "File Uploaded Successfully", Toast.LENGTH_SHORT).show();

                            Pdf pdf = new Pdf(filename, taskSnapshot.getDownloadUrl().toString());
                            mDatabaseReference.child("" + articleCode).child(mDatabaseReference.push().getKey()).setValue(pdf);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });


        }

    }

    public void ImageEncode() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);

        byte[] b = byteArrayOutputStream.toByteArray();

        imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        ImageUploadToServerFunction();
    }

    // Upload captured image online on server function.
    public void ImageUploadToServerFunction() {
        class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Showing progress dialog at image upload time.
                progressDialog = ProgressDialog.show(Thread_Posting_Activity.this, "Image is Uploading", "Please Wait", false, false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                // Dismiss the progress dialog after done uploading.
                progressDialog.dismiss();

                // Printing uploading success message coming from server on android app.
                //Toast.makeText(Thread_Posting_Activity.this, string1, Toast.LENGTH_LONG).show();
                // Setting image as transparent after done uploading.
                //ImageViewHolder.setImageResource(android.R.color.transparent);
                finish();
            }

            @Override
            protected String doInBackground(Void... params) {

                Log.d("Image", "doInBackground");
                ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String, String> HashMapParams = new HashMap<String, String>();

                HashMapParams.put(ImageNameFieldOnServer, GetImageNameFromEditText);
                // 첫번째는 "image_name" 두번째는 실제 title 이미지이름

                HashMapParams.put(ImagePathFieldOnServer, imageEncoded);
                // 첫번째는 "image_path" 두번째는 actural data

                String FinalData = imageProcessClass.ImageHttpRequest(ImageUploadPathOnSever, HashMapParams);
                Log.d("Image", "Final data : " + FinalData);
                // imageHttpRequest 를 실행하여 서버주소와 , haspmap 을 인자로 전달하여 return 값을 FinalData 로 받는다
                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();

        AsyncTaskUploadClassOBJ.execute();

    }

    public class ImageProcessClass {

        public String ImageHttpRequest(String requestURL, HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {
                URL url;
                HttpURLConnection httpURLConnectionObject;
                OutputStream OutPutStream;
                BufferedWriter bufferedWriterObject;
                BufferedReader bufferedReaderObject;
                int RC;

                url = new URL(requestURL);

                httpURLConnectionObject = (HttpURLConnection) url.openConnection();


                httpURLConnectionObject.setRequestMethod("POST");
                httpURLConnectionObject.setDoInput(true);
                httpURLConnectionObject.setDoOutput(true);

                OutPutStream = httpURLConnectionObject.getOutputStream();
                String postData = bufferedWriterDataFN(PData);
                Log.d("postData : ", "" + postData);
                OutPutStream.write(postData.getBytes("UTF-8"));
                OutPutStream.flush();
                OutPutStream.close();

                RC = httpURLConnectionObject.getResponseCode();
                Log.d("RC :", "" + RC);
                if (RC == 413) {
                    Toast.makeText(getApplicationContext(), "Image size is too large. Image upload fail.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReaderObject.readLine()) != null) {

                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            StringBuilder stringBuilderObject;

            stringBuilderObject = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {

                if (check)
                    check = false;
                else
                    stringBuilderObject.append("&");

                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));
                stringBuilderObject.append("=");
                stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilderObject.toString();
        }

    }

    void Push(final Main_Group_List group_data) {
        if(userdata.getUsercode()!=group_data.getGroup_Admin()){
            return;
        }
        Response.Listener<String> responseListener_push = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    //Toast.makeText(getApplication(),"asd",Toast.LENGTH_SHORT);
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
                        //Toast.makeText(getApplication(),getToken,Toast.LENGTH_LONG).show();
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
        RequestQueue queue_push = Volley.newRequestQueue(Thread_Posting_Activity.this);
        queue_push.add(pushRequest);


    }


    //this function will get the pdf from the storage
    private void getPDF() {
        //for greater than lolipop versions we need the permissions asked on runtime
        //so if the permission is not available user will go to the screen to allow storage permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            return;
        }

        //creating an intent for file chooser
        Intent intent = new Intent();

        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_CODE);
    }


}