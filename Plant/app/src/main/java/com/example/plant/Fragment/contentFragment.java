package com.example.plant.Fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plant.Adapter.myPlantRecyclerViewAdpter;
import com.example.plant.R;
import com.example.plant.bean.plantBean;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class contentFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public String title;
    List<plantBean>plantList;


    //数据生成对象
    public contentFragment() {
        // Required empty public constructor
    }
    public contentFragment(String title){
        this.title = title;
        this.plantList = initData();
        //直接在构造方法中获取所有的对象
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment contentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static contentFragment newInstance(String param1) {
        Log.e("碎片生成","重新生成content碎片");
        contentFragment fragment = new contentFragment(param1);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

        Log.e("标题",param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return initAll(inflater,container,savedInstanceState);
    }
    View initAll(LayoutInflater inflater, ViewGroup container,
                 Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_placeholder, container, false);
        RecyclerView recycleView = (RecyclerView)view.findViewById(R.id.plantList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recycleView.setLayoutManager(layoutManager);
        myPlantRecyclerViewAdpter adapter = new myPlantRecyclerViewAdpter(plantList);
        recycleView.setAdapter(adapter);
        return view;
    }
    List<plantBean> initData(){
        List<plantBean> tmp = new ArrayList<>();
        plantBean now = new plantBean();
        plantBean now2 = new plantBean();
//        Drawable drawable = getResources().getDrawable(R.drawable.image2);
//        int width = drawable.getIntrinsicWidth();
//        int height = drawable.getIntrinsicHeight();
//
//        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        drawable.setBounds(0, 0, width, height);
//        drawable.draw(canvas);
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//        byte[] imageBytes = byteArrayOutputStream.toByteArray();

//        String base64EncodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        if(title.equals("今日养护")) now.setpname("晓栀-客厅");
        else now.setpname("栀子花-阳台");
        tmp.add(now);
        if(title.equals("今日养护"))  now2.setpname("芍药-客厅");
        else now2.setpname("黄金花月-阳台");
        tmp.add(now2);
        return tmp;
    }

}

