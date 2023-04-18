package com.example.cloud_note;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Detail_Note extends AppCompatActivity {
    private ImageButton back;
    private ImageButton done;
    private EditText title;
    private EditText content;
    private ImageButton menu;
    private CardView cardView;

    private String color_background;


    //Luu tru gia tri duoc gui boi bundle
    private String title_text;
    private String content_text;
    private int id;
    private String date;
    //Database


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_note);
        //Ánh xạ
        cardView = findViewById(R.id.cardview_detail_note);
        back = findViewById(R.id.back_from_detail_text_note);
        done = findViewById(R.id.btn_done_update_note);
        title = findViewById(R.id.detail_title_name);
        content = findViewById(R.id.detail_content_text);
        menu = findViewById(R.id.menu_detail_text_note);
        getData();
        Back();
        Update();
        OpenMenu();
    }
    public void getData(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            title_text = bundle.getString("Title_Note");
            content_text = bundle.getString("Content_Note");
            id = bundle.getInt("Id_Note");
            date = bundle.getString("Date_Note");
            title.setText(title_text);
            content.setText(content_text);
            color_background = bundle.getString("Color_Note");
            cardView.setCardBackgroundColor(Color.parseColor(color_background));
        }
    }
    public void Update(){
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title_values = title.getText().toString();
                String content_values = content.getText().toString();


                Toast.makeText(Detail_Note.this, "Success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Detail_Note.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public void Back(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Detail_Note.this, MainActivity.class);
                startActivity(intent);
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