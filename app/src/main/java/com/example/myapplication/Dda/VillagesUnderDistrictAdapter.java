package com.example.myapplication.Dda;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class VillagesUnderDistrictAdapter extends RecyclerView.Adapter<VillagesUnderDistrictAdapter.CustomViewHolder> {

    private ArrayList<String> villageNames;
    private ArrayList<Integer> villageIds;
    private Context mContext;
    private SparseBooleanArray sparseBooleanArray;
    private SparseArray<Integer> selectedVillageIds;
    private ArrayList<Integer> currentVillagesPos;

    public VillagesUnderDistrictAdapter(Context mContext, ArrayList<String> villageNames, ArrayList<Integer> villageIds) {
        this.villageNames = villageNames;
        this.mContext = mContext;
        this.villageIds = villageIds;
        sparseBooleanArray = new SparseBooleanArray();
        selectedVillageIds = new SparseArray<>();
        currentVillagesPos = new ArrayList<>();
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.village_under_district_item, parent, false);
        final CustomViewHolder viewHolder = new CustomViewHolder(view);
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                toggleSelection(viewHolder.getAdapterPosition());
                Log.d("SELECTIONS ", "onBindViewHolder: " + b);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.villageTextView.setText(villageNames.get(position));
        Log.d("ADAPTER", "onBindViewHolder: " + currentVillagesPos);
        if (currentVillagesPos.contains(position)) {
            holder.checkBox.setChecked(true);
            toggleSelection(position);
        }
    }

    @Override
    public int getItemCount() {
        return villageNames.size();
    }

    public void toggleSelection(int pos) {
        selectView(pos, !sparseBooleanArray.get(pos));
    }

    private void selectView(int pos, boolean value) {
        if (value) {
            sparseBooleanArray.put(pos, value);
            selectedVillageIds.put(pos, villageIds.get(pos));
        } else {
            sparseBooleanArray.delete(pos);
            selectedVillageIds.delete(pos);
        }
    }

    public SparseArray<Integer> getSelectedVillageIds() {
        Log.d("ADAPTER", "getSelectedVillageIds: " + selectedVillageIds);
        return selectedVillageIds;
    }

    public void addtoCurrentVillagesPos(int pos) {
        currentVillagesPos.add(pos);
        Log.d("ADAPTER", "addtoCurrentVillagesPos: " + pos);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView villageTextView;
        CheckBox checkBox;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            villageTextView = itemView.findViewById(R.id.vill_name);
            checkBox = itemView.findViewById(R.id.village_checkbox);
        }
    }
}
