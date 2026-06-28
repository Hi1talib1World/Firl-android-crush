package com.Denzo.firl.Discover;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.Denzo.firl.R;
import java.util.List;

public class InterestAdapter extends RecyclerView.Adapter<InterestAdapter.ViewHolder> {

    private List<String> interests;
    private OnInterestClickListener listener;

    public interface OnInterestClickListener {
        void onInterestClick(String interest);
    }

    public InterestAdapter(List<String> interests, OnInterestClickListener listener) {
        this.interests = interests;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.interest_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String interest = interests.get(position);
        holder.interestName.setText(interest);
        
        // Use a more generic subtitle
        holder.subtitle.setText("DISCOVER");

        // Cycle through images
        int resId = R.drawable.img1;
        if (position % 4 == 1) resId = R.drawable.img2;
        else if (position % 4 == 2) resId = R.drawable.img3;
        else if (position % 4 == 3) resId = R.drawable.img4;
        holder.interestImage.setImageResource(resId);

        holder.itemView.setOnClickListener(v -> listener.onInterestClick(interest));
    }

    @Override
    public int getItemCount() {
        return interests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView interestName, subtitle;
        ImageView interestImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            interestName = itemView.findViewById(R.id.interest_name);
            subtitle = itemView.findViewById(R.id.interest_subtitle);
            interestImage = itemView.findViewById(R.id.interest_image);
        }
    }
}
