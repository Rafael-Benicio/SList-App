package com.srbenicio.slist;

import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import com.srbenicio.slist.controllers.DatabaseItemController;


public class GroupList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        int itemId = getIntent().getIntExtra("ITEM_ID", -1);
        String itemTitle = getIntent().getStringExtra("ITEM_TITLE");

        if (itemId==-1){finish();}
        if (itemTitle.isEmpty()){itemTitle="SList";}

        DatabaseItemController crud = new DatabaseItemController(getBaseContext());
//        Cursor cursor = crud.();

        Toolbar my_toolbar = findViewById(R.id.toolbar);
        my_toolbar.setTitle(itemTitle);
    }
}
