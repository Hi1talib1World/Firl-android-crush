package com.Denzo.firl.Matches;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.Denzo.firl.Model.User;
import com.Denzo.firl.R;
import com.bumptech.glide.Glide;
import java.util.List;

public class LikesYouAdapter extends RecyclerView.Adapter<LikesYouAdapter.ViewHolder> {

    private List<User> users;

    public LikesYouAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_likes_you, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        if (user.getProfileImageUrl() != null && !user.getProfileImageUrl().equals("default")) {
            Glide.with(holder.itemView.getContext()).load(user.getProfileImageUrl()).into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.monkey);
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.ly_image);
        }
    }
}
