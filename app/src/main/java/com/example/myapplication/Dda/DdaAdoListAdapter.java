package com.example.myapplication.Dda;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class DdaAdoListAdapter extends RecyclerView.Adapter<DdaAdoListAdapter.AdoListViewHolder> {
    private static final String TAG = "DdaAdoListAdapter";

    private ArrayList<String> mtextview1;
    private ArrayList<String> mtextview2;
    private Context mcontext;

    public DdaAdoListAdapter(Context mcontext,ArrayList<String> mtextview1, ArrayList<String> mtextview2) {
        this.mcontext = mcontext;
        this.mtextview1 = mtextview1;
        this.mtextview2 = mtextview2;
    }

    @NonNull
    @Override
    public DdaAdoListAdapter.AdoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(mcontext).inflate(R.layout.ddaselectadolist,parent,false);
        AdoListViewHolder adoListViewHolder = new AdoListViewHolder(view);
        Log.d(TAG, "onCreateViewHolder: error in this.....");
        return adoListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DdaAdoListAdapter.AdoListViewHolder holder, int position) {
        holder.tv1.setText(mtextview1.get(position));
        holder.tv2.setText(mtextview2.get(position));

        holder.btnassign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mtextview1.size();
    }

    public class AdoListViewHolder extends RecyclerView.ViewHolder{
        TextView tv1;
        TextView tv2;
        RelativeLayout adolistlayout;
        Button btnassign;
        public AdoListViewHolder(@NonNull View itemView) {
            super(itemView);
            mcontext = itemView.getContext();
            adolistlayout = itemView.findViewById(R.id.adolistlayout);
            tv1 = itemView.findViewById(R.id.nameofado);
            tv2 = itemView.findViewById(R.id.nameofvillage);
            btnassign = itemView.findViewById(R.id.btnAssign);
        }
    }
}
