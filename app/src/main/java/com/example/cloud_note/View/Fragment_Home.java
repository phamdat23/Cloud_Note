package com.example.cloud_note.View;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.cloud_note.APIs.APINote;
import com.example.cloud_note.Adapter.AdapterNote;
import com.example.cloud_note.DAO.Login;
import com.example.cloud_note.Model.Model_List_Note;
import com.example.cloud_note.Model.GET.Model_Notes;
import com.example.cloud_note.Model.Model_State_Login;
import com.example.cloud_note.R;
import com.example.cloud_note.SettingActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Fragment_Home extends Fragment {
    private Activity mActivity;
    Context context;
    private CardView homePage;
    private ImageButton preferences;
    private SearchView search;
    private ImageButton buttonSortby;
    private RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    AdapterNote adapterNote;
    Model_Notes note;
    Login daoLogin;
    Model_State_Login user;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_page, container, false);
        mActivity = (Activity) getActivity();

        sharedPreferences = getActivity().getSharedPreferences("Account", Context.MODE_PRIVATE);
        homePage = (CardView) view.findViewById(R.id.home_page);
        preferences = (ImageButton) view.findViewById(R.id.preferences);
        search = (SearchView) view.findViewById(R.id.search);
        buttonSortby = (ImageButton) view.findViewById(R.id.button_sortby);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_View);
        //====================================================
        context = getContext();
        daoLogin = new Login(context);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = daoLogin.getLogin();
        preferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SettingActivity.class);
                mActivity.startActivity(intent);
            }
        });
        getListNote();
    }

    @Override
    public void onResume() {
        super.onResume();
        getListNote();
    }

    public void getListNote() {
        Log.d("TAG", "getListNote: Step1");
        APINote.apiService.getListNoteByUser(user.getIdUer()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Model_Notes>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull Model_Notes model_notes) {
                        note = model_notes;
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.e("TAG", "onError: " + e);
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(context, "Lấy dữ liệu thành công", Toast.LENGTH_SHORT).show();
                        List<Model_List_Note> list = new ArrayList<>();
                        list.addAll(note.getList());
                        adapterNote = new AdapterNote(list);
                        recyclerView.setAdapter(adapterNote);

                    }
                });
    }
}