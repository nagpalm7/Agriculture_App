package com.example.myapplication.Admin;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.service.autofill.Dataset;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class Barchart_fragment extends Fragment {

    private Button datebtn;
    private Button allbtn;
    private TextView pendindall;
    private TextView ongoingall;
    private TextView completedall;
    private RecyclerView barchartrecycler;
    private ArrayList<String> distlist;
    private ArrayList<Integer> pending;
    private String token ;
    private ArrayList<Integer> ongoing;
    private ArrayList<Integer> completed;
    private String date;
    final String burl="http://18.224.202.135/api/count-reports/?date=";
    private String URL;
    private barchart_adapter adapter;
    private String status;
    //private ArrayList<BarEntry>[] ctemp= new ArrayList[22];
    //private BarDataSet[] dtemp= new BarDataSet[22];

    public Barchart_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_barchart , container , false);
        datebtn=view.findViewById(R.id.datebtn);
        allbtn=view.findViewById(R.id.allbtn);
        pendindall=view.findViewById(R.id.pendingall);
        ongoingall=view.findViewById(R.id.ongoingall);
        completedall=view.findViewById(R.id.completedall);
       // barchart=view.findViewById(R.id.horbar);
        barchartrecycler=view.findViewById(R.id.barchartrecycler);
        distlist=new ArrayList<>();
        pending=new ArrayList<>();
        ongoing=new ArrayList<>();
        completed=new ArrayList<>();

        Calendar c= Calendar.getInstance();
        final int day=c.get(Calendar.DAY_OF_MONTH);
        final int month=c.get(Calendar.MONTH);
        final int year=c.get(Calendar.YEAR);

        final SharedPreferences preferences = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");

        date = year+"-"+(month + 1)+"-"+(day - 1);
        datebtn.setText(date);
        URL="http://18.224.202.135/api/count-reports/?date="+date;
        //URL="http://18.224.202.135/api/count-reports/?date=2019-11-20";
        datebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity() , new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view , int year , int month , int dayOfMonth) {
                        month=month+1;
                        date=year+"-"+month+"-"+dayOfMonth;
                        datebtn.setText(date);
                        status="false";
                        URL="http://18.224.202.135/api/count-reports/?date="+date;
                        getData(URL);
                    }
                },year,month,day-1);
                datePickerDialog.show();
            }
        });

        RequestQueue mqueue= Volley.newRequestQueue(getActivity());
        JsonObjectRequest json= new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String data1=response.toString();
                    JSONObject object=new JSONObject(data1);
                    int pen_c=Integer.valueOf(object.getString("pending_count"));
                    int ong_c=Integer.valueOf(object.getString("ongoing_count"));
                    int cp=Integer.valueOf(object.getString("completed_count"));

                    pendindall.setText(String.valueOf(pen_c));
                    ongoingall.setText(String.valueOf(ong_c));
                    completedall.setText(String.valueOf(cp));
                    /*distlist.add("TOTAL");
                    pending.add(pen_c);
                    ongoing.add(ong_c);
                    completed.add(cp);*/
                    JSONObject resultsObject = object.getJSONObject("results");
                    Iterator<String> itr = resultsObject.keys();
                    while(itr.hasNext())
                    {
                        String place = itr.next();
                        Object districtObject = resultsObject.get(place);
                        Log.d("Logs", "onResponse: place" + place + "object " + districtObject);
                        int pendingCount = ((JSONObject)districtObject).getInt("pending");
                        int ongoingCount = ((JSONObject)districtObject).getInt("ongoing");
                        int completedCount = ((JSONObject)districtObject).getInt("completed");
                        distlist.add(place);
                        //Collections.sort(distlist);
                        status="false";
                        pending.add(pendingCount);
                        ongoing.add(ongoingCount);
                        completed.add(completedCount);
                    }
                    bindUI();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error2",error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + token);
                return map;
            }
        };
        mqueue.add(json);

        allbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://18.224.202.135/api/count-reports/";
              /*  Calendar c= Calendar.getInstance();
                int day=c.get(Calendar.DAY_OF_MONTH);
                int month=c.get(Calendar.MONTH);
                int year=c.get(Calendar.YEAR); */
                String date = year+"-"+(month + 1)+"-"+day;
                datebtn.setText(date);
                status="true";
                getData(url);
                updateUI();
            }
        });



        return view;


    }

    private void getData(String url)
    {
        RequestQueue mqueue= Volley.newRequestQueue(getActivity());
        distlist.clear();
        pending.clear();
        ongoing.clear();
        completed.clear();
        // pieData.clearValues();
        JsonObjectRequest json= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String data1=response.toString();
                    JSONObject object=new JSONObject(data1);
                    int pen_c=Integer.valueOf(object.getString("pending_count"));
                    int ong_c=Integer.valueOf(object.getString("ongoing_count"));
                    int cp=Integer.valueOf(object.getString("completed_count"));

                    pendindall.setText(String.valueOf(pen_c));
                    ongoingall.setText(String.valueOf(ong_c));
                    completedall.setText(String.valueOf(cp));
                    /*distlist.add("TOTAL");
                    pending.add(pen_c);
                    ongoing.add(ong_c);
                    completed.add(cp);*/
                    JSONObject resultsObject = object.getJSONObject("results");
                    Iterator<String> itr = resultsObject.keys();
                    distlist.clear();
                    while(itr.hasNext())
                    {
                        String place = itr.next();
                        Object districtObject = resultsObject.get(place);
                        //  Log.d("Logs", "onResponse: place" + place + "object " + districtObject);
                        int pendingCount = ((JSONObject)districtObject).getInt("pending");
                        int ongoingCount = ((JSONObject)districtObject).getInt("ongoing");
                        int completedCount = ((JSONObject)districtObject).getInt("completed");
                        distlist.add(place);
                        //Collections.sort(distlist);
                        pending.add(pendingCount);
                        ongoing.add(ongoingCount);
                        completed.add(completedCount);
                    }
                    updateUI();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error2",error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + token);
                return map;
            }
        };
        mqueue.add(json);
    }

    private void bindUI()
    {

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        barchartrecycler.setLayoutManager(linearLayoutManager);
        barchartrecycler.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        adapter=new barchart_adapter(getActivity(),distlist,pending,ongoing,completed,status);
        barchartrecycler.setAdapter(adapter);
    }

    private void updateUI()
    {

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        barchartrecycler.setLayoutManager(linearLayoutManager);
        barchartrecycler.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        adapter=new barchart_adapter(getActivity(),distlist,pending,ongoing,completed,status);
        barchartrecycler.setAdapter(adapter);
    }


}
