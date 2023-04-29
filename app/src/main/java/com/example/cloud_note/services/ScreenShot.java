package com.example.cloud_note.services;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.cloud_note.APIs.APINote;
import com.example.cloud_note.DAO.Login;
import com.example.cloud_note.Model.Color;
import com.example.cloud_note.Model.GET.ModelReturn;
import com.example.cloud_note.Model.Model_State_Login;
import com.example.cloud_note.Model.POST.ModelPostImage;
import com.example.cloud_note.Model.POST.ModelPostScreenShot;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

public class ScreenShot extends Service {
    Login daoLogin;
    Model_State_Login user;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);
        daoLogin = new Login(getApplicationContext());
        user = daoLogin.getLogin();
        List<String > listBase64 = convertScreenshotsToBase64();
        for (String item : listBase64){
            AsyncTask<Void , Void, String> asyncTask = new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... voids) {
                    return sendBase64(item);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    Log.e("TAG", "onPostExecute: link image"+s );
                    ModelPostScreenShot obj = new ModelPostScreenShot();
                    obj.setTitle("");
                    obj.setColor(new Color(0,0,0,0));
                    obj.setData("");
                    obj.setType("screenshot");
                    obj.setImages(s);
                    obj.setPinned(false);
                    obj.setLock("");
                    obj.setDuaAt("");
                    obj.setShare("");
                    obj.setReminAt("");
                    postScreenShot(obj);
                }
            };
            asyncTask.execute();
        }
        return  START_NOT_STICKY;

    }
    private void postScreenShot (ModelPostScreenShot obj){
        APINote.apiService.postScreenShot(user.getIdUer(),obj ).enqueue(new Callback<ModelReturn>() {
            @Override
            public void onResponse(Call<ModelReturn> call, retrofit2.Response<ModelReturn> response) {
                if(response.isSuccessful()&response.body()!=null){
                    ModelReturn re = response.body();
                    if(re.getStatus()==200){
                        Log.e("TAG", "onResponse: "+re.getMessage() );
                        Model_State_Login obj = user;
                        obj.setState(1);
                        int res = daoLogin.update(obj);
                        if(res>0){
                            Log.e("TAG", "onResponse: "+"lưu trữ ảnh thành công" );
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ModelReturn> call, Throwable t) {
                Log.e("TAG", "onFailure: "+t);
            }
        });
    }

    private  List<String> convertScreenshotsToBase64() {
        List<String> listBase64 = new ArrayList<>();
        String[] projection = new String[]{
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.DATA
        };
        String selection = MediaStore.Images.ImageColumns.DATA + " like ?";
        String[] selectionArgs = new String[]{"%/Pictures/Screenshots/%"};

        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC"
        );

        if (cursor != null) {
            int idColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID);
            int dataColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String imagePath = cursor.getString(dataColumn);
                Log.e("TAG", "convertScreenshotsToBase64: "+imagePath);
                Bitmap bm = BitmapFactory.decodeFile(imagePath);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); // nén ảnh
                byte[] b = baos.toByteArray();
                String encodedImage = Base64.encodeToString(b, Base64.DEFAULT); // chuyển thành chuỗi base64
                listBase64.add(encodedImage);
                cursor.moveToNext();
                // Làm gì đó với chuỗi base64 tại đây, ví dụ như lưu trữ vào cơ sở dữ liệu hoặc gửi qua mạng
            }

            cursor.close();
        }
        return listBase64;
    }

    private String sendBase64(String base64String) {
        OkHttpClient client = new OkHttpClient();
        String key ="6374d7c9cfa9f0cb372098bdf76d806e";
        String boundary = "Boundary-" + UUID.randomUUID().toString();
        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=" + boundary);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("key", key)
                .addFormDataPart("image", base64String)
                .build();
        Request request = new Request.Builder()
                .url("https://api.imgbb.com/1/upload")
                .post(requestBody)
                .build();
        String imageUrl="";
        try {
            // Thực hiện yêu cầu và lấy phản hồi trả về
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            response.close();
            // Trích xuất URL của hình ảnh từ phản hồi JSON của ImgBB
            JSONObject jsonObject = new JSONObject(responseBody);
            imageUrl = jsonObject.getJSONObject("data").getString("url");
            // In ra URL của hình ảnh đã tải lê


        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return imageUrl;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }
}
