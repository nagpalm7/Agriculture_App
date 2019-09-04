package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BlendMode;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class login_activity extends AppCompatActivity {

    private String token;
    private String typeOfUser;
    private static final String TAG = "login_activity";
    private EditText editEmail, editPassword;

    private String urlget = "http://13.235.100.235:8000/api/get-user/";
//    private String urlpost = getString(R.string.rooturl)+ "api-token-auth/";
//    private String urlpost = getString(R.string.rooturl);
    private String urlpost = "http://13.235.100.235:8000/api-token-auth/";

    private ProgressBar progressBar;
    private CheckBox checkBox;


    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login_activity);

            editEmail = findViewById(R.id.editEmail);
            editPassword = findViewById(R.id.editPassword);
            final Button btnLogin = findViewById(R.id.login_button);
            progressBar = findViewById(R.id.progressBar);
            checkBox = findViewById(R.id.eyeIcon);

         checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                 checkBox.setAlpha((float) 0.2);
                 if(b){
                    editPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                     checkBox.setAlpha((float) 1.0);
                 }else{
                     editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                 }
             }
         });

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mEmail = editEmail.getText().toString().trim();
                    String mPass = editPassword.getText().toString().trim();

                    if (!mEmail.isEmpty() || !mPass.isEmpty()) {
                        Login(mEmail, mPass);
                    } else {
                        editEmail.setError("Please insert email.");
                        editPassword.setError("Please insert password");
                    }
                }
            });
        }

        private void Login(final String email, final String password) {
            progressBar.setVisibility(View.VISIBLE);
            Log.d(TAG, "onResponse: login clicked");
            final RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
            try {
                final JSONObject postparams = new JSONObject();
                postparams.put("username", email);
                postparams.put("password", password);

                final JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, urlget, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    JSONObject c = new JSONObject(String.valueOf(response));
                                    typeOfUser = c.getString("typeOfUser");
                                    Log.d(TAG, "onResponse: typeOfUser:" + typeOfUser);


                                    Intent intent = null;

                                    if(typeOfUser.equals("admin")){
                                        progressBar.setVisibility(View.INVISIBLE);
                                        intent = new Intent(login_activity.this,AdminActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else if(typeOfUser.equals("dda")){
                                        progressBar.setVisibility(View.INVISIBLE);
                                        intent = new Intent(login_activity.this,DdaActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else if (typeOfUser.equals("ado")){
                                        progressBar.setVisibility(View.INVISIBLE);
                                        intent = new Intent(login_activity.this,AdoActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(login_activity.this, "Invalid User", Toast.LENGTH_SHORT).show();
                                        editEmail.setText("");
                                        editPassword.setText("");
                                    }


                                } catch (JSONException e) {
                                    Log.d(TAG, "onResponse: error in get catch block :" + e.getMessage());
                                    Toast.makeText(login_activity.this,"some error occurred",Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    editEmail.setText("");
                                    editPassword.setText("");
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(login_activity.this,"Invalid User",Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.INVISIBLE);
                                editEmail.setText("");
                                editPassword.setText("");
                                Log.d(TAG, "onErrorResponse: some error in get: " + error.getLocalizedMessage());
//                            error.printStackTrace();
                            }
                        }) {


                    //pass the token in the authorisation header
                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<>();
                        headers.put("Authorization", "Token " + token);
                        return headers;
                    }
                };

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlpost, postparams,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //retrieve the token from server
                                try {
                                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                                    token = jsonObject.getString("token");
                                    MyRequestQueue.add(jsonObjectRequest1);
                                    Log.d(TAG, "onResponse: Token:" + token);
                                } catch (JSONException e) {
                                    Toast.makeText(login_activity.this,"some error occurred",Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Log.d(TAG, "onResponse: error in post catch block: " + e);
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(login_activity.this,"Invalid User",Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.INVISIBLE);
                                Log.d(TAG, "onErrorResponse: some error in post: " + error);
//                                error.printStackTrace();
                            }
                        });



                MyRequestQueue.add(jsonObjectRequest);

            } catch (JSONException e) {
                Log.d(TAG, "Login: Error:"+e);
                Toast.makeText(login_activity.this,"some error occurred",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);

            }
        }

    }
