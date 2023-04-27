package com.example.cloud_note.APIs;

import com.example.cloud_note.Model.POST.ModelPostImage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

public interface API_UploadImage {
    // khởi tạo Gson
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();
    // khởi tạo retrofit
    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    OkHttpClient.Builder okbilder = new OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(httpLoggingInterceptor);
    API_UploadImage  api = new Retrofit.Builder()
            // là dumain cảu api
            .baseUrl("https://api.imgbb.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(okbilder.build())
            .build().create(API_UploadImage.class);

    @Headers("Content-Type: multipart/form-data")
    @POST("1/upload?expiration=600&key=6374d7c9cfa9f0cb372098bdf76d806e")
    @Multipart
    Call<ModelPostImage> upload(@Body RequestBody reqestBody);

}