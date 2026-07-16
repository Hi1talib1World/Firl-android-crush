package com.Denzo.firl.Matches;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.Denzo.firl.Model.MatchPerson;
import com.Denzo.firl.R;
import com.Denzo.firl.chat.ChatActivity;
import com.bumptech.glide.Glide;

public class MatchesAdapter extends ListAdapter<MatchPerson, MatchesAdapter.ViewHolder> {

    private Context context;

    private static final DiffUtil.ItemCallback<MatchPerson> DIFF_CALLBACK = new DiffUtil.ItemCallback<MatchPerson>() {
        @Override
        public boolean areItemsTheSame(@NonNull MatchPerson oldItem, @NonNull MatchPerson newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull MatchPerson oldItem, @NonNull MatchPerson newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                   oldItem.getNumberOfPersons().equals(newItem.getNumberOfPersons()) &&
                   (oldItem.getImageUrl() == null ? newItem.getImageUrl() == null : oldItem.getImageUrl().equals(newItem.getImageUrl()));
        }
    };

    public MatchesAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.matched_user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MatchPerson match = getItem(position);
        holder.name.setText(match.getName());
        holder.profession.setText(match.getNumberOfPersons());
        
        if (match.getImageUrl() != null && !match.getImageUrl().isEmpty()) {
            Glide.with(context).load(match.getImageUrl()).into(holder.image);
        } else {
            holder.image.setImageResource(match.getImageResource() != 0 ? match.getImageResource() : R.drawable.logo);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("matchId", match.getId());
                intent.putExtra("matchName", match.getName());
                intent.putExtra("matchImageUrl", match.getImageUrl());
                context.startActivity(intent);
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
