package com.example.alaze.fileexplorer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        LinearLayout l = (LinearLayout) findViewById(R.id.LineLayMain);
        registerForContextMenu(l);
        this.browseTo(Environment.getExternalStorageDirectory());
        Button BackBut = (Button) findViewById(R.id.BackBut);
        BackBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              upOneLevel();
            }});
        Button GoBut = (Button) findViewById(R.id.GoBut);
        GoBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browseTo(Environment.getRootDirectory());
            }});
    }

// context menu
    public static final int IDM_COPY = 101;
    public static final int IDM_PASTE = 102;
    public static final int IDM_DALETE = 103;
    public static final int IDM_PROPERTIES = 104;
    public  static File Temp_File;
    private static String current_file = "/";
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, IDM_COPY, Menu.NONE, "Копировать");
        menu.add(Menu.NONE, IDM_PASTE, Menu.NONE, "Вставить");
        menu.add(Menu.NONE, IDM_DALETE, Menu.NONE, "Удалить");
        menu.add(Menu.NONE, IDM_PROPERTIES, Menu.NONE, "Свойства");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        CharSequence message = "none";
        switch (item.getItemId())
        {
            case IDM_COPY:
            {
                MainActivity.Temp_File = new File(current_file);
                message = MainActivity.Temp_File.getName();
            }
                break;
            case IDM_PASTE: {
                File f = new File(currentDirectory.getAbsolutePath(), Temp_File.getName());
                String TF_name = Temp_File.getName();
                while(f.exists())
                    for(final File file: currentDirectory.listFiles())
                    {
                        if(TF_name.equals(file.getName())){
                            String [] splStr = TF_name.split("\\.");
                            TF_name = "";
                            for(int i = 0; i < splStr.length - 1; i++){
                                TF_name += splStr[i] + ".";
                            }
                            TF_name += "(copy)." + splStr[splStr.length - 1];
                            f = new File(currentDirectory.getAbsolutePath(), TF_name);
                            break;
                        }
                    }
                try {
                    copyFiles(Temp_File, f);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("file", "not create file");
                }
            }
                break;
            case IDM_DALETE:
            {
                if (new File(current_file).delete())
                message = "удален";
            }
            break;
            case IDM_PROPERTIES:
            {
                Intent i = new Intent(MainActivity.this, propertiesFile.class);
                i.putExtra("fileName",current_file);
                startActivity(i);
            }
            break;
            default:
                return super.onContextItemSelected(item);
        }
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        browseTo(currentDirectory);
        return true;
    }
// context menu end



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_manage) {
            Intent i = new Intent(MainActivity.this, Setting.class);
            startActivity(i);

        } else if(id == R.id.nav_share){
        }else if(id == R.id.nav_send){}

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private File currentDirectory = new File("/");
    private void browseTo(final File aDirectory){
        if (aDirectory.isDirectory()){
            this.currentDirectory = aDirectory;

            this.fill(aDirectory.listFiles());

            Log.i("browseTo", aDirectory.getAbsoluteFile().toString());

        } else {
            //if we want to open file, show this dialog:
            //listener when YES button clicked
            DialogInterface.OnClickListener okButtonListener = new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface arg0, int arg1) {
                    //intent to navigate file
                    Intent i = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("file://" + aDirectory.getAbsolutePath()));
                    //start this activity
                    startActivity(i);
                }
            };
            //listener when NO button clicked
            DialogInterface.OnClickListener cancelButtonListener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    //do nothing
                    //or add something you want
                }
            };
            //create dialog
            new AlertDialog.Builder(this)
                    .setTitle("Подтверждение") //title
                    .setMessage("Хотите открыть файл "+ aDirectory.getName() + "?")
                    .setPositiveButton("Да", okButtonListener)
                    .setNegativeButton("Нет", cancelButtonListener)
                    .show();
        }
    }
    private void fill(File[] files) {

        LinearLayout l = (LinearLayout) findViewById(R.id.LineLayMain);
        //Temp_file_name_list.clear();

        if (null != l && l.getChildCount() > 0) {
            try {
                l.removeViews(0, l.getChildCount());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (final File file : files) {
            TextView t = new TextView(this);
            t.setTextSize(24);
            t.setText(file.getName());
            t.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    browseTo(file);
                }});
            t.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v){
                    MainActivity.current_file = file.getAbsolutePath();
                    return false;
                }});
            l.addView(t);

        }
    }
    private void upOneLevel(){
        if(this.currentDirectory.getParent() != null) {
            this.browseTo(this.currentDirectory.getParentFile());
        }
    }


    public void copyFiles(File source, File target) throws IOException {
        if(source.isDirectory())
        {
            File[] files = source.listFiles();
            if(!target.mkdirs())
                Log.i("filecopy", "Ошибка создания папки");
            for(File myfile:files)
            {
                File f = new File(target.getAbsolutePath(), myfile.getName());
                copyFiles(myfile, f);
            }
        }
        else {
            if(!target.createNewFile())
                Log.i("file","not create file" + source.getName());
            copy c = new copy(source, target);
            Button button = (Button) findViewById(R.id.PBb);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            c.execute();
        }
    }
    class copy extends AsyncTask<Void, Integer, Void> {
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
}
