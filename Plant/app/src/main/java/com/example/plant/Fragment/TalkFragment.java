package com.example.plant.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.plant.R;


public class TalkFragment extends Fragment {
    private TextView content; //返回键和标题控件
    private ImageView image_avatar;
    private TextView title;//标题和返回键对应的视图上方的框


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_talk_fragment,null);

        init(view);
        return view;
    }
    private void init(View view) {
        title=view.findViewById(R.id.title);
        image_avatar=view.findViewById(R.id.image_avatar);
        content = view.findViewById(R.id.name_text);


    }
}