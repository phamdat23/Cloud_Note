package com.example.cloud_note;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.cloud_note.APIs.APINote;
import com.example.cloud_note.Adapter.AdapterCheckList;
import com.example.cloud_note.Model.ModelCheckList;
import com.example.cloud_note.Model.GET.ModelGetCheckList;

import java.util.List;

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
    String titleHeader;
    List<ModelCheckList> data;
    int noteDone;
    int idNote;
    float colorA;
    int colorR;
    int colorG;
    int colorB;
    String createAt;
    AdapterCheckList adapterCheckList;

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
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        getData(intent);
        ModelGetCheckList obj= new ModelGetCheckList();
        APINote.apiService.getNoteByIdTypeCheckList(idNote).enqueue(new Callback<ModelGetCheckList>() {
            @Override
            public void onResponse(Call<ModelGetCheckList> call, Response<ModelGetCheckList> response) {
                if(response.body()!=null&&response.isSuccessful()){
                   obj.setModelTextNoteCheckList(response.body().getModelTextNoteCheckList());
                    title.setText(obj.getModelTextNoteCheckList().getTitle());
                    adapterCheckList = new AdapterCheckList(obj.getModelTextNoteCheckList().getData());
                    recyclerView.setAdapter(adapterCheckList);
                }

            }

            @Override
            public void onFailure(Call<ModelGetCheckList> call, Throwable t) {
                Log.e("TAG", "onFailure: "+t.getMessage() );
            }
        });


        Back();
    }
    private void getData(Intent intent){
        idNote = intent.getIntExtra("id", -1);
        colorA = intent.getFloatExtra("colorA",0);
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
    public com.example.cloud_note.Model.Color chuyenMauARGB (String hexColor){
        int red = Integer.parseInt(hexColor.substring(1, 3), 16);
        int green = Integer.parseInt(hexColor.substring(3, 5), 16);
        int blue = Integer.parseInt(hexColor.substring(5, 7), 16);
        com.example.cloud_note.Model.Color color = new com.example.cloud_note.Model.Color();
        color.setA((float) 0.87);
        color.setB(red);
        color.setG(green);
        color.setR(blue);
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
}