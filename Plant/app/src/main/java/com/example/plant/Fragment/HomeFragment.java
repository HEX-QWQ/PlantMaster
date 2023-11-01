package com.example.plant.Fragment;

import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.plant.Activity.camera;
import com.example.plant.Adapter.HomeAdapter;
import com.example.plant.Listview.HomeListView;
import com.example.plant.R;
import com.example.plant.Adapter.HomeAdapter;
import com.example.plant.View.WeatherView;
import com.example.plant.bean.plantBean;
import com.example.plant.utils.Constant;
import com.example.plant.utils.JsonParse;
import com.example.plant.Listview.HomeListView;
import com.example.plant.bean.plantBean;
import com.example.plant.utils.Constant;
import com.example.plant.utils.JsonParse;
import com.google.gson.Gson;
import com.qweather.sdk.bean.base.Code;
import com.qweather.sdk.bean.base.Lang;
import com.qweather.sdk.bean.base.Unit;
import com.qweather.sdk.bean.weather.WeatherNowBean;
import com.qweather.sdk.view.HeConfig;
import com.qweather.sdk.view.QWeather;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment implements View.OnClickListener {
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
    TextView ClickToCamera;
    private View inflate;
    private TextView choosePhoto;
    private TextView takePhoto;
    private Dialog dialog;
    Intent toCamera;
    WeatherView weatherView;
    public String getCoordinates() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }

        LocationManager locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            return String.format("%.2f", longitude) + "," + String.format("%.2f", latitude);
        } else {
            return "101010300";
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_fragment, container, false);
        imageView = view.findViewById(R.id.image_view);
        weatherView = view.findViewById(R.id.WeatherView1);
        weatherView.setMyType(WeatherView.Type.sunday);
        handler = new Handler();
        mHandler = new HHandler();

        init(view);
        HeConfig.init("HE2311011558511859", "a17193fef0b24c43919c619fcac13d33");
        HeConfig.switchToDevService();
        /**
         * 实况天气数据
         * @param location 所查询的地区，可通过该地区ID、经纬度进行查询经纬度格式：经度,纬度
         *                 （英文,分隔，十进制格式，北纬东经为正，南纬西经为负)
         * @param lang     (选填)多语言，可以不使用该参数，默认为简体中文
         * @param unit     (选填)单位选择，公制（m）或英制（i），默认为公制单位
         * @param listener 网络访问结果回调
         */
        String res = getCoordinates();
        Log.e("TAG",res);
        QWeather.getWeatherNow(getContext(),res , Lang.ZH_HANS, Unit.METRIC, new QWeather.OnResultWeatherNowListener() {
            @Override
            public void onError(Throwable e) {
                Log.i("TAG", "getWeather onError: " + e);
            }

            @Override
            public void onSuccess(WeatherNowBean weatherBean) {
                Log.i("TAG", "getWeather onSuccess: " + new Gson().toJson(weatherBean));
                //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                if (Code.OK == weatherBean.getCode()) {
                    WeatherNowBean.NowBaseBean now = weatherBean.getNow();
                    Log.e("TAG",now.getText());
                } else {
                    //在此查看返回数据失败的原因
                    Code code = weatherBean.getCode();
                    Log.i("TAG", "failed code: " + code);
                }
            }
        });
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
        ClickToCamera = view.findViewById(R.id.ClickToCamera);
        ClickToCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 处理点击事件
                Log.e("TAG","喵喵喵");
                show();
            }
        });
//        tv_title.setText("植物视界");
//        titlebar = (RelativeLayout) view.findViewById(R.id.title_bar);
//        tv_back.setVisibility(View.GONE);
        plantlist = (HomeListView) view.findViewById(R.id.plantlist);
        adapter = new HomeAdapter(getActivity());
        plantlist.setAdapter(adapter);
        search_text=(EditText) view.findViewById(R.id.search_text);
        search_btn=(ImageButton) view.findViewById(R.id.search_btn);
        toCamera = new Intent(getContext(), camera.class);
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
    public void show(){
        Log.e("TAG","调用相机");
        dialog = new Dialog(getContext());
        //填充对话框的布局
        inflate = LayoutInflater.from(getContext()).inflate(R.layout.dialog_takephotos, null);
        //初始化控件
        choosePhoto = (TextView) inflate.findViewById(R.id.choosePhoto);
        takePhoto = (TextView) inflate.findViewById(R.id.takePhoto);
        choosePhoto.setOnClickListener(this);
        takePhoto.setOnClickListener(this);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;//设置Dialog距离底部的距离
        //将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ClickToCamera:
                Log.e("TAG","点击了");
                show();
                break;
            case R.id.takePhoto:
                toCamera.putExtra("args","shot");
                startActivity(toCamera);
                dialog.dismiss();
                break;
            case R.id.choosePhoto:
                toCamera.putExtra("args","option");
                startActivity(toCamera);
                dialog.dismiss();
                break;
        }
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