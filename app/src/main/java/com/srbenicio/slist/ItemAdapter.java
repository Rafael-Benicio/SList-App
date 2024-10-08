package com.srbenicio.slist;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.srbenicio.slist.controllers.DatabaseItemController;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<ItemList> items;
    private LayoutInflater inflater;
    private DatabaseItemController dbController;

    public ItemAdapter(Context context, List<ItemList> items) {
        this.inflater = LayoutInflater.from(context);
        this.items = items;
        this.dbController = new DatabaseItemController(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycle_item_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemList item = items.get(position);

        if (holder.recordTextWatcher != null) {
            holder.recordTextView.removeTextChangedListener(holder.recordTextWatcher);
        }

        holder.nameTextView.setText(item.getName());
        holder.descriptionTextView.setText(item.getDescription());

        // Prevent triggering TextWatcher when setting text programmatically
        holder.isUpdating = true;
        holder.recordTextView.setText(Integer.toString(item.getRecord()));
        holder.isUpdating = false;

        holder.itemCreatedInfo.setText("Created: " + item.getCreatedIn());
        holder.itemUpdatedInfo.setText("Updated: " + item.getLastUpdate());

        holder.nameTextView.setOnClickListener(v -> {
            if (holder.bottomBox.getVisibility() == View.GONE) {
                holder.bottomBox.setVisibility(View.VISIBLE);
            } else {
                holder.bottomBox.setVisibility(View.GONE);
            }
        });

        holder.incrementButton.setOnClickListener(v -> {
            int newRecord = item.getRecord() + 1;
            item.setRecord(newRecord);

            // Update UI without triggering TextWatcher
            holder.isUpdating = true;
            holder.recordTextView.setText(Integer.toString(newRecord));
            holder.isUpdating = false;

            dbController.updateRecord(item.getId(), newRecord);
        });

        holder.decrementButton.setOnClickListener(v -> {
            if (item.getRecord() > 0) {
                int newRecord = item.getRecord() - 1;
                item.setRecord(newRecord);

                // Update UI without triggering TextWatcher
                holder.isUpdating = true;
                holder.recordTextView.setText(Integer.toString(newRecord));
                holder.isUpdating = false;

                dbController.updateRecord(item.getId(), newRecord);
            }
        });

        // Create a new TextWatcher
        holder.recordTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Do nothing
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (holder.isUpdating) {
                    return; // Skip if updating programmatically
                }
                String text = s.toString();
                int newRecord;
                if (!text.isEmpty()) {
                    try {
                        newRecord = Integer.parseInt(text);
                    } catch (NumberFormatException e) {
                        newRecord = 0; // Default value or handle error
                    }
                } else {
                    newRecord = 0; // Default value
                }
                item.setRecord(newRecord);
                dbController.updateRecord(item.getId(), newRecord);
            }
        };

        // Add the TextWatcher to the EditText
        holder.recordTextView.addTextChangedListener(holder.recordTextWatcher);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView descriptionTextView;
        EditText recordTextView;
        LinearLayout bottomBox;
        TextView itemCreatedInfo;
        TextView itemUpdatedInfo;
        ImageButton incrementButton;
        ImageButton decrementButton;
        TextWatcher recordTextWatcher;
        boolean isUpdating = false; // Flag to prevent infinite loops

        ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.item_text);
            descriptionTextView = itemView.findViewById(R.id.item_description);
            recordTextView = itemView.findViewById(R.id.edit_quantity);
            bottomBox = itemView.findViewById(R.id.bottom_box);
            itemCreatedInfo = itemView.findViewById(R.id.item_created_info);
            itemUpdatedInfo = itemView.findViewById(R.id.item_updated_info);
            incrementButton = itemView.findViewById(R.id.btn_increment);
            decrementButton = itemView.findViewById(R.id.btn_decrement);
        }
    }


}
