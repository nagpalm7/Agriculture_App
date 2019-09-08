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

public class DdapendingassignedAdapter extends RecyclerView.Adapter<DdapendingassignedAdapter.ViewHolderAssignedDda> {
    private static final String TAG = "DdapendingassignedAdapt";
    ArrayList<String> mtextview1;
    ArrayList<String> mtextview2;
    Context mcontext;

    public DdapendingassignedAdapter(Context mcontext, ArrayList<String> mtextview1, ArrayList<String> mtextview2) {
        this.mcontext = mcontext;
        this.mtextview1 = mtextview1;
        this.mtextview2 = mtextview2;
    }

    @NonNull
    @Override
    public DdapendingassignedAdapter.ViewHolderAssignedDda onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.notassignedlist,parent,false);
        ViewHolderAssignedDda viewHolderAssignedDda = new ViewHolderAssignedDda(view);
        return viewHolderAssignedDda;
    }

    @Override
    public void onBindViewHolder(@NonNull DdapendingassignedAdapter.ViewHolderAssignedDda holder, int position) {
        holder.tv1.setText(mtextview1.get(position));
        holder.tv2.setText(mtextview2.get(position));
    }

    @Override
    public int getItemCount() {
        return mtextview1.size();
    }

    public class ViewHolderAssignedDda extends RecyclerView.ViewHolder{

        TextView tv1;
        TextView tv2;
        RelativeLayout parentnotassigned;

        public ViewHolderAssignedDda(@NonNull View itemView) {
            super(itemView);
            mcontext = itemView.getContext();
            parentnotassigned = itemView.findViewById(R.id.parentnotassigned);
            tv1 = itemView.findViewById(R.id.lid);
            tv2 = itemView.findViewById(R.id.address);

        }

    }
}
