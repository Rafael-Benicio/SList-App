package com.srbenicio.slist.activitys;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.srbenicio.slist.R;
import com.srbenicio.slist.controllers.DatabaseItemController;


public class GroupList extends AppCompatActivity {
    int groupId;
    String itemTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        groupId = getIntent().getIntExtra("ITEM_ID", -1);
        itemTitle = getIntent().getStringExtra("ITEM_TITLE");

        if (groupId==-1){finish();}
        if (itemTitle.isEmpty()){itemTitle="SList";}

        DatabaseItemController crud = new DatabaseItemController(getBaseContext());
        Cursor cursor = crud.loadData(groupId);

        Toolbar my_toolbar = findViewById(R.id.toolbar);
        my_toolbar.setTitle(itemTitle);

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(view -> createNewItem());
    }

    private void createNewItem(){
        DatabaseItemController crud = new DatabaseItemController(getBaseContext());
        boolean result = crud.insert("Meu", "Minha desc", 0, groupId);

        if (result) Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        else Toast.makeText(this, "Failed to save", Toast.LENGTH_SHORT).show();
    }
}
