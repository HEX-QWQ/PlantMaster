package com.example.plant.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.plant.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class camera extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private Button mTakePhoto, mChooseFromAlbum;
    private ImageView mPicture;
    private static final String PERMISSION_WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;

    private static final int REQUEST_PERMISSION_CODE = 267;
    private static final int TAKE_PHOTO = 189;
    private static final int CHOOSE_PHOTO = 385;
    private static final String FILE_PROVIDER_AUTHORITY = "com.example.plant.fileprovider";
    private Uri mImageUri, mImageUriFromFile;
    private File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        /*申请读取存储的权限*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(PERMISSION_WRITE_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(PERMISSION_CAMERA) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this,
                        new String[]{PERMISSION_WRITE_STORAGE, PERMISSION_CAMERA},
                        REQUEST_PERMISSION_CODE);
            }
        }

        mPicture = (ImageView) findViewById(R.id.iv_picture);
        Intent intent = getIntent();
        String value = intent.getStringExtra("args");
        if(value.equals("shot")){
            takePhoto();
        }
        else{
            openAlbum();
        }
    }
    void getType(String base64) {
//        Log.e("TAG","调用");
        Log.e("TAG", base64);
        String url = "http://10.68.134.109:5000/identify";
        String json = "{\"image_base64\": \"" + base64 + "\"}";
//        OkHttpClient client = new OkHttpClient();

        OkHttpClient client = new OkHttpClient.Builder()
                .callTimeout(200, TimeUnit.SECONDS)
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        Log.e("JSON",json);
        RequestBody requestBody = RequestBody.create(mediaType, json);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("TAG", "onFailure: " + e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonData = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(jsonData);
                    JSONArray resultArray = jsonObject.getJSONArray("result");
                    JSONObject resultObject = resultArray.getJSONObject(0);
                    String encodedName = resultObject.getString("name");
                    String name = URLDecoder.decode(encodedName, "UTF-8");
                    Log.e("TAG",name);
                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    /**
     * 打开相册
     */
    private void openAlbum() {
        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
        openAlbumIntent.setType("image/*");
        startActivityForResult(openAlbumIntent, CHOOSE_PHOTO);//打开相册
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//打开相机的Intent
        if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {//这句作用是如果没有相机则该应用不会闪退，要是不加这句则当系统没有相机应用的时候该应用会闪退
            Log.e("TAG","喵喵喵");
            imageFile = createImageFile();//创建用来保存照片的文件
            mImageUriFromFile = Uri.fromFile(imageFile);
            Log.i(TAG, "takePhoto: uriFromFile " + mImageUriFromFile);
            if (imageFile != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    /*7.0以上要通过FileProvider将File转化为Uri*/
                    mImageUri = FileProvider.getUriForFile(this, FILE_PROVIDER_AUTHORITY, imageFile);
                } else {
                    /*7.0以下则直接使用Uri的fromFile方法将File转化为Uri*/
                    mImageUri = Uri.fromFile(imageFile);
                }
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);//将用于输出的文件Uri传递给相机
                startActivityForResult(takePhotoIntent, TAKE_PHOTO);//打开相机
            }
        }
    }

    /**
     * 创建用来存储图片的文件，以时间来命名就不会产生命名冲突
     *
     * @return 创建的图片文件
     */
    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageFile = null;
        try {
            imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }

    /*申请权限的回调*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "onRequestPermissionsResult: permission granted");
        } else {
            Log.i(TAG, "onRequestPermissionsResult: permission denied");
            Toast.makeText(this, "You Denied Permission", Toast.LENGTH_SHORT).show();
        }
    }

    /*相机或者相册返回来的数据*/
    private Bitmap compressBitmap(Bitmap bitmap, int maxSize) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float bitmapRatio = (float) width / (float) height;

        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    private String bitmapToBase64(Bitmap bitmap) {
        bitmap = compressBitmap(bitmap,600);//将图片进行压缩
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        // 从Uri获取Bitmap
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mImageUri));
                        String base64Image = bitmapToBase64(bitmap);
                        Log.e("BASE",base64Image);
                        getType(base64Image);
                        // 在这里可以使用base64Image进行其他操作，比如上传服务器等
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (data == null) {//如果没有选取照片，则直接返回
                    return;
                }
                Log.i(TAG, "onActivityResult: ImageUriFromAlbum: " + data.getData());
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        handleImageOnKitKat(data);//4.4之后图片解析
                    } else {
                        handleImageBeforeKitKat(data);//4.4之前图片解析
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 4.4版本以下对返回的图片Uri的处理：
     * 就是从返回的Intent中取出图片Uri，直接显示就好
     * @param data 调用系统相册之后返回的Uri
     */
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    /**
     * 4.4版本以上对返回的图片Uri的处理：
     * 返回的Uri是经过封装的，要进行处理才能得到真实路径
     * @param data 调用系统相册之后返回的Uri
     */
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的Uri，则提供document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的uri，则进行普通处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的uri，则直接获取路径
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    /**
     * 将imagePath指定的图片显示到ImageView上
     */
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            mPicture.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 将Uri转化为路径
     * @param uri 要转化的Uri
     * @param selection 4.4之后需要解析Uri，因此需要该参数
     * @return 转化之后的路径
     */
    @SuppressLint("Range")
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 将拍的照片添加到相册
     *
     * @param uri 拍的照片的Uri
     */
    private void galleryAddPic(Uri uri) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(uri);
        sendBroadcast(mediaScanIntent);
    }
}