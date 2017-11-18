package com.example.alaze.fileexplorer;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alaze.fileexplorer.browse.DBHelper;

public class Tabs extends AppCompatActivity {

    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        dbHelper = new DBHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        GridLayout gridLayout = (GridLayout) findViewById(R.id.LTabs);

        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int tabIndex = cursor.getColumnIndex(DBHelper.KEY_TAB);
            do {
                TextView textView = new TextView(this);
                textView.setText(cursor.getString(nameIndex));
                gridLayout.addView(textView);
                TextView textView1 = new TextView(this);
                textView1.setText(cursor.getString(tabIndex));
                gridLayout.addView(textView1);

                Log.d("mLog", "ID = " + cursor.getInt(idIndex) +
                        ", name = " + cursor.getString(nameIndex) +
                        ", tab = " + cursor.getString(tabIndex));
            } while (cursor.moveToNext());
        } else
            Log.d("mLog","0 rows");
        cursor.close();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        dbHelper.close();
    }
}
