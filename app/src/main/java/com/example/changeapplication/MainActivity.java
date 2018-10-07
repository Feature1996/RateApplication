package com.example.changeapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



public class MainActivity extends AppCompatActivity{



    Handler handler;
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

        SharedPreferences sharedPreferences =getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        dollorRate=sharedPreferences.getFloat("dollor_rate",0.0f);
        euroRate=sharedPreferences.getFloat("euro_rate",0.0f);
        wonRate=sharedPreferences.getFloat("won_rate",0.0f);

        handler =new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==11){
                    String str=(String) msg.obj;
                    Log.i("Tag","handlerMessage::"+str);
                    show.setText(str);
                }
                super.handleMessage(msg);
            }
        };

        new Thread(){
            @Override
            public void run() {
                Log.i("TAG","111111111");
                for(int i=0;i<4;i++){
                    Log.i("Tag","run: i="+i);
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //从handler中获取meg对象；
                Message message=handler.obtainMessage(11);
                message.obj="hello from run ()";
                handler.sendMessage(message);


                //获取网络数据；
                URL url=null;
                try {

                    url =new URL("https://www.baidu.com");
                   HttpURLConnection http=(HttpURLConnection) url.openConnection();
                  InputStream inputStream =http.getInputStream();
                  String html=inputstream2string(inputStream);
                  Log.i("Tag","run :html="+html);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }.start();



    }

    //输入流转字符串；
    public String inputstream2string (InputStream stream){
        BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(stream));
        StringBuilder stringBuilder =new StringBuilder();
        String line=null;
        try{
            while ((line=bufferedReader.readLine())!=null){
                stringBuilder.append(line+"/n");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  stringBuilder.toString();
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

            //将新的汇率写到sp里
            SharedPreferences sharedPreferences =getSharedPreferences("myrate", Activity.MODE_PRIVATE);
            SharedPreferences.Editor edit=sharedPreferences.edit();
            edit.putFloat("dollor_rate",dollorRate);
            edit.putFloat("euro_rate",euroRate);
            edit.putFloat("won_rate",wonRate);
            edit.commit();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
