package com.Denzo.firl.Model;

public class ActivityLog {
    public enum Status {
        SUCCESS, FAILURE, PENDING
    }

    private String id;
    private long timestamp;
    private String operationName;
    private Status status;
    private String details;

    public ActivityLog(String operationName, Status status, String details) {
        this.timestamp = System.currentTimeMillis();
        this.operationName = operationName;
        this.status = status;
        this.details = details;
    }

    public long getTimestamp() { return timestamp; }
    public String getOperationName() { return operationName; }
    public Status getStatus() { return status; }
    public String getDetails() { return details; }
}
