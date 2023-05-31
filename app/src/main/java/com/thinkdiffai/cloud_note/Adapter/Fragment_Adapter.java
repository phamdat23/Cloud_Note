package com.thinkdiffai.cloud_note.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.thinkdiffai.cloud_note.R;
import com.thinkdiffai.cloud_note.View.Fragment_Calendar;
import com.thinkdiffai.cloud_note.View.Fragment_Delete;
import com.thinkdiffai.cloud_note.View.Fragment_Home;

public class Fragment_Adapter extends FragmentStateAdapter {

    public Fragment_Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new Fragment_Home();
            case 1:

                return  new Fragment_Calendar();
            case 2:

                return new Fragment_Delete();
            default:

                return  new Fragment_Home();
        }

    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
