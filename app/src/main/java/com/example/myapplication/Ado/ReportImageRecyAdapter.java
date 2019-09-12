package com.example.myapplication.Ado;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class ReportImageRecyAdapter extends RecyclerView.Adapter<ReportImageRecyAdapter.CustomViewHolder> {
    private Context mContext;
    private ArrayList<Bitmap> mImagesBitmap;

    public ReportImageRecyAdapter(Context mContext, ArrayList<Bitmap> mImagesBitmap) {
        this.mContext = mContext;
        this.mImagesBitmap = mImagesBitmap;
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
        holder.imageView.setImageBitmap(mImagesBitmap.get(position));
    }

    @Override
    public int getItemCount() {
        return mImagesBitmap.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ado_report_image);
        }
    }
}
