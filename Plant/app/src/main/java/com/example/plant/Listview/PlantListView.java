package com.example.plant.Listview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class PlantListView extends ListView {
    public PlantListView(Context context, AttributeSet attrs){
        super(context, attrs);
    }
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        int expandSpec= MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec,expandSpec);
    }
}
