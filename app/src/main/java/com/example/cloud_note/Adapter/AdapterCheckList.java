package com.example.cloud_note.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cloud_note.Model.ModelCheckList;
import com.example.cloud_note.R;

import java.util.List;

public class AdapterCheckList extends RecyclerView.Adapter<AdapterCheckList.ViewHoderCheckList> {
    List<ModelCheckList> list;
    Context context;

    public AdapterCheckList(List<ModelCheckList> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHoderCheckList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.recycler_checknote, parent, false);
        context = parent.getContext();
        return new ViewHoderCheckList(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoderCheckList holder, int position) {
        final  int index = position;
        ModelCheckList checkList = list.get(index);
        holder.tvCheckNote.setText(checkList.getContent());
        if(checkList.getStatus()==1){
            holder.checkbox.setChecked(true);
        }else{
            holder.checkbox.setChecked(false);
        }
        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list==null?0: list.size();
    }

    public class ViewHoderCheckList extends RecyclerView.ViewHolder {
        private CheckBox checkbox;
        private TextView tvCheckNote;
        public ViewHoderCheckList(@NonNull View itemView) {
            super(itemView);
            checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);
            tvCheckNote = (TextView) itemView.findViewById(R.id.tv_checkNote);

        }
    }
}
