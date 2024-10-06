package com.srbenicio.slist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.srbenicio.slist.ItemList;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<ItemList> items;
    private LayoutInflater inflater;

    public ItemAdapter(Context context, List<ItemList> items) {
        this.inflater = LayoutInflater.from(context);
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemList item = items.get(position);
        holder.nameTextView.setText(item.getName());
        holder.descriptionTextView.setText(item.getDescription());
        holder.recordTextView.setText(Integer.toString(item.getRecord()));
        holder.itemCreatedInfo.setText("Created: "+item.getCreatedIn());
        holder.itemUpdatedInfo.setText("Updated: "+item.getLastUpdate());
        holder.nameTextView.setOnClickListener(v -> {
            if (holder.bottomBox.getVisibility() == View.GONE) {
                holder.bottomBox.setVisibility(View.VISIBLE);
            } else {
                holder.bottomBox.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView descriptionTextView;
        TextView recordTextView;
        LinearLayout bottomBox;
        TextView itemCreatedInfo;
        TextView itemUpdatedInfo;

        ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.item_text);
            descriptionTextView = itemView.findViewById(R.id.item_description);
            recordTextView = itemView.findViewById(R.id.edit_quantity);
            bottomBox = itemView.findViewById(R.id.bottom_box);
            itemCreatedInfo = itemView.findViewById(R.id.item_created_info);
            itemUpdatedInfo = itemView.findViewById(R.id.item_updated_info);
        }
    }


}
