package com.example.cloud_note.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cloud_note.APIs.APINote;
import com.example.cloud_note.Adapter.AdapterNote;
import com.example.cloud_note.DAO.Login;
import com.example.cloud_note.Model.GET.Model_Notes;
import com.example.cloud_note.Model.Model_State_Login;
import com.example.cloud_note.R;
import com.example.cloud_note.SettingActivity;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Fragment_Delete extends Fragment {
    private ImageButton btnSetting;
    private EditText seachEdittxt;
    private ImageButton buttonSortby;
    private RecyclerView recyclerView;
    Context context;
    AdapterNote adapterNote;
Login daoLogin;
Model_State_Login user;
    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.deleted_page, container, false);
        btnSetting = (ImageButton) view.findViewById(R.id.btnSetting);
        seachEdittxt = (EditText) view.findViewById(R.id.seach_edittxt);
        buttonSortby = (ImageButton) view.findViewById(R.id.button_sortby);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_View);
        context = getContext();
        daoLogin = new Login(context);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = daoLogin.getLogin();
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SettingActivity.class);
                getActivity().startActivity(intent);
            }
        });
        getData(user.getIdUer());
    }
    private void getData(int idUser){
        Model_Notes obj= new Model_Notes();
        APINote.apiService.getListTrash(idUser).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Model_Notes>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull Model_Notes model_notes) {
                        obj.setList(model_notes.getList());
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.e("TAG", "onError: "+e.getMessage() );
                    }

                    @Override
                    public void onComplete() {
                        adapterNote = new AdapterNote(obj.getList());
                        recyclerView.setAdapter(adapterNote);
                    }
                });
    }
}
