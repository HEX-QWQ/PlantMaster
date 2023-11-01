package com.example.plant.Fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telecom.Call;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.plant.Adapter.myFragmentStatePagerAdapter;
import com.example.plant.Adapter.myPagerAadpter;
import com.example.plant.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class MyplantFragment extends Fragment implements ViewPager.OnPageChangeListener{
    private TextView tv_back; //返回键和标题控件
    private RelativeLayout titlebar;//标题和返回键对应的视图上方的框
    ArrayList<View> viewContainter = new ArrayList<View>();
    private View view=null;
    private RadioGroup rgChannel=null;
    private ViewPager viewPager;
    private HorizontalScrollView hvChannel=null;

    private String[] titleList = {"今日养护","即将养护"};  //栏目列表
    private myFragmentStatePagerAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(view==null){
            view=inflater.inflate(R.layout.activity_talk_fragment, container, false);

            viewPager=(ViewPager)view.findViewById(R.id.vpNewsList);
            initViewPager();

            rgChannel=(RadioGroup)view.findViewById(R.id.rgChannel);
            hvChannel=(HorizontalScrollView)view.findViewById(R.id.hvChannel);
            initTab(inflater); //初始化内导航标签
            rgChannel.setOnCheckedChangeListener( //单选按钮的监听事件响应
                    new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group,int checkedId) {
                            viewPager.setCurrentItem(checkedId);
                        }});

        }

        return view;
    }
    private void initTab(LayoutInflater inflater){
        for(int i=0;i<titleList.length;i++){//以下添加单选按钮的实例到内导航
            RadioButton rb=(RadioButton)inflater.inflate(R.layout.tab_rb, null);
            rb.setId(i);
            rb.setText(titleList[i]);
            rb.setTextColor(Color.BLACK);
            // 创建一个Typeface对象，指定为加粗样式
            Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
            rb.setTypeface(boldTypeface);
            RadioGroup.LayoutParams params=new
                    RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT);

            rgChannel.addView(rb,params);
        }
        rgChannel.check(0); //第一个选项
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        setTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}
    private void initViewPager(){
        FragmentManager fragmentManager = super.getActivity().getSupportFragmentManager();
        adapter=new myFragmentStatePagerAdapter(fragmentManager, titleList);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        //viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(this);
    }
    //设置选定某个碎片时水平滚动视图的显示HorizontalScrollView
    private void setTab(int idx){
        RadioButton rb=(RadioButton)rgChannel.getChildAt(idx);
        rb.setChecked(true);
        int left=rb.getLeft();
        int width=rb.getMeasuredWidth();
        DisplayMetrics metrics=new DisplayMetrics();
        super.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth=metrics.widthPixels;
        int len=left+width/2-screenWidth/2;
        hvChannel.smoothScrollTo(len, 0);
    }
}