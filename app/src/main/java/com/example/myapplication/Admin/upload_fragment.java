package com.example.myapplication.Admin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

import java.io.File;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class upload_fragment extends Fragment {
    HashMap<String, File> fileParams;
    private Integer ACTIVITY_CHOOSE_FILE1 = 121;
    private File rootPath;
    private File csvFilePath;
    private String url = "http://13.235.100.235:8000/api/upload/locations/";
    private String token;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.upload_fragment, container, false);
        CardView uploadCard = view.findViewById(R.id.card1);
        SharedPreferences prefs = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        token = prefs.getString("token", "");
        rootPath = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS);
        Log.d("url", "onCreateView: " + url);
        uploadCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCsvPicker();

            }
        });

        return view;
    }

    private void openCsvPicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "Open CSV"), ACTIVITY_CHOOSE_FILE1);
        fileParams = new HashMap<>();
        fileParams.put("location_csv", csvFilePath);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == ACTIVITY_CHOOSE_FILE1) {
                if (data.getData() != null) {
                    Uri rootUri = Uri.fromFile(rootPath);
                    csvFilePath = new File(rootUri.getPath(), "haryana.csv");
                    Log.d("path", "uploadCsv: " + csvFilePath);
                    uploadCsv();


                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadCsv() {
    }
}
