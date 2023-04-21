package com.example.cloud_note.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cloud_note.APIs.APINote;
import com.example.cloud_note.Detail_CheckNote;
import com.example.cloud_note.Detail_Note;
import com.example.cloud_note.Model.GET.ModelGetCheckList;
import com.example.cloud_note.Model.GET.ModelGetNoteText;
import com.example.cloud_note.Model.GET.ModelReturn;
import com.example.cloud_note.Model.Model_List_Note;
import com.example.cloud_note.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterNote extends RecyclerView.Adapter<AdapterNote.ViewHoderItemNote> {

    Context context;
    List<Model_List_Note> list;
    AdapterCheckList adapterCheckList;
    String dataText;
    String title;
    String createAt;
    String duaAt;
    float a;
    int b;
    int g;
    int r;
    int nodeDone;


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
        Log.e("TAG", "onBindViewHolder: IdNote"+list_note.getId());
        holder.titleHeader.setText(list_note.getTitle());
        holder.createDate.setText(list_note.getCreateAt() + "");
        holder.dueDate.setText(list_note.getDuaAt() + "");
        String hex = ChuyenMau(list_note.getColor().getA(), list_note.getColor().getR(), list_note.getColor().getG(), list_note.getColor().getB());
        holder.RecyclerCardview.setCardBackgroundColor(Color.parseColor(hex + ""));
        if (list_note.getDoneNote() == 0) {
            holder.state.setText("Not Done");
        } else if (list_note.getDoneNote() == 1) {
            holder.state.setText("Done");
        }
        if (list_note.getType().equalsIgnoreCase("text")) {
            holder.rcvCheckList.setVisibility(View.GONE);
            holder.contentText.setVisibility(View.VISIBLE);
            Intent intent = new Intent(context, Detail_Note.class);
            intent.putExtra("id", list_note.getId());
            intent.putExtra("colorA", list_note.getColor().getA());
            intent.putExtra("colorR", list_note.getColor().getR());
            intent.putExtra("colorG", list_note.getColor().getG());
            intent.putExtra("colorB", list_note.getColor().getB());

            APINote.apiService.getNoteByIdTypeText(list_note.getId()).enqueue(new Callback<ModelGetNoteText>() {
                @Override
                public void onResponse(Call<ModelGetNoteText> call, Response<ModelGetNoteText> response) {
                    if(response.body()!=null&&response.isSuccessful()){
                        ModelGetNoteText obj = response.body();
                        holder.contentText.setText(obj.getModelTextNote().getData());
                        intent.putExtra("data",obj.getModelTextNote().getData());
                    }

                }

                @Override
                public void onFailure(Call<ModelGetNoteText> call, Throwable t) {
                    Log.e("TAG", "onFailure: "+t.getMessage() );
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(intent);
                }
            });


        } else if (list_note.getType().equalsIgnoreCase("checklist")) {
            holder.rcvCheckList.setVisibility(View.VISIBLE);
            holder.contentText.setVisibility(View.GONE);
            Intent intent = new Intent(context, Detail_CheckNote.class);
            intent.putExtra("id", list_note.getId());

            intent.putExtra("colorA", list_note.getColor().getA());
            intent.putExtra("colorR", list_note.getColor().getR());
            intent.putExtra("colorG", list_note.getColor().getG());
            intent.putExtra("colorB", list_note.getColor().getB());

            APINote.apiService.getNoteByIdTypeCheckList(list_note.getId()).enqueue(new Callback<ModelGetCheckList>() {
                @Override
                public void onResponse(Call<ModelGetCheckList> call, Response<ModelGetCheckList> response) {
                    ModelGetCheckList obj = response.body();
                    adapterCheckList = new AdapterCheckList(obj.getModelTextNoteCheckList().getData());
                    holder.rcvCheckList.setAdapter(adapterCheckList);
                   // intent.putExtra("data", new ArrayList<>(obj.getModelTextNoteCheckList().getData()));
                }

                @Override
                public void onFailure(Call<ModelGetCheckList> call, Throwable t) {
                    Log.e("TAG", "onFailure: "+t.getMessage() );
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(intent);
                }
            });

        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                dialogDelete(context, list_note.getId(), index);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


   public void dialogDelete(Context context, int id, int index){
           final Dialog dialog = new Dialog(context, androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert);
           dialog.setContentView(R.layout.dialog_delete_note);
           Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
           Button btn_delete = dialog.findViewById(R.id.btn_delete);
           Button btn_move_trash = dialog.findViewById(R.id.btn_move_trash);
           btn_cancel.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   dialog.dismiss();
               }
           });
           btn_delete.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   APINote.apiService.deleteNote(id).enqueue(new Callback<ModelReturn>() {
                       @Override
                       public void onResponse(Call<ModelReturn> call, Response<ModelReturn> response) {
                           if(response.isSuccessful()&response.body()!=null){
                               ModelReturn r = response.body();
                               if(r.getStatus()==200){
                                   list.remove(index);
                                   notifyDataSetChanged();
                                   notifyItemRangeRemoved(index, list.size());
                                   Toast.makeText(context, r.getMessage() , Toast.LENGTH_SHORT).show();
                                   dialog.dismiss();
                               }

                           }
                       }

                       @Override
                       public void onFailure(Call<ModelReturn> call, Throwable t) {
                           Log.e("TAG", "onFailure: "+t.getMessage() );
                       }
                   });
               }
           });
           btn_move_trash.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                    APINote.apiService.moveToTrash(id).enqueue(new Callback<ModelReturn>() {
                        @Override
                        public void onResponse(Call<ModelReturn> call, Response<ModelReturn> response) {
                            if(response.isSuccessful()&response.body()!=null){
                                ModelReturn r = response.body();
                                if(r.getStatus()==200){
                                    list.remove(index);
                                    notifyDataSetChanged();
                                    notifyItemRangeRemoved(index, list.size());
                                    Toast.makeText(context, r.getMessage() , Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<ModelReturn> call, Throwable t) {
                            Log.e("TAG", "onFailure: "+t.getMessage() );
                        }
                    });
               }
           });

           dialog.show();
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

    public class ViewHoderItemNote extends RecyclerView.ViewHolder {
        private TextView titleHeader;
        private TextView state;
        private TextView contentText;
        private TextView Created;
        private TextView createDate;
        private TextView Due;
        private TextView dueDate;
        private CardView RecyclerCardview;
        private RecyclerView rcvCheckList;


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
            rcvCheckList = (RecyclerView) itemView.findViewById(R.id.rcv_checkList);
        }
    }
}
