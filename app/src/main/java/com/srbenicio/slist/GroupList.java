package com.srbenicio.slist;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;



public class GroupList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        int itemId = getIntent().getIntExtra("ITEM_ID", -1);
        if (itemId==-1){finish();}

        TextView det = findViewById(R.id.detail_text);
        det.setText("Detail Activity :"+ itemId);
    }
}
