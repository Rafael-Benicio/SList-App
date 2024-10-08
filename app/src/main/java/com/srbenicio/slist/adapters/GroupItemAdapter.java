package com.srbenicio.slist.adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.srbenicio.slist.GroupItem;
import com.srbenicio.slist.R;
import com.srbenicio.slist.activitys.GroupList;

import java.util.List;

public class GroupItemAdapter extends RecyclerView.Adapter<GroupItemAdapter.ViewHolder> {

    private List<GroupItem> items;
    private LayoutInflater inflater;
    private OnGearIconClickListener gearIconClickListener;
    private Context context;

    public interface OnGearIconClickListener {
        void onGearIconClick(int position);
    }

    public GroupItemAdapter(Context context, List<GroupItem> items, OnGearIconClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.items = items;
        this.gearIconClickListener = listener;
        this.context = context;
    }

    @Override
    public GroupItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycle_group_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GroupItemAdapter.ViewHolder holder, int position) {
        GroupItem item = items.get(position);
        holder.titleTextView.setText(item.getTitle());

        if (item.getImageUri() != null && !item.getImageUri().isEmpty()) {
            Glide.with(holder.backgroundImageView.getContext())
                    .load(Uri.parse(item.getImageUri()))
                    .placeholder(R.drawable.placeholder_image)
                    .into(holder.backgroundImageView);
        } else {
            holder.backgroundImageView.setImageResource(R.drawable.placeholder_image);
        }

        // Set click listener on gearImageView
        holder.gearImageView.setOnClickListener(v -> {
            if (gearIconClickListener != null) {
                gearIconClickListener.onGearIconClick(position);
            }
        });

        // Set click listener on the entire card view
        holder.itemView.setOnClickListener(v -> {
            // Create an Intent to open the new activity
            Intent intent = new Intent(context, GroupList.class);
            // Pass data if needed
            intent.putExtra("ITEM_ID", item.getId());
            intent.putExtra("ITEM_TITLE", item.getTitle());
            context.startActivity(intent);
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
