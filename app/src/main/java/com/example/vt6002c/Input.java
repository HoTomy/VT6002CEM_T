package com.example.vt6002c;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class Input extends AppCompatActivity {

    SQLiteDatabase db;
    String dbName = "customerDB";
    private EditText et_up, et_lp, et_hr, et_da, et_ti;

    private Button btnRs, btnCr, btnMain;
    TextView tvRecords;
    Date date;
    ListView listView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        et_up = findViewById(R.id.hp);
        et_lp = findViewById(R.id.lp);
        et_hr = findViewById(R.id.pu);
        btnMain=findViewById(R.id.m_b);
        btnRs = findViewById(R.id.btn_confirm);
        btnRs.setOnClickListener(clickListener);

        tvRecords = findViewById(R.id.tvRecords);


        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Input.this, MainActivity.class);
                startActivity(intent);
            }
        });

        db = openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
        String sql = "CREATE TABLE if not exists customerTable ( id INTEGER PRIMARY KEY AUTOINCREMENT ," +
                "up VARCHAR(32), lp VARCHAR(32), hr VARCHAR(32), date STRING(2048))";
        db.execSQL(sql);

        Cursor mCursor = db.rawQuery("SELECT up ,lp, hr, date FROM customerTable", null);
        if (mCursor != null && mCursor.getCount() > 0) {
            String result = "";
            while (mCursor.moveToNext())
                result += mCursor.getString(3)+"  |  " + mCursor.getInt(0) + "    |   " + mCursor.getInt(1) +
                        "   |    " + mCursor.getInt(2)
                        +"\n";
            showAllRecords(result);
        } else {
            Toast.makeText(this, R.string.no_records, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Cursor mCursor = db.rawQuery("SELECT up ,lp, hr, date FROM customerTable", null);
        if (mCursor != null && mCursor.getCount() > 0) {
            String result = "";
            while (mCursor.moveToNext())
                result += mCursor.getString(3)+"  |  " + mCursor.getInt(0) + "    |   " + mCursor.getInt(1) +
                        "   |    " + mCursor.getInt(2)
                        +"\n";
            showAllRecords(result);
        } else {
            Toast.makeText(this, R.string.no_records, Toast.LENGTH_LONG).show();
        }
    }




    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View V) {
            SimpleDateFormat formatter   =   new   SimpleDateFormat   ("dd/MM/yyyy  HH:mm:ss");
            Date curDate =  new Date(System.currentTimeMillis());
            String strUp = et_up.getText().toString();
            String strLp = et_lp.getText().toString();
            String strHr = et_hr.getText().toString();
            String strDate = formatter.format(curDate);

            if (TextUtils.isEmpty(strUp) || TextUtils.isEmpty(strLp) || TextUtils.isEmpty(strHr)) {
                Toast.makeText(Input.this, getString(R.string.no_input_msg), Toast.LENGTH_SHORT).show();
            } else {
                double up = Double.parseDouble(strUp);
                double lp = Double.parseDouble(strLp);
                double hr = Double.parseDouble(strHr);

                if (lp > up) {
                    Toast.makeText(Input.this, "Wrong Input!!!", Toast.LENGTH_SHORT).show();
                } else {


                    ContentValues cv = new ContentValues(1024);
                    cv.put("up", up);
                    cv.put("lp", lp);
                    cv.put("hr", hr);
                    cv.put("date", strDate);
                    db.insert("customerTable", null, cv);
                    Toast.makeText(Input.this, "The data is recorded.", Toast.LENGTH_SHORT).show();


                    //create an intent object to start new activity
                    Intent intent = new Intent(Input.this, Result.class);

                    //put all the data into bundle
                    Bundle b = new Bundle();
                    b.putDouble("UP", up);
                    b.putDouble("LP", lp);
                    b.putDouble("HR", hr);


                    //add bundle to intent
                    intent.putExtras(b);

                    startActivity(intent);
                    et_up.setText("");
                    et_lp.setText("");
                    et_hr.setText("");
                }
                ;

            }
        }

    };


    public void queryRecords(View V) {

        Cursor mCursor = db.rawQuery("SELECT up ,lp, hr, date FROM customerTable", null);
        if (mCursor != null && mCursor.getCount() > 0) {
            String result = "";
            while (mCursor.moveToNext())
                result += mCursor.getString(3)+"  |  " + mCursor.getInt(0) + "    |   " + mCursor.getInt(1) +
                        "   |    " + mCursor.getInt(2)
                        +"\n";
            showAllRecords(result);
        } else {
            Toast.makeText(this, R.string.no_records, Toast.LENGTH_LONG).show();
        }
        ;
    }

    public void delAllRecords(View V) {
        db.execSQL("delete from customerTable");
        tvRecords.setText("");
    }

    public void showAllRecords(String records) {

        tvRecords.setText(records);
        tvRecords.setMovementMethod(new ScrollingMovementMethod());
        tvRecords.setVerticalScrollBarEnabled(true);
    }

}

