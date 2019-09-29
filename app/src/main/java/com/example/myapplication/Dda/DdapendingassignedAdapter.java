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
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

public class DdapendingassignedAdapter extends RecyclerView.Adapter<DdapendingassignedAdapter.ViewHolderAssignedDda> {
    private static final String TAG = "DdapendingassignedAdapt";
    ArrayList<String> mtextview1;
    ArrayList<String> mtextview2;
    ArrayList<String> mtextview3;
    Context mcontext;
    public boolean showassignedshimmer = true;
    private int shimmer_item_count = 6;

    public DdapendingassignedAdapter(Context mcontext, ArrayList<String> mtextview1, ArrayList<String> mtextview2,ArrayList<String> mtextview3) {
        this.mcontext = mcontext;
        this.mtextview1 = mtextview1;
        this.mtextview2 = mtextview2;
        this.mtextview3 = mtextview3;
    }

    @NonNull
    @Override
    public DdapendingassignedAdapter.ViewHolderAssignedDda onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mcontext).inflate(R.layout.ddaadoassignedlist,parent,false);
        ViewHolderAssignedDda viewHolderAssignedDda = new ViewHolderAssignedDda(view);
        return viewHolderAssignedDda;
    }

    @Override
    public void onBindViewHolder(@NonNull DdapendingassignedAdapter.ViewHolderAssignedDda holder, int position) {

        if(showassignedshimmer){
            holder.shimmerassigned.startShimmer();
        }
        else {
            holder.shimmerassigned.stopShimmer();
            holder.shimmerassigned.setShimmer(null);

            holder.tv2.setBackground(null);
            holder.tv3.setBackground(null);

            holder.tv2.setText(mtextview3.get(position).toUpperCase());
            holder.tv3.setText(mtextview2.get(position).toUpperCase());
        }
    }

    @Override
    public int getItemCount() {
        return showassignedshimmer ? shimmer_item_count : mtextview1.size();
    }

    public class ViewHolderAssignedDda extends RecyclerView.ViewHolder{

        TextView tv2;
        TextView tv3;
        RelativeLayout cardassigned;
        ShimmerFrameLayout shimmerassigned;

        public ViewHolderAssignedDda(@NonNull View itemView) {
            super(itemView);
            mcontext = itemView.getContext();
            cardassigned = itemView.findViewById(R.id.card_assigned);
            tv2 = itemView.findViewById(R.id.adoassignedname);
            tv3 = itemView.findViewById(R.id.adoassignedaddress);
            shimmerassigned = itemView.findViewById(R.id.shimmer_assigned);

        }

    }
}
