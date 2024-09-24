package com.srbenicio.slist;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.graphics.drawable.ColorDrawable;

import android.view.Gravity;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import android.widget.Toast;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.srbenicio.slist.controllers.DatabaseGroupController;
import com.srbenicio.slist.creator.DatabaseCreator;
import com.srbenicio.slist.creator.GroupTable;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private List<Item> itemList;
    private Uri imageUri;
    private ActivityResultLauncher<Intent> pickImageLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configurar a Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DatabaseGroupController crud = new DatabaseGroupController(getBaseContext());
        Cursor cursor = crud.loadData();
        System.out.println(cursor);
        // Inicializar dados
        itemList = new ArrayList<>();
            do {
                if (cursor == null)  {break;}
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(GroupTable.COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(GroupTable.COLUMN_NAME));
                itemList.add(new Item(id ,title, R.drawable.placeholder_image));
            } while (cursor.moveToNext()); // Move to the next row

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Handle gear icon click here
        adapter = new ItemAdapter(this, itemList, this::showConfigGroupModal);

        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(view -> showModalDialog());

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();
                        // Handle the selected image URI as needed
                        Toast.makeText(this, "Image Selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private Dialog getDialogBox(int layout){
        // Create the dialog
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout);
        dialog.setCancelable(false); // Prevent closing the dialog by tapping outside
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

    private void showConfigGroupModal(int position) {
        Item item = itemList.get(position);
        final Dialog dialog = getDialogBox(R.layout.config_group_modal_layout);

        // Find views in the dialog and set up any interactions
        // For example:
        // TextView titleTextView = dialog.findViewById(R.id.dialog_title);
        // if (titleTextView != null) {
        //     titleTextView.setText(item.getTitle());
        // }

        // Close button inside the dialog
        ImageButton closeButton = dialog.findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> dialog.dismiss());

        // Show the dialog
        dialog.show();
    }

    private void showModalDialog() {
        // Create the dialog
        final Dialog dialog = getDialogBox(R.layout.new_group_modal_layout);

        // Find views in the dialog
        ImageButton closeButton = dialog.findViewById(R.id.close_button);
        EditText textInput = dialog.findViewById(R.id.text_input);
        Button chooseImageButton = dialog.findViewById(R.id.choose_image_button);
        Button saveButton = dialog.findViewById(R.id.save_button);

        // Set click listeners
        closeButton.setOnClickListener(v -> dialog.dismiss());

        chooseImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            pickImageLauncher.launch(Intent.createChooser(intent, "Select Picture"));
        });

        saveButton.setOnClickListener(v -> {
            String inputText = textInput.getText().toString();
            // TODO : Add the logic to save data
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        // Show the dialog
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
}