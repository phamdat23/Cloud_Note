package com.example.cloud_note.APIs;

import com.example.cloud_note.Model.LoginModel;
import com.example.cloud_note.Model.POST.LoginReq;
import com.example.cloud_note.Model.GET.ModelGetCheckList;
import com.example.cloud_note.Model.GET.ModelGetNoteText;
import com.example.cloud_note.Model.GET.ModelReturn;
import com.example.cloud_note.Model.POST.ModelTextNoteCheckListPost;
import com.example.cloud_note.Model.POST.ModelTextNotePost;
import com.example.cloud_note.Model.GET.Model_Notes;
import com.example.cloud_note.Model.POST.RegiterReq;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APINote {
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
    APINote apiService = new Retrofit.Builder()
            // là dumain cảu api
            .baseUrl("http://14.225.7.179:18015/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(okbilder.build())
            .build().create(APINote.class);


    @POST("login")

    @Headers({
            "Content-type: Application/json"
    })
    Observable<LoginModel> login(@Body LoginReq loginReq);


    @POST("regiter")
    @Headers({
            "Content-type: Application/json"
    })
    Observable<ModelReturn> Regiter(@Body RegiterReq regiterReq);

    @GET("notes/{id}")
    @Headers({
            "Content-type: Application/json"
    })
    Observable<Model_Notes> getListNoteByUser(@Path("id") int id);

    @POST("notes/{id}")
    @Headers({
            "Content-type: Application/json"
    })
    Observable<ModelReturn> post_text_note(@Path("id") int id, @Body ModelTextNotePost modelTextNotePost);

    @POST("notes/{id}")
    @Headers({
            "Content-type: Application/json"
    })
    Observable<ModelReturn> post_Check_list(@Path("id") int id, @Body ModelTextNoteCheckListPost modelTextNoteCheckList);

    @PATCH("notes/{id}")
    @Headers({
            "Content-type: Application/json"
    })
    Observable<ModelReturn> patch_text_note(@Path("id") int id, @Body ModelTextNotePost modelTextNotePost);

    @PATCH("notes/{id}")
    @Headers({
            "Content-type: Application/json"
    })
    Observable<ModelReturn> patch_Check_list(@Path("id") int id, @Body ModelTextNoteCheckListPost modelTextNoteCheckList);

    @GET("only/{id}")
    @Headers({
            "Content-type: Application/json"
    })
    Call<ModelGetNoteText> getNoteByIdTypeText(@Path("id") int id);

    @GET("only/{id}")
    @Headers({
            "Content-type: Application/json"
    })
    Call<ModelGetCheckList> getNoteByIdTypeCheckList(@Path("id") int id);

    @DELETE("trunc-notes/{id}")
    Call<ModelReturn> deleteNote(@Path("id") int id);

    @DELETE("notes/{id}")
    Call<ModelReturn> moveToTrash(@Path("id") int id);

    @GET("trash/{id}")
    Observable<Model_Notes> getListTrash(@Path("id") int id);
}
