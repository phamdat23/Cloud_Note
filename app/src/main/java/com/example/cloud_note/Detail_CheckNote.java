package com.example.cloud_note;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.cloud_note.APIs.APINote;
import com.example.cloud_note.Adapter.AdapterCheckList;
import com.example.cloud_note.Model.GET.ModelCheckList;
import com.example.cloud_note.Model.GET.ModelGetCheckList;
import com.example.cloud_note.Model.GET.ModelReturn;
import com.example.cloud_note.Model.PATCH.ModelPutCheckList;
import com.example.cloud_note.Model.POST.ModelCheckListPost;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Detail_CheckNote extends AppCompatActivity {
    private CardView cardView;
    private ImageButton back;
    private EditText title;
    private ImageButton done;
    private String color_background;
    //Adapter and Recyler View
    private RecyclerView recyclerView;
    // dữ liệu intent gửi sang
    int idNote;
    float colorA;
    int colorR;
    int colorG;
    int colorB;
    AdapterCheckList adapterCheckList;
    private ImageButton btnDetailChecklistDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_check_note);
        Intent intent = getIntent();
        //Ánh xạ
        cardView = findViewById(R.id.cardview_detail_checklist);
        back = findViewById(R.id.back_from_detail_check_note);
        title = findViewById(R.id.title_detail_checklist);
        recyclerView = findViewById(R.id.recycler_checklist);
        btnDetailChecklistDone = (ImageButton) findViewById(R.id.btn_detail_checklist_done);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        getData(intent);
        String hex = ChuyenMau(colorA, colorR, colorG, colorB);
        cardView.setCardBackgroundColor(Color.parseColor(hex));
        ModelGetCheckList obj = new ModelGetCheckList();
        APINote.apiService.getNoteByIdTypeCheckList(idNote).enqueue(new Callback<ModelGetCheckList>() {
            @Override
            public void onResponse(Call<ModelGetCheckList> call, Response<ModelGetCheckList> response) {
                if (response.body() != null && response.isSuccessful()) {
                    obj.setModelTextNoteCheckList(response.body().getModelTextNoteCheckList());
                    title.setText(obj.getModelTextNoteCheckList().getTitle());
                    adapterCheckList = new AdapterCheckList(obj.getModelTextNoteCheckList().getData(), true);
                    recyclerView.setAdapter(adapterCheckList);
                    btnDetailChecklistDone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ModelPutCheckList update = new ModelPutCheckList();
                            update.setTitle(title.getText().toString());
                            if (cardView.getCardBackgroundColor().getDefaultColor() == Color.parseColor(hex)) {
                                Log.d("TAG", "onCreate:Color1: k thay đổi màu  ");
                                update.setColor(new com.example.cloud_note.Model.Color(colorA, colorB, colorG, colorR));
                            } else {
                                Log.d("TAG", "onCreate:Color2: thay đổi màu  ");
                                update.setColor(chuyenMauARGB(color_background));
                            }
                            List<ModelCheckListPost> checkListUpdate = new ArrayList<>();
                            for (ModelCheckList x : obj.getModelTextNoteCheckList().getData()) {
                                ModelCheckListPost item = new ModelCheckListPost();
                                item.setContent(x.getContent());
                                if (x.getStatus() == 1) {
                                    item.setStatus(true);
                                } else {
                                    item.setStatus(false);
                                }
                                checkListUpdate.add(item);
                            }
                            update.setData(checkListUpdate);
                            update.setType("checklist");
                            update.setPinned(0);
                            update.setReminAt("");
                            update.setDuaAt("");
                            update.setLock("");
                            pathCheckList(update);
                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<ModelGetCheckList> call, Throwable t) {
                Toast.makeText(Detail_CheckNote.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });


        Back();
    }

    private void pathCheckList(ModelPutCheckList obj) {
        ModelReturn r = new ModelReturn();
        APINote.apiService.patch_Check_list(idNote, obj).subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ModelReturn>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ModelReturn modelReturn) {
                        r.setStatus(modelReturn.getStatus());
                        r.setMessage(modelReturn.getMessage());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("TAG", "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        if (r.getStatus() == 200) {
                            Intent intent = new Intent(Detail_CheckNote.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    private void getData(Intent intent) {
        idNote = intent.getIntExtra("id", -1);
        colorA = intent.getFloatExtra("colorA", 0);
        colorR = intent.getIntExtra("colorR", 0);
        colorG = intent.getIntExtra("colorG", 0);
        colorB = intent.getIntExtra("colorB", 0);


    }

    private String ChuyenMau(float alpha, float red, float green, float blue) {
        // chuyển đổi giá trị của từng kênh màu sang giá trị thập lục phân
        String alphaHex = Integer.toHexString((int) alpha);
        String redHex = Integer.toHexString((int) red);
        String greenHex = Integer.toHexString((int) green);
        String blueHex = Integer.toHexString((int) blue);
// ghép các giá trị thập lục phân lại với nhau theo thứ tự ARGB
        String hex = "#" + redHex + greenHex + blueHex;
        Log.d("TAG", "ChuyenMau: " + hex);
        return hex;
    }

    public com.example.cloud_note.Model.Color chuyenMauARGB(String hexColor) {
        int red = Integer.parseInt(hexColor.substring(1, 3), 16);
        int green = Integer.parseInt(hexColor.substring(3, 5), 16);
        int blue = Integer.parseInt(hexColor.substring(5, 7), 16);
        com.example.cloud_note.Model.Color color = new com.example.cloud_note.Model.Color();
        color.setA((float) 0.87);
        color.setB(blue);
        color.setG(green);
        color.setR(red);
        return color;
    }

    public void Back() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}