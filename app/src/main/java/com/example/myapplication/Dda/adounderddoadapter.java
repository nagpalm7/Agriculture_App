package com.example.myapplication.Dda;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Admin.RecyclerViewAdater;
import com.example.myapplication.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class adounderddoadapter extends RecyclerView.Adapter<adounderddoadapter.Holder> {

    Context context;
    ArrayList<String> ado_namelist;

    public adounderddoadapter(Context context, ArrayList<String> ado_name) {
        this.context = context;
        this.ado_namelist = ado_name;
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adounderddo,parent,false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.ado_name.setText(ado_namelist.get(position));


    }

    @Override
    public int getItemCount() {
        return ado_namelist.size();
    }

    public class Holder extends RecyclerView.ViewHolder{

        TextView ado_name;

        public Holder(@NonNull View itemView) {
            super(itemView);
            ado_name = itemView.findViewById(R.id.textViewAdo);

        }
    }
}
