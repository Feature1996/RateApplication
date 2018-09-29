package com.example.changeapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ShowActivity extends AppCompatActivity {

    private EditText dollorRate;
    private EditText euroRate;
    private EditText wonRate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        dollorRate=(EditText)findViewById(R.id.dollorRate);
        euroRate=(EditText)findViewById(R.id.euroRate);
        wonRate=(EditText)findViewById(R.id.wonRate);
        Intent intent =getIntent();
        Float dollorRatef= intent.getFloatExtra("dollorRate",0.0f);
        Float euroRatef=intent.getFloatExtra("euroRate",0.0f);
        Float wonRatef=intent.getFloatExtra("wonRate",0.0f);
        dollorRate.setText(""+dollorRatef);
        euroRate.setText(""+euroRatef);
        wonRate.setText(""+wonRatef);
    }
    public void save(View view){
        float newDollar = Float.parseFloat(dollorRate.getText().toString());
        float newEuro = Float.parseFloat(euroRate.getText().toString());
        float newWon = Float.parseFloat(wonRate.getText().toString());
        Intent intent =getIntent();
        Bundle bundle=new Bundle();
        bundle.putFloat("newdollor",newDollar);
        bundle.putFloat("newEuro",newEuro);
        bundle.putFloat("newWon",newWon);
        intent.putExtras(bundle);
        setResult(2,intent);
        finish();
    }

}
