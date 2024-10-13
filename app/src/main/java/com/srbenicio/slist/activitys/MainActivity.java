package com.srbenicio.slist.activitys;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.graphics.drawable.ColorDrawable;

import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import android.widget.ImageView;
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
import com.srbenicio.slist.DatabaseExporter;
import com.srbenicio.slist.GroupItem;
import com.srbenicio.slist.adapters.GroupItemAdapter;
import com.srbenicio.slist.R;
import com.srbenicio.slist.controllers.DatabaseGroupController;
import com.srbenicio.slist.creators.GroupTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private GroupItemAdapter adapter;
    private List<GroupItem> itemList;
    private Uri imageUri;
    private ActivityResultLauncher<Intent> pickImageLauncher;
    private Dialog dialogActive = null;
    private enum UPDATE_TYPE {DELETE, UPDATE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configurar a Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton exportButton = toolbar.findViewById(R.id.toolbar_menu_button);
        exportButton.setOnClickListener(view -> showModalMainConfig());

        loadItemsAndShow();

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(view -> showModalToCreateGroup());

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();
                        setImageDialogPreview();
                        Toast.makeText(this, "Image Selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void loadItemsAndShow() {
        DatabaseGroupController crud = new DatabaseGroupController(getBaseContext());
        Cursor cursor = crud.loadData();
        itemList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                try {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(GroupTable.COLUMN_ID));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(GroupTable.COLUMN_NAME));
                    String imageUri = cursor.getString(cursor.getColumnIndexOrThrow(GroupTable.COLUMN_IMAGE));

                    itemList.add(new GroupItem(id, title, imageUri));
                } catch (Exception e) {
                    System.out.println("Error loading item: " + e.getMessage());
                }
            } while (cursor.moveToNext());

            cursor.close(); // Always close the cursor when done
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupItemAdapter(this, itemList, this::showConfigGroupModal);

        recyclerView.setAdapter(adapter);
    }

    private void updateName(int id){
        if (dialogActive==null) return;

        EditText titleTextView = dialogActive.findViewById(R.id.text_input);
        String inputText = titleTextView.getText().toString();

        if (inputText.isEmpty()) return;

        DatabaseGroupController crud = new DatabaseGroupController(getBaseContext());
        boolean result = crud.updateName(id,inputText);

        updateFeedback(result, UPDATE_TYPE.UPDATE);
    }

    private void updateImage(int id){
        String imageUriString = (imageUri != null) ? imageUri.toString() : "";

        DatabaseGroupController crud = new DatabaseGroupController(getBaseContext());
        boolean result = crud.updateImage(id, imageUriString);

        updateFeedback(result, UPDATE_TYPE.UPDATE);
    }

    private void updateFeedback(boolean result, UPDATE_TYPE updateType){

        if (!result){
            Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(
                this,
                (updateType == UPDATE_TYPE.UPDATE ) ? "Updated" : "Deleted" ,
                Toast.LENGTH_SHORT).show();
        closeDialogActive();

        loadItemsAndShow();
    }

    private void deleteGroup(int id){
        DatabaseGroupController crud = new DatabaseGroupController(getBaseContext());
        boolean result = crud.delete(id);

        updateFeedback(result, UPDATE_TYPE.DELETE);
    }

    private void showModalToCreateGroup() {
        final Dialog dialog = getDialogBox(R.layout.modal_new_group_layout);
        imageUri = null;

        ImageButton closeButton = dialog.findViewById(R.id.close_button);
        EditText textInput = dialog.findViewById(R.id.text_input);
        Button chooseImageButton = dialog.findViewById(R.id.choose_image_button);
        Button saveButton = dialog.findViewById(R.id.save_button);

        closeButton.setOnClickListener(v -> closeDialogActive());

        chooseImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            pickImageLauncher.launch(Intent.createChooser(intent, "Select Picture"));
        });

        saveButton.setOnClickListener(v -> {
            String inputText = textInput.getText().toString();
            String imageUriString = (imageUri != null) ? imageUri.toString() : "";

            if (inputText.isEmpty()) return;

            DatabaseGroupController crud = new DatabaseGroupController(getBaseContext());
            boolean result = crud.insert(inputText, imageUriString);

            loadItemsAndShow();

            if (result) {
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to save", Toast.LENGTH_SHORT).show();
            }

            closeDialogActive();
        });

        dialog.show();
    }

    private void showModalMainConfig() {
        final Dialog dialog = getDialogBox(R.layout.modal_config_main);

        ImageButton closeButton = dialog.findViewById(R.id.close_button);

        Button exportBtn = dialog.findViewById(R.id.btn_export);
        Button importBtn = dialog.findViewById(R.id.btn_import);

        closeButton.setOnClickListener(v -> closeDialogActive());

        exportBtn.setOnClickListener(v -> {
            boolean res= DatabaseExporter.exportDatabase(this);
             Toast.makeText(
                        this,
                        (res)?"SUCCESS in backup process":"FAIL in backup process",
                        Toast.LENGTH_SHORT).show();
             closeDialogActive();

        });
        importBtn.setOnClickListener(v -> {
            DatabaseExporter.importDatabase(this);
            loadItemsAndShow();
            closeDialogActive();
        });

        dialog.show();
    }

    private void showConfigGroupModal(int position) {
        GroupItem item = itemList.get(position);
        final Dialog dialog = getDialogBox(R.layout.modal_config_group_layout);
        imageUri = null;

        EditText titleTextView = dialog.findViewById(R.id.text_input);
        ImageButton closeButton = dialog.findViewById(R.id.close_button);
        Button deleteBtn = dialog.findViewById(R.id.delete_btn);
        Button saveButton = dialog.findViewById(R.id.save_button);
        Button saveImageButton = dialog.findViewById(R.id.save_image_button);
        Button chooseImageButton = dialog.findViewById(R.id.choose_image_button);

        titleTextView.setHint(item.getTitle());

        closeButton.setOnClickListener(v -> closeDialogActive());

        saveButton.setOnClickListener(v -> updateName(item.getId()));
        saveImageButton.setOnClickListener(v -> updateImage(item.getId()));
        deleteBtn.setOnClickListener(v -> deleteGroup(item.getId()));

        chooseImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            pickImageLauncher.launch(Intent.createChooser(intent, "Select Picture"));
        });

        dialog.show();
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

        dialogActive = dialog;
        return dialog;
    }

    private void closeDialogActive(){
        if (dialogActive==null) return;

        dialogActive.dismiss();
        dialogActive=null;
    }

    private void setImageDialogPreview(){
        if (dialogActive==null) return;

        ImageView imagePreview = dialogActive.findViewById(R.id.image_preview);

        if (imagePreview==null) return;

        imagePreview.setImageURI(imageUri);
        imagePreview.setVisibility(View.VISIBLE);
    }
}