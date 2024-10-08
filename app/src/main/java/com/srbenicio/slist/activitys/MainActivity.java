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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configurar a Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadItemsAndShow();

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(view -> showModalDialog());

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();
                        setImageDialogPreviw();
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

    private void showConfigGroupModal(int position) {
        GroupItem item = itemList.get(position);
        final Dialog dialog = getDialogBox(R.layout.modal_config_group_layout);

        EditText titleTextView = dialog.findViewById(R.id.text_input);
        ImageButton closeButton = dialog.findViewById(R.id.close_button);
        Button deleteBtn = dialog.findViewById(R.id.delete_btn);

        titleTextView.setHint(item.getTitle());
        closeButton.setOnClickListener(v -> closeDialogActive(dialog));

        deleteBtn.setOnClickListener(v -> deleteGroup(item.getId(), dialog));

        // Show the dialog
        dialog.show();
    }

    private void deleteGroup(int id, Dialog dialog){
        DatabaseGroupController crud = new DatabaseGroupController(getBaseContext());
        boolean result = crud.delete(id);

        if (!result){
            Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
        closeDialogActive(dialog);// Close the modal dialog when deletion is successful
        loadItemsAndShow();  // Refresh the RecyclerView to reflect the changes
    }

    private Optional<Integer> getItemPosition(int id){
        Optional<Integer> position = Optional.empty();
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getId() == id) {
                position = Optional.of(i);
                break;
            }
        }
        return position;
    }

    private void showModalDialog() {
        final Dialog dialog = getDialogBox(R.layout.modal_new_group_layout);
        imageUri = null;

        ImageButton closeButton = dialog.findViewById(R.id.close_button);
        EditText textInput = dialog.findViewById(R.id.text_input);
        Button chooseImageButton = dialog.findViewById(R.id.choose_image_button);
        Button saveButton = dialog.findViewById(R.id.save_button);

        closeButton.setOnClickListener(v -> closeDialogActive(dialog));

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

            closeDialogActive(dialog);
        });

        dialog.show();
    }

    private void closeDialogActive(Dialog dialog){
        dialogActive=null;
        dialog.dismiss();
    }

    private void setImageDialogPreviw(){
        if (dialogActive==null) return;

        ImageView imagePreview = dialogActive.findViewById(R.id.image_preview);

        if (imagePreview==null) return;

        imagePreview.setImageURI(imageUri);
        imagePreview.setVisibility(View.VISIBLE);
    }
}