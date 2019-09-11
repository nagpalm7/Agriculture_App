package com.example.myapplication.Dda;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.facebook.shimmer.ShimmerFrameLayout;


import java.util.ArrayList;

public class DdaongoingAdapter extends RecyclerView.Adapter<DdaongoingAdapter.ViewHolderOngoingDda> {
    private ArrayList<String> mtextview1;
    private ArrayList<String> mtextview2;
    private Context mcontext;
    public boolean showongoingshimmer = true;
    private int shimmer_item_count = 6;

    public DdaongoingAdapter(Context mcontext, ArrayList<String> mtextview1, ArrayList<String> mtextview2) {
        this.mcontext = mcontext;
        this.mtextview1 = mtextview1;
        this.mtextview2 = mtextview2;
    }

    @NonNull
    @Override
    public ViewHolderOngoingDda onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.notassignedlist, parent, false);
        ViewHolderOngoingDda viewHolderOngoingDda = new ViewHolderOngoingDda(view);
        return viewHolderOngoingDda;
    }

    @Override
    public void onBindViewHolder(@NonNull DdaongoingAdapter.ViewHolderOngoingDda holder, int position) {

        if(showongoingshimmer){
            holder.shimmerongoing.startShimmer();
        }
        else {
            holder.shimmerongoing.stopShimmer();
            holder.shimmerongoing.setShimmer(null);
            holder.tv1.setBackground(null);
            holder.tv2.setBackground(null);
            holder.tv1.setText(mtextview1.get(position));
            holder.tv2.setText(mtextview2.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return showongoingshimmer ? shimmer_item_count : mtextview1.size();
    }

    public class ViewHolderOngoingDda extends RecyclerView.ViewHolder {

        TextView tv1;
        TextView tv2;
        CardView cardongoing;
        ShimmerFrameLayout shimmerongoing;

        public ViewHolderOngoingDda(@NonNull View itemView) {
            super(itemView);
            mcontext = itemView.getContext();
            tv1 = itemView.findViewById(R.id.lid);
            tv2 = itemView.findViewById(R.id.address);
            cardongoing = itemView.findViewById(R.id.card_unassigned);
            shimmerongoing = itemView.findViewById(R.id.shimmer_unassigned);
        }

    }
}
