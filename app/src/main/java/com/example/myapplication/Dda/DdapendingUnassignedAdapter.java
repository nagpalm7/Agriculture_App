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
    private Context mcontext;
    private ArrayList<String> id;

    public DdapendingUnassignedAdapter(Context mcontext, ArrayList<String> mtextview1, ArrayList<String> mtextview2) {
        this.mcontext = mcontext;
        this.mtextview1 = mtextview1;
        this.mtextview2 = mtextview2;
        this.id = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolderPendingDda onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.notassignedlist,parent,false);
        final ViewHolderPendingDda viewHolderPendingDda = new ViewHolderPendingDda(view);
        viewHolderPendingDda.parentnotassigned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: item clicked");
                Intent intent = new Intent(mcontext, DdaselectAdo.class);
                intent.putExtra("Id_I_Need",id.get(viewHolderPendingDda.getAdapterPosition()));
                mcontext.startActivity(intent);
            }
        });
        return viewHolderPendingDda;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPendingDda holder, int position) {
            holder.tv1.setText(mtextview1.get(position));
            holder.tv2.setText(mtextview2.get(position));
    }

    @Override
    public int getItemCount() {
        return mtextview1.size();
    }

    public class ViewHolderPendingDda extends RecyclerView.ViewHolder{

        TextView tv1;
        TextView tv2;
        RelativeLayout parentnotassigned;

        public ViewHolderPendingDda(@NonNull View itemView) {
            super(itemView);
            mcontext = itemView.getContext();
            parentnotassigned = itemView.findViewById(R.id.parentnotassigned);
            tv1 = itemView.findViewById(R.id.lid);
            tv2 = itemView.findViewById(R.id.address);
        }

    }

    public void sendlocationId(String id){
        this.id.add(id);
    }

}
