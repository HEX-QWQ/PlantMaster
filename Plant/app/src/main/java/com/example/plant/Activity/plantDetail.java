package com.example.plant.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.plant.R;
import com.example.plant.bean.plantBean;
import com.example.plant.utils.Constant;
import com.example.plant.bean.plantBean;
import com.example.plant.utils.Constant;

import java.io.IOException;

public class plantDetail extends AppCompatActivity {
    public TextView tv_back, tv_title; //返回键和标题控件
    public RelativeLayout titlebar;//标题和返回键对应的视图上方的框\
    private ImageView plant_photo;
    private plantBean bean;
    private TextView   plant_name,plant_type,plant_introduct,plant_env,plant_eng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_detail);
        bean = (plantBean) getIntent().getSerializableExtra("plant"); //获取店铺详情数据 23
        if (bean == null) return;
       init();
       setData();
    }
    private void init() {
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("植株详情");
        titlebar = (RelativeLayout) findViewById(R.id.title_bar);
        //结束活动，返回碎片页
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        plant_photo=(ImageView)findViewById(R.id.plant_photo);
        plant_name=(TextView)findViewById(R.id.plant_name);
        plant_type=(TextView)findViewById(R.id.plant_type);
        plant_introduct=(TextView)findViewById(R.id.plant_introduct);
        plant_env=(TextView)findViewById(R.id.plant_env);
        plant_eng=(TextView)findViewById(R.id.plant_eng);
    }
    private void setData(){
        plant_photo.setImageBitmap(bean.getimg());
        plant_name.setText(bean.getpname());
        plant_type.setText("植物类别："+bean.gettype());
        plant_introduct.setText("简介："+"\n"+bean.getcontent());
        plant_env.setText("生长习性："+"\n"+bean.getenv());
        plant_eng.setText("育植技术："+"\n"+bean.geteng());

    }
}