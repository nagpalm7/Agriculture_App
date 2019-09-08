package com.example.myapplication.Dda;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.Ado.ado_map_activity;
import com.example.myapplication.R;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

public class DdapendingUnassignedAdapter extends RecyclerView.Adapter<DdapendingUnassignedAdapter.ViewHolderPendingDda>{
    private static final String TAG = "DdaUnassignedAdapter";
    private ArrayList<String> mtextview1;
    private ArrayList<String> mtextview2;
    private ArrayList<String> mtextview3;
    private ArrayList<String> mtextview4;
    private Context mcontext;
//    private boolean Ushimmer = true;
//    private int Ushimmer_count = 5;

    public DdapendingUnassignedAdapter(Context mcontext, ArrayList<String> mtextview1, ArrayList<String> mtextview2, ArrayList<String> mtextview3,ArrayList<String> mtextview4) {
        this.mcontext = mcontext;
        this.mtextview1 = mtextview1;
        this.mtextview2 = mtextview2;
        this.mtextview3 = mtextview3;
        this.mtextview4 = mtextview4;
    }

    @NonNull
    @Override
    public ViewHolderPendingDda onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.notassignedlist,parent,false);
        ViewHolderPendingDda viewHolderPendingDda = new ViewHolderPendingDda(view);
//        if (!Ushimmer) {
//            viewHolderPendingDda.parentnotassigned.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(mcontext, DdaselectAdo.class);
//                    mcontext.startActivity(intent);
//                }
//            });
//        }
        Log.d(TAG, "onCreateViewHolder: error in this");
        return viewHolderPendingDda;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPendingDda holder, int position) {
//        if(Ushimmer){
//            holder.UshimmerFrameLayout.startShimmer();
//        }else{
//            holder.UshimmerFrameLayout.stopShimmer();
//            holder.UshimmerFrameLayout.setShimmer(null);
//            holder.tv1.setBackground(null);
//            holder.tv2.setBackground(null);
//            holder.tv3.setBackground(null);
//            holder.tv4.setBackground(null);
            holder.tv1.setText(mtextview1.get(position));
            holder.tv2.setText(mtextview2.get(position));
            holder.tv3.setText(mtextview3.get(position));
            holder.tv4.setText(mtextview4.get(position));
//        }
        Log.d(TAG, "onBindViewHolder: error in this");


        holder.parentnotassigned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: item clicked");
                Intent intent = new Intent(mcontext, DdaselectAdo.class);
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mtextview1.size();
    }

    public class ViewHolderPendingDda extends RecyclerView.ViewHolder{

        TextView tv1;
        TextView tv2;
        TextView tv3;
        TextView tv4;
        RelativeLayout parentnotassigned;
//        ShimmerFrameLayout UshimmerFrameLayout;

        public ViewHolderPendingDda(@NonNull View itemView) {
            super(itemView);
            mcontext = itemView.getContext();
            parentnotassigned = itemView.findViewById(R.id.parentnotassigned);
            tv1 = itemView.findViewById(R.id.lid);
            tv2 = itemView.findViewById(R.id.date);
            tv3 = itemView.findViewById(R.id.Time);
            tv4 = itemView.findViewById(R.id.address);
//            UshimmerFrameLayout = itemView.findViewById(R.id.shimmer_notassigned);
        }

    }

}
