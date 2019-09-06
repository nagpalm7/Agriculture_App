package com.example.myapplication.Dda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class DdacompletedAdapter extends RecyclerView.Adapter<DdacompletedAdapter.ViewHolderCompletedDda> {
    private static final String TAG = "DdacompletedAdapter";
    ArrayList<String> mtextview1;
    ArrayList<String> mtextview2;
    ArrayList<String> mtextview3;
    Context mcontext;

    public DdacompletedAdapter(Context mcontext, ArrayList<String> mtextview1, ArrayList<String> mtextview2, ArrayList<String> mtextview3) {
        this.mcontext = mcontext;
        this.mtextview1 = mtextview1;
        this.mtextview2 = mtextview2;
        this.mtextview3 = mtextview3;
    }

    @NonNull
    @Override
    public DdacompletedAdapter.ViewHolderCompletedDda onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.notassignedlist,parent,false);
        ViewHolderCompletedDda viewHolderCompletedDda = new ViewHolderCompletedDda(view);
        return viewHolderCompletedDda;
    }

    @Override
    public void onBindViewHolder(@NonNull DdacompletedAdapter.ViewHolderCompletedDda holder, int position) {
        holder.tv1.setText(mtextview1.get(position));
        holder.tv2.setText(mtextview2.get(position));
        holder.tv3.setText(mtextview3.get(position));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolderCompletedDda extends RecyclerView.ViewHolder{

        TextView tv1;
        TextView tv2;
        TextView tv3;
        RelativeLayout parentnotassigned;

        public ViewHolderCompletedDda(@NonNull View itemView) {
            super(itemView);
            mcontext = itemView.getContext();
            parentnotassigned = itemView.findViewById(R.id.parentnotassigned);
            tv1 = itemView.findViewById(R.id.date);
            tv2 = itemView.findViewById(R.id.Time);
            tv3 = itemView.findViewById(R.id.address);

        }

    }
}
