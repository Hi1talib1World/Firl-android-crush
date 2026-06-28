package com.Denzo.firl.Conversations;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Denzo.firl.Model.ActiveUser;
import com.Denzo.firl.R;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActiveUserAdapter extends RecyclerView.Adapter<ActiveUserAdapter.ViewHolder> {

    private List<ActiveUser> activeUsers;
    private OnActiveUserClickListener listener;

    public interface OnActiveUserClickListener {
        void onUserClick(ActiveUser user);
    }

    public ActiveUserAdapter(List<ActiveUser> activeUsers, OnActiveUserClickListener listener) {
        this.activeUsers = activeUsers;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.active_user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ActiveUser user = activeUsers.get(position);
        holder.name.setText(user.getName());
        
        if (user.getImageUrl() != null && !user.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext()).load(user.getImageUrl()).into(holder.image);
        } else {
            holder.image.setImageResource(user.getImageResource() != 0 ? user.getImageResource() : R.drawable.logo);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onUserClick(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return activeUsers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.aui_image);
            name = itemView.findViewById(R.id.aui_name);
        }
    }
}
