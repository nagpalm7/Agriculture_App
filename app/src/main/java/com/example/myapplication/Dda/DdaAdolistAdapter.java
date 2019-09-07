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

import com.example.myapplication.R;

import java.util.ArrayList;

public class DdaAdolistAdapter extends RecyclerView.Adapter<DdaAdolistAdapter.MyAdoListHolder> {
    private static final String TAG = "DdaAdolistAdapter";
    private ArrayList<String> mtextview1;
    Context mcontext;

    public DdaAdolistAdapter(Context mcontext,ArrayList<String> mtextview1) {
        this.mcontext = mcontext;
        this.mtextview1 = mtextview1;
    }

    @NonNull
    @Override
    public DdaAdolistAdapter.MyAdoListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.adolist,parent,false);
        MyAdoListHolder myAdoListHolder = new MyAdoListHolder(view);
        Log.d(TAG, "onCreateViewHolder: error in this");
        return myAdoListHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DdaAdolistAdapter.MyAdoListHolder holder, int position) {
        holder.tv1.setText(mtextview1.get(position));
    }



    @Override
    public int getItemCount() {
        return mtextview1.size();
    }

    public class MyAdoListHolder extends RecyclerView.ViewHolder{
        TextView tv1;
        RelativeLayout Adolistlayout;

        public MyAdoListHolder(@NonNull View itemView) {
            super(itemView);
            Adolistlayout = itemView.findViewById(R.id.adolistlayout);
            tv1 = itemView.findViewById(R.id.adoname);
            Log.d(TAG, "AdoListHolder: error in this");

        }
    }
}
