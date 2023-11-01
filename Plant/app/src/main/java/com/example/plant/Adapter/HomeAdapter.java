package com.example.plant.Adapter;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plant.R;
import com.example.plant.Activity.plantDetail;
import com.example.plant.bean.plantBean;
import com.example.plant.utils.Constant;
import com.example.plant.Activity.plantDetail;
import com.example.plant.bean.plantBean;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeAdapter extends BaseAdapter {
    private Context mContext;
    private List<plantBean> plant;
    private Handler mHandler; // 添加Handler成员变量
    public HomeAdapter(Context context){
        this.mContext=context;
        mHandler = new Handler(Looper.getMainLooper()); // 初始化Handler
    }
    //数据更新
    public void setData(List<plantBean> plant){
        this.plant=plant;
        notifyDataSetChanged();
    }
    //获取item总数
    @Override
    public int getCount() {
        return plant==null?0:plant.size();
    }
    //根据position得到对应Item对象
    @Override
    public plantBean getItem(int position) {
        return plant==null?null:plant.get(position);
    }
    //根据position得到对应Item的id
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if(convertView==null){
            vh=new ViewHolder();
            convertView=LayoutInflater.from(mContext).inflate(R.layout.plant_item,null);
            vh.plant_photo=(ImageView)convertView.findViewById(R.id.plant_photo);
            vh.plant_name=(TextView)convertView.findViewById(R.id.plant_name);
            vh.plant_type=(TextView)convertView.findViewById(R.id.plant_type);
            convertView.setTag(vh);
        }
        else{
            vh=(ViewHolder)convertView.getTag();
        }
        final plantBean bean=getItem(position);
        if(bean!=null) {
            vh.plant_photo.setImageBitmap(bean.getimg());
            vh.plant_name.setText(bean.getpname());
            vh.plant_type.setText(bean.gettype());
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bean == null) return;
                Intent intent = new Intent(mContext, plantDetail.class);
                //把图书的详细信息传递到图书详情界面
                intent.putExtra("plant", bean);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }
    class ViewHolder{
        public TextView plant_name,plant_type,plant_introduct,plant_env,plant_eng;
        public ImageView plant_photo;
    }
}
