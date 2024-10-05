package com.srbenicio.slist.activitys;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.srbenicio.slist.ItemAdapter;
import com.srbenicio.slist.ItemList;
import com.srbenicio.slist.R;
import com.srbenicio.slist.controllers.DatabaseItemController;
import com.srbenicio.slist.creators.ItemTable;

import java.util.ArrayList;
import java.util.List;

public class GroupList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private int groupId;
    private String itemTitle;
    private List<ItemList> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        groupId = getIntent().getIntExtra("ITEM_ID", -1);
        itemTitle = getIntent().getStringExtra("ITEM_TITLE");

        if (groupId==-1){finish();}
        if (itemTitle.isEmpty()){itemTitle="SList";}


        Toolbar my_toolbar = findViewById(R.id.toolbar);
        my_toolbar.setTitle(itemTitle);

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(view -> createNewItem());

        loadItemsAndShow(groupId);
    }

    private void createNewItem(){
        DatabaseItemController crud = new DatabaseItemController(getBaseContext());
        boolean result = crud.insert("Meu", "Minha desc", 0, groupId);

        if (result) Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        else Toast.makeText(this, "Failed to save", Toast.LENGTH_SHORT).show();

        loadItemsAndShow(groupId);
    }

    private void loadItemsAndShow(int groupId) {
        DatabaseItemController crud = new DatabaseItemController(getBaseContext());
        Cursor cursor = crud.loadData(groupId);
        itemList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                try {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(ItemTable.COLUMN_ID));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(ItemTable.COLUMN_NAME));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow(ItemTable.COLUMN_DESC));
                    int record = cursor.getInt(cursor.getColumnIndexOrThrow(ItemTable.COLUMN_RECORD));
                    String createdIn = cursor.getString(cursor.getColumnIndexOrThrow(ItemTable.COLUMN_CREATED_IN));
                    String lastUpdate = cursor.getString(cursor.getColumnIndexOrThrow(ItemTable.COLUMN_LAST_UPDATE));

                    itemList.add(new ItemList(id, name, description, record, createdIn, lastUpdate));
                } catch (Exception e) {
                    System.out.println("Error loading item: " + e.getMessage());
                }
            } while (cursor.moveToNext());

            cursor.close(); // Always close the cursor when done
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ItemAdapter(this, itemList);
        recyclerView.setAdapter(adapter);
    }
}
