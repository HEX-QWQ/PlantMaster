package com.example.plant.Adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.plant.Fragment.PlaceholderFragment;

import java.util.ArrayList;
import java.util.List;

public class myFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
    private String[] titleList;


    private FragmentManager fm;



    public myFragmentStatePagerAdapter(@NonNull FragmentManager fm, String[] titleList) {
        super(fm);
        this.fm=fm;
        this.titleList = titleList;
    }

    @Override
    public Fragment getItem(int idx) {

        String newsCategoryTitle = titleList[idx];
        return PlaceholderFragment.newInstance(newsCategoryTitle);
    }
    //滑动翻页时根据idx 返回需要装入ViewPager的碎片。

    @Override
    public int getCount() {
        return titleList.length;
    }
}

