package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ado_fragment extends Fragment {
    private ArrayList<String> mtextview1=null;
    private ArrayList<String> mtextview2=null;

    private final String TAG = "ddo_fragment";

    public ado_fragment(){
        mtextview1 = new ArrayList<String>(3);
        mtextview2 = new ArrayList<String>(3);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ado_fragment,container,false);
        for(int i=0;i<3;i++){
            mtextview1.add("User Name");
            mtextview2.add("User Information");

        }
        RecyclerViewAdater recyclerViewAdater = new RecyclerViewAdater(getActivity(),mtextview1,mtextview2);
        RecyclerView Rview = view.findViewById(R.id.recyclerViewado);
        Rview.setAdapter(recyclerViewAdater);
        Rview.setLayoutManager( new LinearLayoutManager(getActivity()));
        return view;
    }
}
