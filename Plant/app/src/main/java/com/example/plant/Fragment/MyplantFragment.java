package com.example.plant.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


import com.example.plant.R;


import java.io.IOException;


public class MyplantFragment extends Fragment {
    private TextView tv_back; //返回键和标题控件
    private RelativeLayout titlebar;//标题和返回键对应的视图上方的框\
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_myplant_fragment,null);
        init(view);
        return view;
    }
    private void init(View view) {
        tv_back = (TextView) view.findViewById(R.id.tv_back);
        titlebar = (RelativeLayout) view.findViewById(R.id.title_bar);
        tv_back.setVisibility(View.GONE);

    }
}