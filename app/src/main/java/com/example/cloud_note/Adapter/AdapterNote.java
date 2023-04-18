package com.example.cloud_note.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cloud_note.APIs.APINote;
import com.example.cloud_note.Model.ModelTextNote;
import com.example.cloud_note.Model.Model_List_Note;
import com.example.cloud_note.Model.Model_Notes;
import com.example.cloud_note.R;

import java.text.SimpleDateFormat;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AdapterNote extends RecyclerView.Adapter<AdapterNote.ViewHoderItemNote> {

    Context context;
    List<Model_List_Note> list;

    public AdapterNote(List<Model_List_Note> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHoderItemNote onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.custom_layout, parent, false);
        context = parent.getContext();

        return new ViewHoderItemNote(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoderItemNote holder, int position) {
        final int index = position;

        Model_List_Note list_note = list.get(index);
        holder.titleHeader.setText(list_note.getTitle());
        holder.createDate.setText(list_note.getCreateAt() + "");
        holder.dueDate.setText(list_note.getDuaAt() + "");
        String hex = ChuyenMau(list_note.getColor().getA(), list_note.getColor().getR(), list_note.getColor().getG(), list_note.getColor().getB());
        holder.RecyclerCardview.setCardBackgroundColor(Color.parseColor(hex+""));
        if (list_note.getDoneNote() == 0) {
            holder.state.setText("Not Done");
        } else if (list_note.getDoneNote() == 1) {
            holder.state.setText("Done");
        }
        if(list_note.getType().equalsIgnoreCase("text")){
            getDataText(list_note.getId());
        }else if(list_note.getType().equalsIgnoreCase("checklist")){

        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }
    private String getDataText(int id){
        String data ="";
        APINote.apiService.getNoteByIdTypeText(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ModelTextNote>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull ModelTextNote modelTextNote) {
                        Log.d("TAG", "onNext:DATA: "+modelTextNote.getData());
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.e("TAG", "onError: "+e );
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return data;
    }


    private String ChuyenMau(float alpha, float red, float green, float blue) {
        // chuyển đổi giá trị của từng kênh màu sang giá trị thập lục phân
        String alphaHex = Integer.toHexString((int) alpha);
        String redHex = Integer.toHexString((int) red);
        String greenHex = Integer.toHexString((int) green);
        String blueHex = Integer.toHexString((int) blue);
// ghép các giá trị thập lục phân lại với nhau theo thứ tự ARGB
        String hex = "#"  + redHex + greenHex + blueHex;
        Log.d("TAG", "ChuyenMau: "+hex);
        return hex;
    }

    public class ViewHoderItemNote extends RecyclerView.ViewHolder {
        private TextView titleHeader;
        private TextView state;
        private TextView contentText;
        private TextView Created;
        private TextView createDate;
        private TextView Due;
        private TextView dueDate;
        private CardView RecyclerCardview;


        public ViewHoderItemNote(@NonNull View itemView) {
            super(itemView);
            titleHeader = (TextView) itemView.findViewById(R.id.title_header);
            state = (TextView) itemView.findViewById(R.id.state);
            contentText = (TextView) itemView.findViewById(R.id.content_text);
            Created = (TextView) itemView.findViewById(R.id.Created);
            createDate = (TextView) itemView.findViewById(R.id.create_date);
            Due = (TextView) itemView.findViewById(R.id.Due);
            dueDate = (TextView) itemView.findViewById(R.id.due_date);
            RecyclerCardview = (CardView) itemView.findViewById(R.id.Recycler_Cardview);
        }
    }
}
