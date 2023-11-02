package com.example.plant.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.plant.Fragment.contentFragment;
import com.example.plant.Fragment.resFragment;

public class myCameraFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
    private String[] titleList;


    private FragmentManager fm;



    public myCameraFragmentStatePagerAdapter(@NonNull FragmentManager fm, String[] titleList) {
        super(fm);
        this.fm=fm;
        this.titleList = titleList;
    }
    public myCameraFragmentStatePagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        this.fm = fm;
    }
    @Override
    public Fragment getItem(int idx) {

        //"今日养护","即将养护"
        return resFragment.newInstance("test");
    }
    //滑动翻页时根据idx 返回需要装入ViewPager的碎片。

    @Override
    public int getCount() {
        return 2;
    }
}

