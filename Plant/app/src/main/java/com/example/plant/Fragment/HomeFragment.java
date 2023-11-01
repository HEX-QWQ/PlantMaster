package com.example.plant.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.plant.Adapter.HomeAdapter;
import com.example.plant.Listview.HomeListView;
import com.example.plant.R;
import com.example.plant.Adapter.HomeAdapter;
import com.example.plant.bean.plantBean;
import com.example.plant.utils.Constant;
import com.example.plant.utils.JsonParse;
import com.example.plant.Listview.HomeListView;
import com.example.plant.bean.plantBean;
import com.example.plant.utils.Constant;
import com.example.plant.utils.JsonParse;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment{
    private ImageView imageView;
    private int[] images = {R.drawable.image1, R.drawable.image2, R.drawable.image4,R.drawable.image5}; // 图片资源数组
    private Handler handler;//用于处理轮播图的子线程
    private Runnable runnable;
    private int currentPosition = 0;
    private static final long DELAY_TIME = 1500; // 图片切换时间间隔
    private TextView tv_back, tv_title; //返回键和标题控件
    private HomeListView plantlist; //图书列表控件
    private HomeAdapter adapter;//列表对应的适配器
    public static final int MSG_SHOP_OK = 1; //获取数据
    private EditText search_text;
    private ImageButton search_btn;
    private HHandler mHandler;
    private RelativeLayout titlebar;//标题和返回键对应的视图上方的框
    private OkHttpClient okHttpClient;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_fragment, container, false);
        imageView = view.findViewById(R.id.image_view);
        handler = new Handler();
        mHandler = new HHandler();
        init(view);
        runnable = new Runnable() {
            @Override
            public void run() {
                currentPosition++;
                if (currentPosition >= images.length) {
                    currentPosition = 0;
                }
                // 切换图片
                imageView.setImageResource(images[currentPosition]);
                // 循环播放
                handler.postDelayed(runnable, DELAY_TIME);
            }
        };
        sendRequest();
        return view;
    }

    private void init(View view) {
        tv_back = (TextView) view.findViewById(R.id.tv_back);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText("植物视界");
        titlebar = (RelativeLayout) view.findViewById(R.id.title_bar);
        tv_back.setVisibility(View.GONE);
        plantlist = (HomeListView) view.findViewById(R.id.plantlist);
        adapter = new HomeAdapter(getActivity());
        plantlist.setAdapter(adapter);
        search_text=(EditText) view.findViewById(R.id.search_text);
        search_btn=(ImageButton) view.findViewById(R.id.search_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchtext=search_text.getText().toString();
                sendRequest2(searchtext);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        handler.postDelayed(runnable, DELAY_TIME);
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    private void sendRequest() {
        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Constant.ReadPlant_URL)
                .build();
        Call call = okHttpClient.newCall(request);
        // 开启异步线程访问网络
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string(); // 获取图书数据
                Log.e("response", res);
                Message msg = new Message();
                msg.what = MSG_SHOP_OK;
                msg.obj = res;
                mHandler.sendMessage(msg);
            }
        });
    }
    private void sendRequest2(String searchtext) {
        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Constant.SearchPlant_URL+ "?plantname=" + searchtext)
                .build();
        Call call = okHttpClient.newCall(request);
        // 开启异步线程访问网络
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string(); // 获取图书数据
                Log.e("response", res);
                JSONArray jsonArray;
                try {
                    jsonArray = new JSONArray(res); // 将字符串转换为JSON数组
                } catch (JSONException e) {
                    e.printStackTrace();
                    return; // 返回，不继续执行后续的处理
                }
                Message msg = new Message();
                msg.what = MSG_SHOP_OK;
                msg.obj = res;
                mHandler.sendMessage(msg);
            }
        });
    }
    /*事件捕获*/
    class HHandler extends Handler {
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case MSG_SHOP_OK:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        Log.e("CartActivity", result);
                        //解析获取的JSON数据
                        List<plantBean> plantList = JsonParse.getInstance().getPlantList(result);
                        adapter.setData(plantList);
                    }
                    break;
            }
        }
    }
}