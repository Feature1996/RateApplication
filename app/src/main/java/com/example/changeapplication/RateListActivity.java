package com.example.changeapplication;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RateListActivity extends ListActivity implements Runnable{

    Handler handler;
    String data[]={"waiting......"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final List<String> list1=new ArrayList<String>();
        for(int i=1;i<100;i++){
            list1.add("item"+i);
        }
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_rate_list);//用于父类ListActivity中已经有了一个布局；
        ListAdapter adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list1);
        setListAdapter(adapter);//把当前界面用adapter来管理；

        Thread thread =new Thread(this);
        thread.start();

        handler =new Handler(){

            @Override
            public void handleMessage(Message msg) {
                if(msg.what==2){
                   List<String> list2= (List<String>) msg.obj;
                    ListAdapter adapter=new ArrayAdapter<String>(RateListActivity.this,android.R.layout.simple_list_item_1,list2);
                    setListAdapter(adapter);
                }

                super.handleMessage(msg);
            }
        };


    }

    @Override
    public void run() {
        //获取网络数据；放入list带回到主线程中；
        List<String>  relist = new ArrayList<String>();
        Document doc=null;
        URL url=null;
        try {
            doc= Jsoup.connect("http://www.boc.cn/sourcedb/whpj/").get();
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
                relist.add(str1+"==>"+val);
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Message message =handler.obtainMessage(2);
        message.obj=relist;
        handler.sendMessage(message);

    }
}
