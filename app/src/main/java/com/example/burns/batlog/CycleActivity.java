package com.example.burns.batlog;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class CycleActivity extends AppCompatActivity {
    private TextView mNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycle);

        mNameView = (TextView) findViewById(R.id.id_cycle_name);

        String id = getIntent().getStringExtra("KEY-ID");
        Log.d("KEY-ID", id);

        mNameView.setText(id);

        Button okButton = (Button) findViewById(R.id.id_cycle_okButton);
        okButton.setTag(id);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = (String)view.getTag();

                batteryAddCycle(id);
                finish();
            }
        });
    }

    public void batteryAddCycle(String inId) {
        // Create a path where we will place our private file on external
        // storage.

        File root = new File(Environment.getExternalStorageDirectory(), "BatLOG");
        File batteries = new File(root, "Batteries");
        File idDir = new File(batteries, inId);
        File file = new File(idDir, "cycles.csv");


        if (!root.mkdirs()) {
            Log.e("mkdir", "Directory not created");
        }
        if (!batteries.mkdirs()) {
            Log.e("mkdir batteries", "Directory not created");
        }


        Log.i("File info:", file.getAbsolutePath());
        try {

            FileOutputStream fOut = new FileOutputStream(file, true);

            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

            myOutWriter.append("cycle;100;200;300;");
            myOutWriter.append("\n");

            myOutWriter.close();

            fOut.flush();
            fOut.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed in createExternalStoragePrivateFile: " + e.toString());
        }
    }
}
