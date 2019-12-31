package com.example.myapplication.Admin;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class barchart_adapter extends RecyclerView.Adapter<barchart_adapter.MyViewHolder> {

    Context context;
    ArrayList<String> distlist;
    ArrayList<Integer> pending;
    ArrayList<Integer> ongoing;
    ArrayList<Integer> completed;
    BarData data;
    BarDataSet dataSet;
    HorizontalBarChart hbarchart;
    ArrayList<BarEntry> chartfile;
    ArrayList<Integer> colors;
    ArrayList<LegendEntry> legendEntries;
    String status;

    public barchart_adapter(Context context, ArrayList<String> distlist, ArrayList<Integer> pending,
                            ArrayList<Integer> ongoing, ArrayList<Integer> completed, String status)
    {
        this.context=context;
        this.distlist=distlist;
        this.pending=pending;
        this.completed=completed;
        this.ongoing=ongoing;
        this.status=status;

    }
    @NonNull
    @Override
    public barchart_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent , int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.barchart_recycler,parent,false);
        barchart_adapter.MyViewHolder vh=new barchart_adapter.MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull barchart_adapter.MyViewHolder holder , int position) {

        holder.testdist.setText(distlist.get(position));
        chartfile=new ArrayList<>();

        colors=new ArrayList<>();
        colors.add(context.getResources().getColor(R.color.pending));
        colors.add(context.getResources().getColor(R.color.ongoing));
        colors.add(context.getResources().getColor(R.color.completed));

        Legend legend = holder.barchart.getLegend();
        legend.setEnabled(false);
        chartfile.add(new BarEntry(0f,(float) completed.get(position)));
        // chartfile.add(new BarEntry(0f,(float)pending.get(position),0f));
        chartfile.add(new BarEntry(1f,(float) ongoing.get(position)));
        chartfile.add(new BarEntry(2f,(float)pending.get(position)));
        // chartfile.add(new BarEntry(2f,(float) completed.get(position),2f));

        if(position%2==0){
            // holder.linearcount.setBackgroundColor(707070);
            holder.barchartback.setBackgroundColor(context.getResources().getColor(R.color.grey_bar));
        }
        else{
            holder.barchartback.setBackgroundColor(context.getResources().getColor(R.color.white));
        }

        // legend.setEnabled(false);
        dataSet=new BarDataSet(chartfile,"");
        // dataSet.setColors(ColorTemplate.PASTEL_COLORS);
        dataSet.setDrawValues(true);
       // dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(10);
        //dataSet.setBarBorderWidth(5f);
        dataSet.setColors(colors);
        data= new BarData(dataSet);
        data.setBarWidth(0.6f);

       /* holder.barchart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e , Highlight h) {
                Toast.makeText(context,"The value is "+e.getY(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        }); */
        holder.barchart.setDrawGridBackground(false);
        holder.barchart.getXAxis().setDrawGridLines(false);
        holder.barchart.setDrawValueAboveBar(true);
       // holder.barchart.getXAxis().setDrawAxisLine(true);
        holder.barchart.getAxisLeft().setDrawGridLines(false);
        holder.barchart.getAxisRight().setDrawGridLines(false);
        holder.barchart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        holder.barchart.getXAxis().setDrawGridLinesBehindData(false);
        holder.barchart.getXAxis().setDrawLabels(false);
        holder.barchart.setScaleEnabled(false);
        holder.barchart.setDoubleTapToZoomEnabled(false);
        holder.barchart.getAxisLeft().setAxisMinimum(0f);
        holder.barchart.setViewPortOffsets(0f,0f,0f,0f);
        //holder.barchart.setExtraBottomOffset(20f);
        if(status.equals("true")){
           holder.barchart.getAxisLeft().setAxisMaximum(2000f);
        }
        else if(status.equals("false")){
            holder.barchart.getAxisLeft().setAxisMaximum(100f);
        }
        //holder.barchart.getXAxis().setEnabled(false);
         holder.barchart.getAxisRight().setEnabled(false);
        // holder.barchart.setDrawValueAboveBar(true);
        holder.barchart.getAxisLeft().setEnabled(false);
        holder.barchart.animateY(2000);
        holder.barchart.setData(data);
        holder.barchart.getDescription().setText("");

    }

    @Override
    public int getItemCount() {
        return distlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView testdist;
        HorizontalBarChart barchart;
        LinearLayout barchartback;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            testdist=itemView.findViewById(R.id.textdist);
            barchart=itemView.findViewById(R.id.barchart);
            barchartback=itemView.findViewById(R.id.barchartback);
        }
    }
}
