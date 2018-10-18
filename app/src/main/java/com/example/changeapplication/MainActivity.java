package com.example.changeapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
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
        dollorRate=sharedPreferences.getFloat("dollar_rate",0.0f);
        euroRate=sharedPreferences.getFloat("euro_rate",0.0f);
        wonRate=sharedPreferences.getFloat("won_rate",0.0f);

        handler =new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==5){
                    Bundle bdl=(Bundle) msg.obj;
                    dollorRate=bdl.getFloat("dollar-rate");
                    euroRate=bdl.getFloat("euro-rate");
                    wonRate=bdl.getFloat("won-rate");
                    Log.i("TAG","dollarkkkkkk"+dollorRate);
                    Log.i("TAG","wonkkkkkk"+wonRate);
                    Log.i("TAG","eurokkkkkk"+euroRate);




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


                //获取网络数据；

                Bundle bundle=new Bundle();//用于保存汇率
                Document doc=null;
                URL url=null;
                try {
                    doc=Jsoup.connect("http://www.boc.cn/sourcedb/whpj/").get();
                    Log.i("run:::::",doc.title());
                    Elements tables =doc.getElementsByTag("table");
                    Element td=tables.get(1);
                    Elements tds=td.getElementsByTag("td");
                    for(int i=0;i<tds.size();i+=8){
                        Element td1=tds.get(i);
                        Element td2=tds.get(i+5);
                        String str1=td1.text();
                        String val=td2.text();
                        Log.i("Tag",str1+"==>"+val);
                        if("美元".equals(str1)){
                            bundle.putFloat("dollar-rate",100f/Float.parseFloat(val));
                        }else if("欧元".equals(str1)){
                            bundle.putFloat("euro-rate",100f/Float.parseFloat(val));
                        }else if("韩国元".equals(str1)){
                            bundle.putFloat("won-rate",100f/Float.parseFloat(val));
                        }
                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //从handler中获取meg对象；
                Message message=handler.obtainMessage();
                message.what = 5;
                message.obj=bundle;
                //message.obj="hello from run ()";
                handler.sendMessage(message);




            }
        }.start();
    }

    //输入流转字符串；
    public String inputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "gb2312");
        for(; ;){
            int rsz = in.read(buffer,0,buffer.length);
            if(rsz < 0)
                break;
            out.append(buffer,0,rsz);
        }
        return out.toString();
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
        }else if(item.getItemId()==R.id.open_list){
            //打开列表
            Intent list = new Intent(this,RateListActivity.class);
            startActivity(list);
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
