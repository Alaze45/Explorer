package com.example.alaze.fileexplorer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.text.DecimalFormat;

public class propertiesFile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_properties_file);
        File propertiesFile = new File(getIntent().getStringExtra("fileName"));

        Log.i("propertiesFile",propertiesFile.getName());
        ((TextView)findViewById(R.id.Name)).setText(propertiesFile.getName());
        ((TextView)findViewById(R.id.AbsolutePath)).setText(propertiesFile.getAbsolutePath());
        ((TextView)findViewById(R.id.TotalSpace)).setText(new DecimalFormat("#0.00").format((double) propertiesFile.getTotalSpace()/1048576) + "мб (" + propertiesFile.getTotalSpace() + ")");
        ((TextView)findViewById(R.id.UsableSpace)).setText(new DecimalFormat("#0.00").format((double) propertiesFile.getUsableSpace()/1048576) + "мб (" + propertiesFile.getUsableSpace() + ")");
        ((TextView)findViewById(R.id.lastModified)).setText(Long.toString(propertiesFile.lastModified()));
    }
}
