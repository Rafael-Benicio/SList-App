package com.srbenicio.slist;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<Item> items;
    private LayoutInflater inflater;
    private OnGearIconClickListener gearIconClickListener;

    public interface OnGearIconClickListener {
        void onGearIconClick(int position);
    }

    public ItemAdapter(Context context, List<Item> items, OnGearIconClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.items = items;
        this.gearIconClickListener = listener;
    }

    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemAdapter.ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.titleTextView.setText(item.getTitle());
        holder.backgroundImageView.setImageResource(item.getImageResource());

        // Set click listener on gearImageView
        holder.gearImageView.setOnClickListener(v -> {
            if (gearIconClickListener != null) {
                gearIconClickListener.onGearIconClick(position);
            }
        });
    }


    @Override
    public int getItemCount() { return items.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView backgroundImageView;
        TextView titleTextView;
        ImageView gearImageView;

        ViewHolder(View itemView) {
            super(itemView);
            backgroundImageView = itemView.findViewById(R.id.item_background_image);
            titleTextView = itemView.findViewById(R.id.item_title);
            gearImageView = itemView.findViewById(R.id.item_gear_icon);
        }
    }
}
