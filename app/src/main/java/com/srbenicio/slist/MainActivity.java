package com.srbenicio.slist;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.graphics.drawable.ColorDrawable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private List<Item> itemList;
    private FloatingActionButton fabAdd;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // ... código existente
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configurar a Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Inicializar dados
        itemList = new ArrayList<>();
        itemList.add(new Item("Item 1", R.drawable.placeholder_image));
        itemList.add(new Item("Item 2", R.drawable.placeholder_image));
        itemList.add(new Item("Item 2", R.drawable.placeholder_image));
        itemList.add(new Item("Item 2", R.drawable.placeholder_image));
        itemList.add(new Item("Item 2", R.drawable.placeholder_image));
        itemList.add(new Item("Item 2", R.drawable.placeholder_image));
        itemList.add(new Item("Item 2", R.drawable.placeholder_image));
        itemList.add(new Item("Item 2", R.drawable.placeholder_image));
        itemList.add(new Item("Item 2", R.drawable.placeholder_image));
        // Adicione mais itens conforme necessário

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ItemAdapter(this, itemList);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(view -> showModalDialog());
    }

    private void showModalDialog() {
        // Create the dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.modal_layout);
        dialog.setCancelable(false); // Prevent closing the dialog by tapping outside

        // Set the background to be transparent
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        // Find views in the dialog
        ImageButton closeButton = dialog.findViewById(R.id.close_button);
        EditText textInput = dialog.findViewById(R.id.text_input);
        Button chooseImageButton = dialog.findViewById(R.id.choose_image_button);
        Button saveButton = dialog.findViewById(R.id.save_button);

        // Set click listeners
        closeButton.setOnClickListener(v -> dialog.dismiss());

        chooseImageButton.setOnClickListener(v -> {
            // Handle image selection
            Toast.makeText(this, "Choose Image clicked", Toast.LENGTH_SHORT).show();
            // Implement image selection logic here
        });

        chooseImageButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        });


        saveButton.setOnClickListener(v -> {
            // Handle save action
            String inputText = textInput.getText().toString();
            Toast.makeText(this, "Saved: " + inputText, Toast.LENGTH_SHORT).show();
            // Implement save logic here
            dialog.dismiss();
        });

        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        // Show the dialog
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            // Handle the selected image URI as needed
            Toast.makeText(this, "Image Selected: " + imageUri.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
}