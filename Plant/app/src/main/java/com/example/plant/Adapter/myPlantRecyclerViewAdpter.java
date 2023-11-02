package com.example.plant.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plant.Activity.plantDetail;
import com.example.plant.R;
import com.example.plant.bean.plantBean;

import java.util.List;

public class myPlantRecyclerViewAdpter extends RecyclerView.Adapter<myPlantRecyclerViewAdpter.ViewHolder>{
    private List<plantBean> plantList;
    Context context;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ListImage;
        TextView ListTitle;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.ListImage = (ImageView)itemView.findViewById(R.id.myPlantImg);
            this.ListTitle =  (TextView)itemView.findViewById(R.id.myPlantName);
        }
    }
    public myPlantRecyclerViewAdpter(List<plantBean>plantList){
        this.plantList = plantList;
    }
    public myPlantRecyclerViewAdpter(List<plantBean>plantList,Context context){
        this.plantList = plantList;
        this.context = context;
    }
    @NonNull
    @Override
    public myPlantRecyclerViewAdpter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plant_card,parent,false);
        final myPlantRecyclerViewAdpter.ViewHolder holder = new myPlantRecyclerViewAdpter.ViewHolder(view);
        context= parent.getContext();
        //TODO 在这里处理每个列表项的点击事件

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Intent intent = new Intent(context, plantDetail.class);
                intent.putExtra("url",plantList.get(position).getpname());

                context.startActivity(intent);

            }
        });

        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        plantBean plant = plantList.get(position);
        //holder.ListImage.setImageBitmap(getImage(course.getImage()));
        //Glide.with(context).load(poem.getImage()).fitCenter().into(holder.ListImage);

        holder.ListTitle.setText(plant.getpname());
        holder.ListImage.setImageBitmap(plant.getimg());
    }

    @Override
    public int getItemCount() {
        return plantList.size();
    }
}
