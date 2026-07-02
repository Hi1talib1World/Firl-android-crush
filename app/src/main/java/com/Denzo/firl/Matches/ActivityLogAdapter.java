package com.Denzo.firl.Matches;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.Denzo.firl.Model.ActivityLog;
import com.Denzo.firl.R;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActivityLogAdapter extends RecyclerView.Adapter<ActivityLogAdapter.ViewHolder> {
    private List<ActivityLog> logs;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

    public ActivityLogAdapter(List<ActivityLog> logs) {
        this.logs = logs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity_log, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ActivityLog log = logs.get(position);
        holder.opName.setText(log.getOperationName());
        holder.opDetails.setText(log.getDetails());
        holder.opTime.setText(sdf.format(new Date(log.getTimestamp())));

        int color;
        switch (log.getStatus()) {
            case SUCCESS: color = holder.itemView.getContext().getColor(R.color.likegreen); break;
            case FAILURE: color = holder.itemView.getContext().getColor(R.color.dislikered); break;
            default: color = holder.itemView.getContext().getColor(R.color.orange); break;
        }
        holder.indicator.getBackground().setTint(color);
    }

    @Override
    public int getItemCount() { return logs.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View indicator;
        TextView opName, opDetails, opTime;

        ViewHolder(View itemView) {
            super(itemView);
            indicator = itemView.findViewById(R.id.status_indicator);
            opName = itemView.findViewById(R.id.op_name);
            opDetails = itemView.findViewById(R.id.op_details);
            opTime = itemView.findViewById(R.id.op_time);
        }
    }
}
