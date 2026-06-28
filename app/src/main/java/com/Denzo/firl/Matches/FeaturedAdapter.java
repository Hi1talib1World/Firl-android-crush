package com.Denzo.firl.Matches;

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

public class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.ViewHolder> {

    private List<User> featuredUsers;

    public FeaturedAdapter(List<User> featuredUsers) {
        this.featuredUsers = featuredUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_featured_profile, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = featuredUsers.get(position);
        holder.nameAge.setText(user.getName() + ", " + user.getAge());
        holder.info.setText(user.getJob() != null ? user.getJob() : "Professional");
        
        if (user.getProfileImageUrl() != null && !user.getProfileImageUrl().equals("default")) {
            Glide.with(holder.itemView.getContext()).load(user.getProfileImageUrl()).into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.logo);
        }
    }

    @Override
    public int getItemCount() {
        return featuredUsers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView nameAge, info;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.featured_image);
            nameAge = itemView.findViewById(R.id.featured_name_age);
            info = itemView.findViewById(R.id.featured_info);
        }
    }
}
