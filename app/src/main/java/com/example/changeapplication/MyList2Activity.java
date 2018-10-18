package com.example.changeapplication;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class MyList2Activity extends ListActivity {

    Handler handler;
    private ArrayList<HashMap<String,String>> item;
    private SimpleAdapter listItemAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_my_list2);
    }
}
