package com.example.plant.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.plant.Activity.LoginActivity;
import com.example.plant.Activity.MainActivity;
import com.example.plant.bean.User;
import com.example.plant.R;
import com.example.plant.bean.User;


public class MineFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;
    TextView ClickToLogin;
    TextView ClickToLogout;

    private User user;

    public MineFragment(User user) {
        // Required empty public constructor
        this.user=user;
    }
    public MineFragment() {
        // Required empty public constructor
    }
    public static MineFragment newInstance(String param1, String param2) {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        //Log.e("TAG","onCreateView");
        View view = inflater.inflate(R.layout.fragment_space, container, false);
        initView(view);
        initListener();

        return view;
    }
    void initView(View view){
        ClickToLogin = (TextView) view.findViewById(R.id.ClickToLogin);
        ClickToLogout = (TextView) view.findViewById(R.id.ClickToLogout);
    }
    void initListener(){

        if(user == null) {
            ClickToLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    getContext().startActivity(intent);
                }
            });

            ClickToLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
                }
            });

        }//如果为空的话设置监听器事件，点击跳转到登录界面
        else{
            ClickToLogin.setText(user.getSname());

            ClickToLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    //清空调用栈
                    getContext().startActivity(intent);
                }
            });
        }
    }
}