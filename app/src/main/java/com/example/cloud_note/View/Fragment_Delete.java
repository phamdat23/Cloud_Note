package com.example.cloud_note.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cloud_note.R;
import com.example.cloud_note.SettingActivity;

public class Fragment_Delete extends Fragment {
    private ImageButton btnSetting;
    private EditText seachEdittxt;
    private ImageButton buttonSortby;
    private RecyclerView recyclerView;
    Context context;

    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.deleted_page, container, false);


        btnSetting = (ImageButton) view.findViewById(R.id.btnSetting);
        seachEdittxt = (EditText) view.findViewById(R.id.seach_edittxt);
        buttonSortby = (ImageButton) view.findViewById(R.id.button_sortby);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_View);
        context = getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SettingActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }
}
