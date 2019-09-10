package com.example.myapplication.Admin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Admin.AdoDdoActivity.AdoDdoActivity;
import com.example.myapplication.R;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

public class RecyclerViewAdater extends RecyclerView.Adapter<RecyclerViewAdater.ViewHolder> {

    ArrayList<String> mtextview1;
    ArrayList<String> mtextview2;
    public boolean mShowShimmer = true;
    Context mcontext;
    private boolean isDdoFragment;
    ArrayList<String> mUserId;
    private int SHIMMER_ITEM_COUNT = 6;

    public RecyclerViewAdater(Context mcontext, ArrayList<String> mtextview1, ArrayList<String> mtextview2, boolean isDdoFragment) {
        this.mcontext = mcontext;
        this.mtextview1 = mtextview1;
        this.mtextview2 = mtextview2;
        this.isDdoFragment = isDdoFragment;

    }

    public RecyclerViewAdater(Context mcontext, ArrayList<String> mtextview1, ArrayList<String> mtextview2,
                              ArrayList<String> mUserId, boolean isDdoFragment) {
        this.mtextview1 = mtextview1;
        this.mtextview2 = mtextview2;
        this.mUserId = mUserId;
        this.mcontext = mcontext;
        this.isDdoFragment = isDdoFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.listusers, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext, AdoDdoActivity.class);
                intent.putExtra("Id", mUserId.get(viewHolder.getAdapterPosition()));
                if (isDdoFragment)
                    intent.putExtra("isDdo", true);
                else
                    intent.putExtra("isDdo", false);
                mcontext.startActivity(intent);
            }
            });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mShowShimmer) {
            holder.shimmerFrameLayout.startShimmer();
        } else {
            holder.shimmerFrameLayout.stopShimmer();
            holder.shimmerFrameLayout.setShimmer(null);
            holder.tv1.setBackground(null);
            holder.tv2.setBackground(null);
            holder.tv1.setText(mtextview1.get(position));
            holder.tv2.setText(mtextview2.get(position));
        }

    }


    @Override
    public int getItemCount() {
        return mShowShimmer ? SHIMMER_ITEM_COUNT : mtextview1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv1;
        TextView tv2;
        RelativeLayout parentlayout;
        CardView cardView;
        ShimmerFrameLayout shimmerFrameLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parentlayout= itemView.findViewById(R.id.parent);
            tv1= itemView.findViewById(R.id.tvuser);
            tv2= itemView.findViewById(R.id.tvinfo);
            cardView = itemView.findViewById(R.id.ddo_profile_cardview);
            shimmerFrameLayout = itemView.findViewById(R.id.ado_ddo_shimmer);
        }
    }
}
