package com.example.alaze.fileexplorer.browse;

import android.os.AsyncTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Alaze on 17.11.2017.
 */
public class copy extends AsyncTask<Void, Integer, Void> {
    private File source;
    private File target;
    private float p = 0;

    public copy(File f1, File f2) {
        this.source = f1;
        this.target = f2;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //progressBar.setVisibility(View.VISIBLE);
        //textView.setVisibility(View.VISIBLE);
        //button.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        //progressBar.setProgress(values[0]);
        //textView.setText(source.getName());
    }

    @Override
    protected Void doInBackground(Void... parameter) {
        try {
            InputStream in = new FileInputStream(source);
            OutputStream out = new FileOutputStream(target);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
                float a = len;
                float b = source.length();
                p += a / b * 1000f;
                publishProgress((int) p);
            }
            in.close();
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        //progressBar.setVisibility(View.INVISIBLE);
        //textView.setVisibility(View.INVISIBLE);
        //button.setVisibility(View.INVISIBLE);
    }
}
