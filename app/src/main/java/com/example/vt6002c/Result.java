package com.example.vt6002c;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Result extends AppCompatActivity {
    TextView tvResult;
    Button btnBack;

    private View.OnClickListener clickListener= new View.OnClickListener() {

        public void onClick(View v) {

            Result.this.finish();};};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvResult=findViewById(R.id.tv_result);
        btnBack=findViewById(R.id.btn_back);
        btnBack.setOnClickListener(clickListener);

        Bundle bundle=getIntent().getExtras();
        double up=bundle.getDouble("UP");
        double lp=bundle.getDouble("LP");
        double hr=bundle.getDouble("HR");


        String resultMsg = getString(R.string.up_msg) + (int)up + " mmHg，\n";
        if(up<90)
        {resultMsg += getString(R.string.up_l_msg)+"\n";}
        else if (up>139)
        {resultMsg += getString(R.string.up_u_msg)+"\n";}
        else
        {resultMsg += getString(R.string.up_n_msg)+"\n";}
        resultMsg += "\n";
        resultMsg += "\n";

        resultMsg += getString(R.string.lp_msg) + (int)lp + " mmHg，\n";
        if(lp<60)
        {resultMsg += getString(R.string.lp_l_msg)+"\n";}
        else if (lp>89)
        {resultMsg += getString(R.string.lp_u_msg)+"\n";}
        else
        {resultMsg += getString(R.string.lp_n_msg)+"\n";}
        resultMsg += "\n";

        resultMsg += "\n";
        resultMsg += getString(R.string.hr_msg) +(int)hr + " BPN，\n";
        if(hr<60)
        {resultMsg += getString(R.string.hr_l_msg)+"\n";}
        else if (hr>100)
        {resultMsg += getString(R.string.hr_u_msg)+"\n";}
        else
        {resultMsg += getString(R.string.hr_n_msg)+"\n";}

        tvResult.setText(resultMsg);


    }
}