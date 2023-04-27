package com.example.cloud_note;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cloud_note.APIs.APINote;
import com.example.cloud_note.Model.GET.ModelGetNoteText;
import com.example.cloud_note.Model.GET.ModelReturn;
import com.example.cloud_note.Model.PATCH.ModelPutTextNote;
import com.example.cloud_note.Model.POST.ModelTextNotePost;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Detail_Note extends AppCompatActivity {
    private ImageButton back;
    private ImageButton done;
    private EditText title;
    private EditText content;
    private ImageButton menu;
    private CardView cardView;

    private String color_background;
    private TextView tvDateCreate;
    private ImageView imgDateCreate;
    private TextView tvTimeCreate;
    private ImageView imgTimeCreate;

    //Luu tru gia tri duoc gui boi bundle

    int idNote;
    float colorA;
    int colorR;
    int colorG;
    int colorB;


    //Database


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_note);
        Intent intent = getIntent();
        //Ánh xạ
        cardView = findViewById(R.id.cardview_detail_note);
        back = findViewById(R.id.back_from_detail_text_note);
        done = findViewById(R.id.btn_done_update_note);
        title = findViewById(R.id.detail_title_name);
        content = findViewById(R.id.detail_content_text);
        menu = findViewById(R.id.menu_detail_text_note);


        tvDateCreate = (TextView) findViewById(R.id.tv_dateCreate);
        imgDateCreate = (ImageView) findViewById(R.id.img_dateCreate);
        tvTimeCreate = (TextView) findViewById(R.id.tv_timeCreate);
        imgTimeCreate = (ImageView) findViewById(R.id.img_timeCreate);

        getData(intent);

        APINote.apiService.getNoteByIdTypeText(idNote).enqueue(new Callback<ModelGetNoteText>() {
            @Override
            public void onResponse(Call<ModelGetNoteText> call, Response<ModelGetNoteText> response) {
                if(response.isSuccessful()&response.body()!=null){
                    ModelGetNoteText obj = response.body();
                    ModelPutTextNote update = new ModelPutTextNote();
                    title.setText(obj.getModelTextNote().getTitle());
                    content.setText(obj.getModelTextNote().getData());
                    String hex = ChuyenMau(colorA, colorR, colorG, colorB);
                    cardView.setCardBackgroundColor(Color.parseColor(hex));
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                           // ModelTextNotePost update = new ModelTextNotePost();
                            update.setData(content.getText().toString());
                            update.setTitle(title.getText().toString());
                            update.setType(obj.getModelTextNote().getType());
                            if(cardView.getCardBackgroundColor().getDefaultColor()==Color.parseColor(hex)){
                                Log.d("TAG", "onCreate:Color1: k thay đổi màu  ");
                                update.setColor(new com.example.cloud_note.Model.Color(colorA, colorB, colorG, colorR));
                            }else{
                                Log.d("TAG", "onCreate:Color2: thay đổi màu  ");
                                update.setColor(ChuyenMauARGB(color_background));
                            }

                            update.setLock("");
                            update.setReminAt("");
                            update.setPinned(0);
                            if(tvDateCreate.getText().toString()==""&&tvTimeCreate.getText().toString()==""){
                                if(obj.getModelTextNote().getDuaAt()==""){
                                    update.setDuaAt("");
                                }else{
                                    update.setDuaAt(obj.getModelTextNote().getDuaAt());
                                }
                            }else{
                                update.setDuaAt(tvDateCreate.getText().toString()+" "+tvTimeCreate.getText().toString());
                            }
                            updateNodeTextNote(update, idNote);
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<ModelGetNoteText> call, Throwable t) {
                Log.e("TAG", "onFailure: "+t.getMessage() );
            }
        });



        Back();

        OpenMenu();
    }
    private void getData(Intent intent){
        idNote = intent.getIntExtra("id", -1);
        colorA = intent.getFloatExtra("colorA",0);
        colorR = intent.getIntExtra("colorR", 0);
        colorG = intent.getIntExtra("colorG", 0);
        colorB = intent.getIntExtra("colorB", 0);


    }
    private void updateNodeTextNote(ModelPutTextNote obj, int id) {
        ModelReturn modelR = new ModelReturn();
        APINote.apiService.patch_text_note(id, obj)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ModelReturn>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull ModelReturn modelReturn) {
                        modelR.setMessage(modelReturn.getMessage());
                        modelR.setStatus(modelR.getStatus());
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.e("TAG", "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(Detail_Note.this, modelR.getMessage() , Toast.LENGTH_SHORT).show();
                        onBackPressed();
                        if (modelR.getStatus() == 200) {

                        }
                    }
                });
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
    public com.example.cloud_note.Model.Color ChuyenMauARGB (String hexColor){
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


    public void Back(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    public void OpenMenu(){
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Menu_Dialog(Gravity.BOTTOM);
            }
        });
    }
    public void Menu_Dialog(int gravity){
        final Dialog dialog = new Dialog(this);
        //Truyền layout cho dialog.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_select_color);

        //Xác định vị trí cho dialog

        Window window = dialog.getWindow();
        if(window == null){

        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        if(Gravity.BOTTOM == gravity){
            dialog.setCancelable(true);
        }else {
            dialog.setCancelable(false);
        }
        //Ánh xạ
        ImageButton red = dialog.findViewById(R.id.color_red);
        ImageButton orange = dialog.findViewById(R.id.color_orange);
        ImageButton yellow = dialog.findViewById(R.id.color_yellow);
        ImageButton green1 = dialog.findViewById(R.id.color_green1);
        ImageButton green2 = dialog.findViewById(R.id.color_green2);
        ImageButton mint = dialog.findViewById(R.id.color_mint);
        ImageButton blue = dialog.findViewById(R.id.color_blue);
        ImageButton purple = dialog.findViewById(R.id.color_purple);
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#FF7D7D";
                cardView.setCardBackgroundColor(Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        orange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#FFBC7D";
                cardView.setCardBackgroundColor(Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#FAE28C";
                cardView.setCardBackgroundColor(Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        green1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#D3EF82";
                cardView.setCardBackgroundColor(Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        green2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#A5EF82";
                cardView.setCardBackgroundColor(Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        mint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#82EFBB";
                cardView.setCardBackgroundColor(Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#82C8EF";
                cardView.setCardBackgroundColor(Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        purple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#8293EF";
                cardView.setCardBackgroundColor(Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        dialog.show();
    }
}