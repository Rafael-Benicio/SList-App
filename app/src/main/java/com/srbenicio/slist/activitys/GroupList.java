package com.srbenicio.slist.activitys;

import android.app.Dialog;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.srbenicio.slist.adapters.ItemAdapter;
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

        TextView text_toolbar = my_toolbar.findViewById(R.id.toolbar_title);
        text_toolbar.setText(itemTitle);

        ImageButton btn_toolbar = my_toolbar.findViewById(R.id.toolbar_menu_button);
        btn_toolbar.setOnClickListener(view -> showModalConfig());

        FloatingActionButton addItemButton = findViewById(R.id.fab_add);
        addItemButton.setOnClickListener(view -> showModalToCreateItem());

        loadItemsFromDatabaseAndShow(groupId);
    }

    private void closeDialogActive(Dialog dialog){
        dialog.dismiss();
    }

    private void showModalToCreateItem() {
        final Dialog dialog = getDialogBox(R.layout.modal_new_item_list);

        EditText textInput = dialog.findViewById(R.id.text_input);
        EditText textInputDesc = dialog.findViewById(R.id.desc_text_input);
        Button saveButton = dialog.findViewById(R.id.save_button);
        ImageButton closeButton = dialog.findViewById(R.id.close_button);
        textInputDesc.setMovementMethod(new ScrollingMovementMethod());

        closeButton.setOnClickListener(v -> closeDialogActive(dialog));

        saveButton.setOnClickListener(v -> {
            String inputText = textInput.getText().toString();
            String inputTextDesc = textInputDesc.getText().toString();


            if (inputText.isEmpty()) return;

            DatabaseItemController crud = new DatabaseItemController(getBaseContext());
            boolean result = crud.insert(inputText, inputTextDesc,0,groupId);

            loadItemsFromDatabaseAndShow(groupId);

            if (result) {
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to save", Toast.LENGTH_SHORT).show();
            }

            closeDialogActive(dialog);
        });

        dialog.show();
    }

    private void showModalConfig(){
        final Dialog dialog = getDialogBox(R.layout.modal_config_item_list_layout);

        ImageButton closeButton = dialog.findViewById(R.id.close_button);

        closeButton.setOnClickListener(v -> closeDialogActive(dialog));

        dialog.show();
    }

    private Dialog getDialogBox(int layout){
        // Create the dialog
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout);
        dialog.setCancelable(true); // Prevent closing the dialog by tapping outside
        //Define dialog box behavior
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }

        return dialog;
    }

    private void loadItemsFromDatabaseAndShow(int groupId) {
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
