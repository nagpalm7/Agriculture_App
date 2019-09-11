package com.example.myapplication.Dda;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
    public boolean showunassignedshimmer = true;
    private int shimmer_item_count = 6;

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
        viewHolderPendingDda.cardinassigned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!showunassignedshimmer)
                {Log.d(TAG, "onClick: item clicked");
                Intent intent = new Intent(mcontext, DdaselectAdo.class);
                intent.putExtra("Id_I_Need",id.get(viewHolderPendingDda.getAdapterPosition()));
                mcontext.startActivity(intent);
                }
            }
        });
        return viewHolderPendingDda;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPendingDda holder, int position) {


            if(showunassignedshimmer){
                holder.shimmerunassigned.startShimmer();
            }
            else {
                holder.shimmerunassigned.stopShimmer();
                holder.shimmerunassigned.setShimmer(null);
                holder.tv1.setBackground(null);
                holder.tv2.setBackground(null);
                holder.tv1.setText(mtextview1.get(position));
                holder.tv2.setText(mtextview2.get(position));
            }
    }

    @Override
    public int getItemCount() {
        return showunassignedshimmer ? shimmer_item_count : mtextview1.size();
    }

    public class ViewHolderPendingDda extends RecyclerView.ViewHolder{

        TextView tv1;
        TextView tv2;
        CardView cardinassigned;
        ShimmerFrameLayout shimmerunassigned;

        public ViewHolderPendingDda(@NonNull View itemView) {
            super(itemView);
            mcontext = itemView.getContext();
            cardinassigned = itemView.findViewById(R.id.card_unassigned);
            tv1 = itemView.findViewById(R.id.lid);
            tv2 = itemView.findViewById(R.id.address);
            shimmerunassigned = itemView.findViewById(R.id.shimmer_unassigned);
        }

    }

    public void sendlocationId(String id){
        this.id.add(id);
    }

}
