package com.example.cloud_note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager2.widget.ViewPager2;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.cloud_note.Adapter.Fragment_Adapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class    MainActivity extends AppCompatActivity {
    private ViewPager2 viewpager2;
    private Fragment_Adapter fragment_adapter;
    public BottomNavigationView bottomNavigationView;
    private ImageButton btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottombar);
        viewpager2 = findViewById(R.id.viewpager);
        btnAdd  = findViewById(R.id.btn_add);

        init_Viewpager();
        SetUpViewpager();
        add();
    }
    private void init_Viewpager(){
        fragment_adapter = new Fragment_Adapter(MainActivity.this);
        viewpager2.setAdapter(fragment_adapter);
    }
    private void SetUpViewpager(){
       viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
           @Override
           public void onPageSelected(int position) {
               super.onPageSelected(position);
               switch (position){
                   case 0:
                       bottomNavigationView.getMenu().findItem(R.id.note).setChecked(true);
                       break;
                   case 1:
                       bottomNavigationView.getMenu().findItem(R.id.calendar).setChecked(true);
                       break;
                   case 2:
                       bottomNavigationView.getMenu().findItem(R.id.deleted).setChecked(true);
                       break;
                   default:
                       bottomNavigationView.getMenu().findItem(R.id.note).setChecked(true);
                       break;
               }
           }
       });
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.note:
                        viewpager2.setCurrentItem(0);
                        return true;
                    case R.id.calendar:
                        viewpager2.setCurrentItem(1);
                        return true;
                    case R.id.deleted:
                        viewpager2.setCurrentItem(2);
                        return true;
                }
                return false;
            }
        });
    }
    public void add(){
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenDialog(Gravity.BOTTOM);
            }
        });
    }
    public void OpenDialog(int gravity){
        final Dialog dialog = new Dialog(this);
        //Truyền layout cho dialog.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pop_up_layout);

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
        Button button_checkList = dialog.findViewById(R.id.btn_new_checked_list);
        Button button_note = dialog.findViewById(R.id.btn_new_text_note);
        Button button_cancel = dialog.findViewById(R.id.btn_Cancel);
        button_checkList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CheckedList_Activity.class);
                startActivity(intent);

            }
        });
        button_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TextNoteActvity.class);
                startActivity(intent);
            }
        });
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               dialog.cancel();
            }
        });
        dialog.show();
    }
}