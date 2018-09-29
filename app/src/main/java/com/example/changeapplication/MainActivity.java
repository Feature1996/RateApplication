package com.example.changeapplication;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {



    private EditText rmb;
    private TextView show;
    private float dollorRate=0.1f;
    private float euroRate=0.2f;
    private float wonRate=0.3f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rmb=(EditText) findViewById(R.id.rmb);
        show=(TextView) findViewById(R.id.show);
    }

    public void onclick(View btn){

        String str=rmb.getText().toString();
        float r=0;
        if(str.length()>0){
            r=Float.parseFloat(str);
        }else{
            Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
        }
        if(btn.getId()==R.id.dollor){
            float val=r*dollorRate;

            show.setText(""+String.format("%.2f", val));
        }else if(btn.getId()==R.id.euro){
            float val=r*euroRate;
            show.setText(""+String.format("%.2f", val));
        }else {
            float val=r*wonRate;
            show.setText(""+String.format("%.2f", val));
        }

    }
    public  void openone(View btn){
        Intent config = new Intent(this,ShowActivity.class);
        config.putExtra("dollorRate",dollorRate);
        config.putExtra("euroRate",euroRate);
        config.putExtra("wonRate",wonRate);
        startActivityForResult(config,1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_set){
            Intent config = new Intent(this,ShowActivity.class);
            config.putExtra("dollorRate",dollorRate);
            config.putExtra("euroRate",euroRate);
            config.putExtra("wonRate",wonRate);
            startActivityForResult(config,1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1&&resultCode==2){
            /*
        bundle.putFloat("newdollor",newDollar);
        bundle.putFloat("newEuro",newEuro);
        bundle.putFloat("newWon",newWon);
             */
            Bundle bundle =data.getExtras();
            dollorRate=bundle.getFloat("newdollor",0.5f);
            euroRate=bundle.getFloat("newEuro",0.5f);
            wonRate=bundle.getFloat("newWon",0.5f);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
