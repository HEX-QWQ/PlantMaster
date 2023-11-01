package com.example.plant.Activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.plant.Fragment.HomeFragment;
import com.example.plant.Fragment.MineFragment;
import com.example.plant.Fragment.MyplantFragment;
import com.example.plant.Fragment.TalkFragment;
import com.example.plant.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Fragment> mFragments;
    //四个Tab对应的布局
    private LinearLayout mTabHome;
    private LinearLayout mTabVideo;
    private LinearLayout mTabQuan;
    private LinearLayout mTabMine;


    //四个Tab对应的ImageButton
    private ImageButton mImgHome;
    private ImageButton mImgVideo;
    private ImageButton mImgQuan;
    private ImageButton mImgMine;

    private ImageButton mImgTui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragments(); //初始化数据
        initViews(); //初始化控件
        initEvents(); //初始化事件
        initFirstRun(0);//第一次运行初始化界面，第一个碎片
    }
    private void initFragments() {
        mFragments = new ArrayList<>();
        //将四个Fragment加入集合中
        mFragments.add(new HomeFragment());
        mFragments.add(new MyplantFragment());
        mFragments.add(new TalkFragment());
        mFragments.add(new MineFragment());

    }
    private void initViews() {
        mTabHome = (LinearLayout) findViewById(R.id.id_tab_home);
        mTabVideo = (LinearLayout) findViewById(R.id.id_tab_video);
        mTabQuan = (LinearLayout) findViewById(R.id.id_tab_quan);
        mTabMine = (LinearLayout) findViewById(R.id.id_tab_mine);

        mImgHome = (ImageButton) findViewById(R.id.id_tab_home_img);
        mImgVideo = (ImageButton) findViewById(R.id.id_tab_video_img);
        mImgQuan = (ImageButton) findViewById(R.id.id_tab_quan_img);
        mImgMine = (ImageButton) findViewById(R.id.id_tab_mine_img);

    }
    private void initEvents() {
        //设置四个Tab的点击事件
        mTabHome.setOnClickListener(onClickListener);
        mTabVideo.setOnClickListener(onClickListener);
        mTabQuan.setOnClickListener(onClickListener);
        mTabMine.setOnClickListener(onClickListener);

    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //先将四个ImageButton置为灰色
            resetImgs();
            //根据点击的Tab切换不同的页面及设置对应的ImageButton为绿色
            switch (v.getId()) {
                case R.id.id_tab_home: selectTab(0); break;
                case R.id.id_tab_video: selectTab(1); break;
                case R.id.id_tab_quan: selectTab(2); break;
                case R.id.id_tab_mine: selectTab(3); break;
            }
        }};
    private void resetImgs() {
        mImgHome.setImageResource(R.mipmap.tab_home_normal);
        mImgVideo.setImageResource(R.mipmap.tab_myplant_normal);
        mImgQuan.setImageResource(R.mipmap.tab_talk_normal);
        mImgMine.setImageResource(R.mipmap. tab_mine_normal);

    }
    private void selectTab(int i) {
        //根据点击的Tab设置对应的ImageButton为绿色
        switch (i) {
            case 0: mImgHome.setImageResource(R.mipmap.tab_home_pressed);break;
            case 1: mImgVideo.setImageResource(R.mipmap.tab_myplant_pressed);break;
            case 2: mImgQuan.setImageResource(R.mipmap.tab_talk_pressed);break;
            case 3:  mImgMine.setImageResource(R.mipmap.tab_mine_pressed);break;
        }
        //设置当前点击的Tab所对应的页面
        setCurrentFragment(i);
    }
    private void setCurrentFragment(int i)
    {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        trans.replace(R.id.frag_layout, mFragments.get(i));
        trans.commit();
    }
    private void initFirstRun(int i)
    {
        resetImgs(); //重置所有Tab
        selectTab(i); //显示第i个碎片
    }
    protected long exitTime;
    public boolean onKeyDown(int keyCode, KeyEvent  event){
        if(keyCode==KeyEvent.KEYCODE_BACK
        &&event.getAction()==KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime)>2000){
                Toast.makeText(MainActivity.this,"再按一次退出应用",
                        Toast.LENGTH_LONG).show();
                exitTime=System.currentTimeMillis();
            }else{
                MainActivity.this.finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }


}

