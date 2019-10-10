package com.example.myapplication.Admin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class pending_fragment extends Fragment {

    //vars
    private ArrayList<String> mDdaName;
    private ArrayList<String> mAdaName;
    private ArrayList<String> mAddress;
    private ArrayList<String> mpkado;
    private ArrayList<String> mpkdda;
    private String token;
    private String villagename;
    private String blockname;
    private String district;

    //tags
    private static final String TAG = "pending_fragment";
    private String url_unassigned = "http://18.224.202.135/api/locations/unassigned";
    private String url_assigned = "http://18.224.202.135/api/locations/assigned";
    private String next_unassigned_url = "null";
    private String next_assigned_url = "null";
    private LinearLayoutManager layoutManager;
    private AdminLocationAdapter recyclerViewAdater;
    private ProgressBar progressBar;
    private int NEXT_LOCATION_COUNT = 1;
    private boolean isNextBusy;
    private boolean isSendingNotifications = false;
    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;

    public pending_fragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.pending_fragment, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewpending);
        progressBar = view.findViewById(R.id.locations_loading);
        swipeRefreshLayout = view.findViewById(R.id.refreshpull4);
        FloatingActionButton notificationButton = view.findViewById(R.id.notifications_button);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFragmentManager().beginTransaction().detach(pending_fragment.this).attach(pending_fragment.this).commit();
            }
        });
        mDdaName = new ArrayList<>();
        mAdaName = new ArrayList<>();
        mAddress = new ArrayList<>();
        mpkado = new ArrayList<>();
        mpkdda = new ArrayList<>();
        recyclerViewAdater = new AdminLocationAdapter(getActivity(), mDdaName, mAdaName,true, mAddress, null,mpkado,mpkdda);
        recyclerView.setAdapter(recyclerViewAdater);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(divider);
        final SharedPreferences preferences = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");
        Log.d(TAG, "onCreateView: " + token);

        Log.d(TAG, "onCreateView: inflated fragment_ongoing");

        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        final JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, url_assigned, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    next_assigned_url = jsonObject.getString("next");
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    if (jsonArray.length() == 0 && mDdaName.isEmpty()) {
                        recyclerViewAdater.mShowShimmer = false;
                        recyclerViewAdater.notifyDataSetChanged();
                        view.setBackground(getActivity().getResources().getDrawable(R.mipmap.no_entry_background));
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        JSONObject adoobj = c.getJSONObject("ado");
                        JSONObject authado = adoobj.getJSONObject("auth_user");
                        mpkado.add(authado.getString("pk"));

                        JSONObject ddaobj = c.getJSONObject("dda");
                        JSONObject authddo = ddaobj.getJSONObject("auth_user");
                        mpkdda.add(authddo.getString("pk"));
                        Log.d(TAG, "onResponse: DDA " + authddo.getString("pk"));
                        villagename = c.getString("village_name");
                        blockname = c.getString("block_name");
                        district = c.getString("district");

                        try {
                            JSONObject mDdaObject = c.getJSONObject("dda");
                            String ddaName = mDdaObject.getString("name");
                            mDdaName.add(ddaName);
                        } catch (JSONException e) {
                            mDdaName.add("Not Assigned");
                        }
                        try {
                            JSONObject mAdoObject = c.getJSONObject("ado");
                            Log.d(TAG, "onResponse: try block");
                            String adoName = mAdoObject.getString("name");
                            Log.d(TAG, "onResponse: adoname " + adoName);
                            mAdaName.add(adoName);
                        } catch (JSONException e) {
                            mAdaName.add("Not Assigned");
                            Log.d(TAG, "exception: ");
                        }
                        mAddress.add(villagename.toUpperCase() + ", " + blockname.toUpperCase() +
                                ", " + district.toUpperCase());
                        Log.d(TAG, "onResponse: next");
                    }
                    recyclerViewAdater.mShowShimmer = false;
                    recyclerViewAdater.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                    e.printStackTrace();
                    recyclerViewAdater.mShowShimmer = false;
                    recyclerViewAdater.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + token);
                return map;
            }
        };

        final JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, url_unassigned, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    next_unassigned_url = jsonObject.getString("next");
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        try {
                            JSONObject adoobj = c.getJSONObject("ado");
                            JSONObject authado = adoobj.getJSONObject("auth_user");
                            mpkado.add(authado.getString("pk"));
                        } catch (JSONException e) {
                            mpkado.add("null");
                        }

                        JSONObject ddaobj = c.getJSONObject("dda");
                        JSONObject authddo = ddaobj.getJSONObject("auth_user");
                        mpkdda.add(authddo.getString("pk"));


                        villagename = c.getString("village_name");
                        blockname = c.getString("block_name");
                        district = c.getString("district");
                        try {
                            JSONObject mDdaObject = c.getJSONObject("dda");
                            String ddaName = mDdaObject.getString("name");
                            mDdaName.add(ddaName);
                        } catch (JSONException e) {
                            mDdaName.add("Not Assigned");
                        }

                        try {
                            JSONObject mAdoObject = c.getJSONObject("ado");
                            String adoName = mAdoObject.getString("name");
                            mAdaName.add(adoName);
                        } catch (JSONException e) {
                            mAdaName.add("Not Assigned");
                        }
                        mAddress.add(villagename.toUpperCase() + ", " +
                                blockname.toUpperCase() + ", " + district.toUpperCase());
                    }
                    requestQueue.add(jsonObjectRequest2);
                } catch (JSONException e) {
                    Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                    e.printStackTrace();
                    requestQueue.add(jsonObjectRequest2);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError)
                    Toast.makeText(getActivity(), "Check Your Internt Connection Please!", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onErrorResponse: " + error);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + token);
                return map;
            }
        };


        requestQueue.add(jsonObjectRequest1);
        jsonObjectRequest1.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        jsonObjectRequest2.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int totalCount, pastItemCount, visibleItemCount;

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    totalCount = layoutManager.getItemCount();
                    pastItemCount = layoutManager.findFirstVisibleItemPosition();
                    visibleItemCount = layoutManager.getChildCount();
                    if ((pastItemCount + visibleItemCount) >= totalCount) {
                        if (!isNextBusy)
                            loadNextLocations();
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSendingNotifications)
                    sendNotifications();
                else
                    Toast.makeText(getActivity(), "Please wait, Notifications request in progress",
                            Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void loadNextLocations() {
        /*switch (NEXT_LOCATION_COUNT) {
            case 1:
                if (!next_unassigned_url.equals("null"))
                    get_Unassigned();
                NEXT_LOCATION_COUNT = 2;
                break;
            case 2:
                if (!next_assigned_url.equals("null"))
                    get_Assigned();
                NEXT_LOCATION_COUNT = 1;
                break;
        }*/
        if (!next_unassigned_url.equals("null")) {
            get_Unassigned();
        } else if (!next_assigned_url.equals("null")) {
            get_Assigned();
        }

    }

    private void get_Unassigned() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        progressBar.setVisibility(View.VISIBLE);
        isNextBusy = true;
            final JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, next_unassigned_url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(response));
                        JSONArray jsonArray = jsonObject.getJSONArray("results");
                        next_unassigned_url = jsonObject.getString("next");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);

                            try {
                                JSONObject adoobj = c.getJSONObject("ado");
                                JSONObject authado = adoobj.getJSONObject("auth_user");
                                mpkado.add(authado.getString("pk"));
                            } catch (JSONException e) {
                                mpkado.add("null");
                            }

                            JSONObject ddaobj = c.getJSONObject("dda");
                            JSONObject authddo = ddaobj.getJSONObject("auth_user");
                            mpkdda.add(authddo.getString("pk"));

                            villagename = c.getString("village_name");
                            blockname = c.getString("block_name");
                            district = c.getString("district");
                            JSONObject mDdaObject = c.getJSONObject("dda");
                            String ddaName = mDdaObject.getString("name");
                            mDdaName.add(ddaName);
                            try {
                                JSONObject mAdoObject = c.getJSONObject("ado");
                                String adoName = mAdoObject.getString("name");
                                mAdaName.add(adoName);
                            } catch (JSONException e) {
                                mAdaName.add("Not Assigned");
                            }
                            mAddress.add(villagename.toUpperCase() + ", "
                                    + blockname.toUpperCase() + ", " + district.toUpperCase());
                        }
                        recyclerViewAdater.notifyDataSetChanged();
                        isNextBusy = false;
                    } catch (JSONException e) {
                        Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof NoConnectionError)
                        Toast.makeText(getActivity(), "Check Your Internt Connection Please!", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onErrorResponse: " + error);
                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("Authorization", "Token " + token);
                    return map;
                }
            };
        jsonObjectRequest1.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
            requestQueue.add(jsonObjectRequest1);
        requestFinished(requestQueue);

    }

    private void get_Assigned() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        isNextBusy = true;
        progressBar.setVisibility(View.VISIBLE);
            final JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, next_assigned_url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(response));
                        JSONArray jsonArray = jsonObject.getJSONArray("results");
                        next_assigned_url = jsonObject.getString("next");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            JSONObject adoobj = c.getJSONObject("ado");
                            JSONObject authado = adoobj.getJSONObject("auth_user");
                            mpkado.add(authado.getString("pk"));

                            JSONObject ddaobj = c.getJSONObject("dda");
                            JSONObject authddo = ddaobj.getJSONObject("auth_user");
                            mpkdda.add(authddo.getString("pk"));
                            villagename = c.getString("village_name");
                            blockname = c.getString("block_name");
                            district = c.getString("district");
                            JSONObject mDdaObject = c.getJSONObject("dda");
                            String ddaName = mDdaObject.getString("name");
                            mDdaName.add(ddaName);
                            try {
                                JSONObject mAdoObject = c.getJSONObject("ado");
                                String adoName = mAdoObject.getString("name");
                                mAdaName.add(adoName);
                            } catch (JSONException e) {
                                mAdaName.add("Not Assigned");
                            }
                            mAddress.add(villagename.toUpperCase() + ", " +
                                    blockname.toUpperCase() + ", " + district.toUpperCase());
                        }
                        recyclerViewAdater.notifyDataSetChanged();
                        isNextBusy = false;
                    } catch (JSONException e) {
                        Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof NoConnectionError)
                        Toast.makeText(getActivity(), "Check Your Internt Connection Please!",
                                Toast.LENGTH_SHORT).show();
                    isNextBusy = false;
                    Log.e(TAG, "onErrorResponse: " + error);
                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("Authorization", "Token " + token);
                    return map;
                }
            };
        jsonObjectRequest2.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
            requestQueue.add(jsonObjectRequest2);
        requestFinished(requestQueue);
    }

    private void requestFinished(RequestQueue queue) {

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void sendNotifications() {
        isSendingNotifications = true;
        String url = "http://18.224.202.135/api/trigger/sms/pending";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: sendNotifications " + response);
                        Toast.makeText(getActivity(), "Notifications Successfully Sent!",
                                Toast.LENGTH_SHORT).show();
                        isSendingNotifications = false;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError)
                            Toast.makeText(getActivity(), "Check Your Internt Connection Please!",
                                    Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getActivity(), "Something went wrong, please try again!",
                                    Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onErrorResponse: sendNotifications " + error);
                        isSendingNotifications = false;
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + token);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}

