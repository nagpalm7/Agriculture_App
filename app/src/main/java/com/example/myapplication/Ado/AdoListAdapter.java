package com.example.myapplication.Ado;

import android.content.Context;
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

public class AdoListAdapter extends RecyclerView.Adapter<AdoListAdapter.AdoListHolder> {

    private static final String TAG = "AdoListAdapter";
    private ArrayList<String> mtextview1;
    private ArrayList<String> mtextview2;
    Context mcontext;

    public AdoListAdapter(Context mcontext, ArrayList<String> mtextview1,ArrayList<String> mtextview2) {
        this.mcontext = mcontext;
        this.mtextview1 = mtextview1;
        this.mtextview2 = mtextview2;
    }

    @NonNull
    @Override
    public AdoListAdapter.AdoListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        Context context = parent.getContext();
        View view = LayoutInflater.from(mcontext).inflate(R.layout.ado_location_listview, parent, false);
        AdoListHolder adoListHolder = new AdoListHolder(view);
        Log.d(TAG, "onCreateViewHolder: error in this");
        return adoListHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdoListAdapter.AdoListHolder holder, int position) {
        holder.tv1.setText(mtextview1.get(position));
        holder.tv2.setText(mtextview2.get(position));
        Log.d(TAG, "onBindViewHolder: error in this");
    }

    @Override
    public int getItemCount() {
        return mtextview1.size();
    }

    public class AdoListHolder extends RecyclerView.ViewHolder {
        TextView tv1;
        TextView tv2;
        RelativeLayout Adolistlayout;

        public AdoListHolder(@NonNull View itemView) {
            super(itemView);
            Adolistlayout = itemView.findViewById(R.id.ado_loaction_parent);
            tv1 = itemView.findViewById(R.id.lname);
            tv2 = itemView.findViewById(R.id.laddress);
            Log.d(TAG, "AdoListHolder: error in this");

        }
    }
}
