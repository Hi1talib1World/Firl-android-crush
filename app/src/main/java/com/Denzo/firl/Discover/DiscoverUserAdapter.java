package com.Denzo.firl.Discover;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.Denzo.firl.Model.User;
import com.Denzo.firl.R;
import com.bumptech.glide.Glide;
import java.util.List;

public class DiscoverUserAdapter extends RecyclerView.Adapter<DiscoverUserAdapter.ViewHolder> {

    private Context context;
    private List<User> userList;
    private OnUserClickListener listener;
    private boolean isGridView = false;

    public interface OnUserClickListener {
        void onUserClick(User user);
    }

    public DiscoverUserAdapter(Context context, List<User> userList, OnUserClickListener listener) {
        this.context = context;
        this.userList = userList;
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
        User user = userList.get(position);
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

    @Override
    public int getItemCount() {
        return userList.size();
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
