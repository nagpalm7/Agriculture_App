package com.example.myapplication.Dda;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DdaAdoListAdapter extends RecyclerView.Adapter<DdaAdoListAdapter.AdoListViewHolder> {
    private static final String TAG = "DdaAdoListAdapter";

    private ArrayList<String> mtextview1;
    private ArrayList<String> mtextview2;
    private Context mcontext;
    private String locationid;
    private String adoid;
    private String urlpatch;
    private String token;

    public DdaAdoListAdapter(Context mcontext,ArrayList<String> mtextview1, ArrayList<String> mtextview2) {
        this.mcontext = mcontext;
        this.mtextview1 = mtextview1;
        this.mtextview2 = mtextview2;
    }

    @NonNull
    @Override
    public AdoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(mcontext).inflate(R.layout.ddaselectadolist,parent,false);
        AdoListViewHolder adoListViewHolder = new AdoListViewHolder(view);
        Log.d(TAG, "onCreateViewHolder:");



        adoListViewHolder.btnassign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdialogbox("", "Press OK for confirmation", "Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences preferences = mcontext.getSharedPreferences("tokenFile",Context.MODE_PRIVATE);
                        token = preferences.getString("token", "");
                        Log.d(TAG, "onClick: TOKEN"+token);
                        try {
                            final JSONObject postbody = new JSONObject();
                            postbody.put("ado", adoid);
                            final RequestQueue requestQueue = Volley.newRequestQueue(mcontext);
                            urlpatch = "http://13.235.100.235:8000/api/location/"+locationid+"/";
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, urlpatch, postbody, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Toast.makeText(mcontext,"Location assigned",Toast.LENGTH_SHORT).show();
                                    ((Activity)mcontext).finish();
                                    try {
                                        JSONObject c = new JSONObject(String.valueOf(response));
                                        Log.d(TAG, "onResponse: " + c);
                                    }catch (JSONException e){
                                        Log.d(TAG, "onResponse: "+e);
                                    }

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d(TAG, "onErrorResponse: "+error);
                                    Toast.makeText(mcontext,"Ado not assigned.Please try again",Toast.LENGTH_LONG).show();
                                }
                            }) {
                                @Override
                                public Map<String, String> getHeaders() {
                                    HashMap<String, String> headers = new HashMap<>();
                                    headers.put("Authorization", "Token " + token);
                                    return headers;
                                }
                            };
                            requestQueue.add(jsonObjectRequest);
                        }catch (JSONException e){
                            Log.d(TAG, "onClick: "+e);
                            e.printStackTrace();
                        }
                    }
                }, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                },false);
            }
        });
        return adoListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdoListViewHolder holder, int position) {
        holder.tv1.setText(mtextview1.get(position));
        holder.tv2.setText(mtextview2.get(position));
    }

    @Override
    public int getItemCount() {
        return mtextview1.size();
    }

    public class AdoListViewHolder extends RecyclerView.ViewHolder{
        TextView tv1;
        TextView tv2;
        RelativeLayout adolistlayout;
        Button btnassign;

        public AdoListViewHolder(@NonNull View itemView) {
            super(itemView);
            mcontext = itemView.getContext();
            adolistlayout = (RelativeLayout) itemView.findViewById(R.id.adolistlayout);
            tv1 = itemView.findViewById(R.id.nameofado);
            tv2 = itemView.findViewById(R.id.nameofvillage);
            btnassign = itemView.findViewById(R.id.btnAssign);
        }



    }


    //made function to show dialogbox
    private AlertDialog showdialogbox(String title, String msg, String positiveLabel, DialogInterface.OnClickListener positiveOnclick,
                                      String negativeLabel, DialogInterface.OnClickListener negativeOnclick,
                                      boolean isCancelable){

        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveLabel, positiveOnclick);
        builder.setNegativeButton(negativeLabel, negativeOnclick);
        builder.setCancelable(isCancelable);
        AlertDialog alert = builder.create();
        Log.d(TAG, "showdialogbox: "+alert);
        alert.show();
        return alert;
    }

    public void getlocationid(String lid){
        this.locationid = lid;
    }

    public void getadoid(String adoid){
        this.adoid = adoid;
    }
}
