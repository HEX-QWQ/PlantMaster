package com.example.plant.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.plant.R;

public class PlaceholderFragment extends Fragment {
    private static final String ARG_SECTION_TITLE = "section_title";

    public static PlaceholderFragment newInstance(String sectionTitle) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION_TITLE, sectionTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 加载布局文件
        View rootView = inflater.inflate(R.layout.fragment_placeholder, container, false);
        // 获取参数值
        String sectionTitle = getArguments().getString(ARG_SECTION_TITLE);
        // 设置对应的 ID
        TextView textView = rootView.findViewById(R.id.section_label);
        textView.setText(sectionTitle);
        return rootView;
    }
}

