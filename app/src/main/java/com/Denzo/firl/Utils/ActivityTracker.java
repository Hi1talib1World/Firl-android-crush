package com.Denzo.firl.Utils;

import com.Denzo.firl.Model.ActivityLog;
import java.util.ArrayList;
import java.util.List;

public class ActivityTracker {
    private static ActivityTracker instance;
    private List<ActivityLog> logs = new ArrayList<>();
    private OnLogAddedListener listener;

    public interface OnLogAddedListener {
        void onLogAdded(ActivityLog log);
    }

    private ActivityTracker() {}

    public static synchronized ActivityTracker getInstance() {
        if (instance == null) {
            instance = new ActivityTracker();
        }
        return instance;
    }

    public void log(String operation, ActivityLog.Status status, String details) {
        ActivityLog log = new ActivityLog(operation, status, details);
        logs.add(0, log); // Newest first
        if (listener != null) {
            listener.onLogAdded(log);
        }
    }

    public List<ActivityLog> getLogs() {
        return new ArrayList<>(logs);
    }

    public void setOnLogAddedListener(OnLogAddedListener listener) {
        this.listener = listener;
    }
}
