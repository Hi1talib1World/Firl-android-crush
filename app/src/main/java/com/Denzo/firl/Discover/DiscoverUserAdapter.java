package com.Denzo.firl.Discover;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.Denzo.firl.Model.User;
import com.Denzo.firl.R;
import com.bumptech.glide.Glide;

public class DiscoverUserAdapter extends ListAdapter<User, DiscoverUserAdapter.ViewHolder> {

    private Context context;
    private OnUserClickListener listener;
    private boolean isGridView = false;

    public interface OnUserClickListener {
        void onUserClick(User user);
    }

    private static final DiffUtil.ItemCallback<User> DIFF_CALLBACK = new DiffUtil.ItemCallback<User>() {
        @Override
        public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.getUid().equals(newItem.getUid());
        }

        @Override
        public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                   oldItem.getAge() == newItem.getAge() &&
                   (oldItem.getProfileImageUrl() == null ? newItem.getProfileImageUrl() == null : 
                    oldItem.getProfileImageUrl().equals(newItem.getProfileImageUrl()));
        }
    };

    public DiscoverUserAdapter(Context context, OnUserClickListener listener) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.listener = listener;
    }

    public void setGridView(boolean gridView) {
        isGridView = gridView;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = isGridView ? R.layout.item_discover_grid : R.layout.matched_user_item;
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = getItem(position);
        holder.name.setText(user.getName() + ", " + user.getAge());
        holder.profession.setText(user.getJob() != null ? user.getJob() : "Professional");

        if (user.getProfileImageUrl() != null && !user.getProfileImageUrl().equals("default")) {
            Glide.with(context).load(user.getProfileImageUrl()).into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.monkey);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onUserClick(user);
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, profession;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.mui_image);
            name = itemView.findViewById(R.id.mui_name);
            profession = itemView.findViewById(R.id.mui_profession);
        }
    }
}
