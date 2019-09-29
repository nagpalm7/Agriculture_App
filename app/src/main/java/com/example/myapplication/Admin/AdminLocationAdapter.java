

package com.example.myapplication.Admin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Ado.ReviewReport;
import com.example.myapplication.R;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

public class AdminLocationAdapter extends RecyclerView.Adapter<AdminLocationAdapter.ViewHolder>{
    ArrayList<String> mtextview1;
    ArrayList<String> mtextview2;
    ArrayList<String> mtextview3;
    private ArrayList<String> mIds;
    private boolean isComplete = false;
    private boolean isOngoing = false;
    boolean mShowShimmer = true;
    private int SHIMMER_ITEM_NO = 5;
    Context mcontext;

    public AdminLocationAdapter(Context mcontext, ArrayList<String> mtextview1, ArrayList<String> mtextview2, ArrayList<String> mtextview3, ArrayList<String> mIds, boolean isOngoing) {
        this.mcontext = mcontext;
        this.mtextview1 = mtextview1;
        this.mtextview2 = mtextview2;
        this.mtextview3 = mtextview3;
        this.isOngoing = isOngoing;
        this.mIds = mIds;
    }

    public AdminLocationAdapter(Context mcontext, ArrayList<String> mtextview1, ArrayList<String> mtextview2, ArrayList<String> mtextview3, boolean isComplete, ArrayList<String> mIds) {
        this.mcontext = mcontext;
        this.mtextview1 = mtextview1;
        this.mtextview2 = mtextview2;
        this.mtextview3 = mtextview3;
        this.mIds = mIds;
        this.isComplete = isComplete;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.adminlocationlist,parent,false);
        final ViewHolder viewHolderDda = new ViewHolder(view);
        viewHolderDda.parentnotassigned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mShowShimmer) {
                    if (isComplete || isOngoing) {
                        Intent intent = new Intent(mcontext, ReviewReport.class);
                        int pos = viewHolderDda.getAdapterPosition();
                        intent.putExtra("id", mIds.get(pos));
                        intent.putExtra("isDdo", true);
                        intent.putExtra("isAdmin", true);
                        if (isOngoing) {
                            intent.putExtra("isOngoing", true);
                        }
                        mcontext.startActivity(intent);
                    }
                }
            }
        });
        return viewHolderDda;
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
            holder.tv3.setBackground(null);
            holder.tv1.setText("DDA     : " + mtextview1.get(position).toUpperCase());
            holder.tv2.setText("ADO     : " + mtextview2.get(position).toUpperCase());
            holder.tv3.setText(mtextview3.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mShowShimmer ? SHIMMER_ITEM_NO : mtextview1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv1;
        TextView tv2;
        TextView tv3;
        RelativeLayout parentnotassigned;
        ShimmerFrameLayout shimmerFrameLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parentnotassigned = itemView.findViewById(R.id.adminlocation);
            tv1 = itemView.findViewById(R.id.dda_name);
            tv2 = itemView.findViewById(R.id.ada_name);
            tv3 = itemView.findViewById(R.id.address);
            shimmerFrameLayout = itemView.findViewById(R.id.locations_shimmer);
        }


    }

}