package com.example.myapplication.Ado;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import java.io.File;
import java.util.ArrayList;

public class ReportImageRecyAdapter extends RecyclerView.Adapter<ReportImageRecyAdapter.CustomViewHolder> {
    private Context mContext;
    private ArrayList<File> mImages;

    public ReportImageRecyAdapter(Context mContext, ArrayList<File> mImages) {
        this.mContext = mContext;
        this.mImages = mImages;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.ado_report_image_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Glide.with(mContext).load(mImages.get(position)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ado_report_image);
        }
    }
}