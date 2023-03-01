package com.example.fakecall.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.fakecall.CharacterActivity;
import com.example.fakecall.FragmentHome;

public class ViewPageAdapter extends FragmentStateAdapter {
    public  static  int chung  ;
    public ViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull
    @Override
    public  Fragment createFragment(int position) {
        switch (position)
        {
            case 0 :
                return new FragmentHome();
            case 1 :
                return new CharacterActivity();
            default:
                return new FragmentHome() ;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
