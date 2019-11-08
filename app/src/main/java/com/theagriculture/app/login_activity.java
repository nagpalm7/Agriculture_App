package com.theagriculture.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.theagriculture.app.Admin.AdminActivity;
import com.theagriculture.app.Ado.AdoActivity;
import com.theagriculture.app.Dda.DdaActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;


public class login_activity extends AppCompatActivity {

    private String token;
    private String typeOfUser;
    private String Name;
    private static final String TAG = "login_activity";
    private EditText editEmail, editPassword;
    private TextView signUpAdo, signUpDda;

    private String urlget = "http://18.224.202.135/api/get-user/";
    //    private String urlpost = getString(R.string.rooturl)+ "api-token-auth/";
//    private String urlpost = getString(R.string.rooturl)
    private String urlpost = "http://18.224.202.135/api-token-auth/";

    private AlertDialog dialog;
    private CheckBox checkBox;
    private Button btnLogin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        final SharedPreferences sp = getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        String Usertype = sp.getString("typeOfUser", "");
        if (sp.contains("token")) {
            Intent intent = null;
            if (Usertype.equals("dda"))
                intent = new Intent(this, DdaActivity.class);
            if (Usertype.equals("ado"))
                intent = new Intent(this, AdoActivity.class);
            if (Usertype.equals("admin"))
                intent = new Intent(this, AdminActivity.class);
            if (intent != null) {
                startActivity(intent);
                finish();
            }

        }

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.login_button);
        checkBox = findViewById(R.id.eyeIcon);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checkBox.setAlpha((float) 0.2);
                if (b) {
                    editPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    checkBox.setAlpha((float) 1.0);
                } else {
                    editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        editPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    String mEmail = editEmail.getText().toString().trim();
                    String mPass = editPassword.getText().toString().trim();

                    if (!mEmail.isEmpty() && !mPass.isEmpty()) {
                        Log.d(TAG, "onEditorAction: Done button pressed");
                        Login(mEmail, mPass);
                    } else if (mEmail.isEmpty() && mPass.isEmpty()) {
                        editEmail.setError("Please insert email");
                        editPassword.setError("Please insert password");
                    } else if (mEmail.isEmpty())
                        editEmail.setError("Please insert email");
                    else if (mPass.isEmpty())
                        editPassword.setError("Please insert password");

                    handled = true;
                }
                return handled;
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = editEmail.getText().toString().trim();
                String mPass = editPassword.getText().toString().trim();

                if (!mEmail.isEmpty() && !mPass.isEmpty()) {
                    Login(mEmail, mPass);
                } else if (!mEmail.isEmpty() && mPass.isEmpty()) {
                    editPassword.setError("Please insert password");
                    Toast.makeText(getApplicationContext(), "Please insert password", Toast.LENGTH_SHORT);
                } else if (mEmail.isEmpty() && !mPass.isEmpty()) {
                    editEmail.setError("Please insert email.");
                    Toast.makeText(getApplicationContext(), "Please insert email", Toast.LENGTH_SHORT);
                } else {
                    editEmail.setError("Please insert email.");
                    editPassword.setError("Please insert password");
                    Toast.makeText(getApplicationContext(), "Please insert email and password", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private void Login(final String email, final String password) {

        dialog = new SpotsDialog.Builder().setContext(login_activity.this).setMessage("Logging in").setCancelable(false)
                .setTheme(R.style.CustomDialog).build();
        dialog.show();

        Log.d(TAG, "onResponse: login clicked");
        final RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        JSONObject postparams = null;
        try {
            postparams = new JSONObject();
            postparams.put("username", email);
            postparams.put("password", password);
        } catch (JSONException e) {
            Log.d(TAG, "Login: Error:" + e);
            Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_LONG).show();
            dialog.dismiss();
            btnLogin.setEnabled(true);
        }

        final JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, urlget, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String pk;
                        try {
                            JSONObject c = new JSONObject(String.valueOf(response));
                            Log.d(TAG, "onResponse: " + c);
                            JSONObject a = c.getJSONObject("auth_user");
                            typeOfUser = a.getString("type_of_user");
                            Name = c.getString("name");
                            pk = a.getString("pk");
                            Log.d(TAG, "onResponse: valuepk"+pk);
                            SharedPreferences.Editor editor = getSharedPreferences("tokenFile", Context.MODE_PRIVATE).edit();
                            editor.putString("typeOfUser", typeOfUser);
                            editor.putString("Name", Name);
                            editor.putString("pk", pk);
                            editor.apply();

                            Log.d(TAG, "onResponse: typeOfUser:" + typeOfUser);


                            Intent intent = null;

                            if (typeOfUser.equals("admin")) {

                                intent = new Intent(login_activity.this, AdminActivity.class);
                                startActivity(intent);
                                finish();
                            } else if (typeOfUser.equals("dda")) {
                                btnLogin.setEnabled(false);
                                btnLogin.setClickable(false);
                                intent = new Intent(login_activity.this, DdaActivity.class);
                                startActivity(intent);
                                finish();
                            } else if (typeOfUser.equals("ado")) {

                                intent = new Intent(login_activity.this, AdoActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Invalid User", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            Log.d(TAG, "onResponse: error in get catch block :" + e.getMessage());
                            Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            btnLogin.setEnabled(true);
//
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError)
                            Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_LONG).show();
                        else if (error instanceof ClientError)
                            Toast.makeText(getApplicationContext(), "Invalid User!", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getApplicationContext(), "Something went wrong, please try again!", Toast.LENGTH_LONG).show();
                        btnLogin.setEnabled(true);
                        dialog.dismiss();
                        Log.d(TAG, "onErrorResponse: some error in get: " + error.getLocalizedMessage());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                Log.d(TAG, "getHeaders: ");
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
                            SharedPreferences.Editor editor = getSharedPreferences("tokenFile", Context.MODE_PRIVATE).edit();
                            editor.putString("token", token);
                            editor.apply();
                            MyRequestQueue.add(jsonObjectRequest1);
                            Log.d(TAG, "onResponse: Token:" + token);
                        } catch (JSONException e) {
                            dialog.dismiss();
                            btnLogin.setEnabled(true);
                            Log.d(TAG, "onResponse: error in post catch block: " + e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError)
                            Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_LONG).show();
                        else if (error instanceof ClientError)
                            Toast.makeText(getApplicationContext(), "Invalid User!", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getApplicationContext(), "Something went wrong, please try again!", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "onErrorResponse: invalid user : " + error);
                        dialog.dismiss();
                        btnLogin.setEnabled(true);
                    }
                });

        MyRequestQueue.add(jsonObjectRequest);

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

    }






    public void onClickregister(String url, boolean isAdo) {

        Intent intent = new Intent(login_activity.this,RegistrationActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("isAdo", isAdo);
        startActivity(intent);
    }

    public void onClickForgetPassword(View view) {
        Intent intent = new Intent(login_activity.this,ForgetPasswordActivity.class);
        startActivity(intent);
    }
}
