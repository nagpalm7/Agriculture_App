package com.example.myapplication.Ado;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.myapplication.R;

public class AdoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ado);
        Toast.makeText(this,"Ado successfully logged in..",Toast.LENGTH_LONG).show();
    }
}
