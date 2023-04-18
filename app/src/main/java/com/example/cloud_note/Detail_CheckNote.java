package com.example.cloud_note;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.cloud_note.Adapter.Adapter_Checked_List;

import java.util.ArrayList;

public class Detail_CheckNote extends AppCompatActivity {
    private CardView cardView;
    private ImageButton back;
    private EditText title;
    private ImageButton done;
    private String color_background;

    //Adapter and Recyler View
    private RecyclerView recyclerView;
    private Adapter_Checked_List adapter;
    private ArrayList<String> checklist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_check_note);
        //Ánh xạ
        cardView = findViewById(R.id.cardview_detail_checklist);
        back = findViewById(R.id.back_from_detail_check_note);
        title = findViewById(R.id.title_detail_checklist);
        recyclerView = findViewById(R.id.recycler_checklist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        getData();
        Back();
    }
    public void getData(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            title.setText(bundle.getString("CheckedList_Title"));
            //content text
            String s[] = bundle.getString("CheckedList_Content").split("\n");
            for(int i = 0 ; i < s.length; i++){
                checklist.add(s[i]);
            }
            adapter = new Adapter_Checked_List(this,checklist);
            recyclerView.setAdapter(adapter);
            //background color
            color_background = bundle.getString("Color_Note_Checklist");
            cardView.setCardBackgroundColor(Color.parseColor(color_background));
        }
    }
    public void Back(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Detail_CheckNote.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}