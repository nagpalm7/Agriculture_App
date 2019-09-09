package com.example.myapplication.Ado;

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
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

public class AdoListAdapter extends RecyclerView.Adapter<AdoListAdapter.AdoListHolder> {

    private static final String TAG = "AdoListAdapter";
    private ArrayList<String> mtextview1;
    private ArrayList<String> mtextview2;
    private ArrayList<String> longitude;
    private ArrayList<String> latitude;
    public Boolean mshowshimmer = true;
    private int shimmer_count = 5;
    Context mcontext;

    public AdoListAdapter(Context mcontext, ArrayList<String> mtextview1, ArrayList<String> mtextview2) {
        this.mcontext = mcontext;
        this.mtextview1 = mtextview1;
        this.mtextview2 = mtextview2;
    }

    @NonNull
    @Override
    public AdoListAdapter.AdoListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        Context context = parent.getContext();
        View view = LayoutInflater.from(mcontext).inflate(R.layout.ado_location_listview, parent, false);
        final AdoListHolder adoListHolder = new AdoListHolder(view);
        adoListHolder.Adolistlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mshowshimmer) {
                    Intent intent = new Intent(mcontext, ado_map_activity.class);
                    int position = adoListHolder.getAdapterPosition();
                    Log.d(TAG, "onClick: ");
                    intent.putExtra("longitude", longitude.get(position));
                    intent.putExtra("latitude", latitude.get(position));
                    mcontext.startActivity(intent);
                }
            }
        });



        return adoListHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdoListAdapter.AdoListHolder holder, int position) {
        if (mshowshimmer) {
            holder.shimmerFrameLayout.startShimmer();
        } else {
            holder.shimmerFrameLayout.stopShimmer();
            holder.shimmerFrameLayout.setShimmer(null);
            holder.tv1.setBackground(null);
            holder.tv2.setBackground(null);
            holder.tv1.setText(mtextview1.get(position));
            holder.tv2.setText(mtextview2.get(position));
            Log.d(TAG, "onBindViewHolder: error in this");

        }

    }

    @Override
    public int getItemCount() {
        return mshowshimmer ? shimmer_count : mtextview1.size();
    }

    public class AdoListHolder extends RecyclerView.ViewHolder {
        TextView tv1;
        TextView tv2;
        RelativeLayout Adolistlayout;
        ShimmerFrameLayout shimmerFrameLayout;

        public AdoListHolder(@NonNull View itemView) {
            super(itemView);
            Adolistlayout = itemView.findViewById(R.id.ado_loaction_parent);
            tv1 = itemView.findViewById(R.id.lname);
            tv2 = itemView.findViewById(R.id.laddress);
            shimmerFrameLayout = itemView.findViewById(R.id.ado_location_shimmer);
            Log.d(TAG, "AdoListHolder: error in this");

        }
    }

    public void sendPostion(ArrayList<String> longitude, ArrayList<String> latitdue) {
        this.longitude = longitude;
        this.latitude = latitdue;


    }
}
