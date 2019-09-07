package com.example.myapplication.Admin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Admin.DdoActivity.DdoActivity;
import com.example.myapplication.R;

import java.util.ArrayList;

public class RecyclerViewAdater extends RecyclerView.Adapter<RecyclerViewAdater.ViewHolder> {

    ArrayList<String> mtextview1;
    ArrayList<String> mtextview2;
    Context mcontext;
    private boolean isDdoFragment;

    public RecyclerViewAdater(Context mcontext, ArrayList<String> mtextview1, ArrayList<String> mtextview2, boolean isDdoFragment) {
        this.mcontext = mcontext;
        this.mtextview1 = mtextview1;
        this.mtextview2 = mtextview2;
        this.isDdoFragment = isDdoFragment;

    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.listusers,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        if (isDdoFragment) {
            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mcontext, DdoActivity.class);
                    intent.putExtra("ddoId", mtextview2.get(viewHolder.getAdapterPosition()));
                    mcontext.startActivity(intent);
                }
            });
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv1.setText(mtextview1.get(position));
        holder.tv2.setText(mtextview2.get(position));

    }


    @Override
    public int getItemCount() {
        return mtextview1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv1;
        TextView tv2;
        RelativeLayout parentlayout;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parentlayout= itemView.findViewById(R.id.parent);
            tv1= itemView.findViewById(R.id.tvuser);
            tv2= itemView.findViewById(R.id.tvinfo);
            cardView = itemView.findViewById(R.id.ddo_profile_cardview);
        }
    }
}
