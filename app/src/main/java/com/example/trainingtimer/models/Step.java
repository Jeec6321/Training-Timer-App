package com.example.trainingtimer.models;

public class Step {

    private long workSeconds, restSeconds;

    public Step(long workSeconds, long restSeconds) {
        this.workSeconds = workSeconds;
        this.restSeconds = restSeconds;
    }

    public long getWorkSeconds() {
        return workSeconds;
    }

    public void setWorkSeconds(long workSeconds) {
        this.workSeconds = workSeconds;
    }

    public long getRestSeconds() {
        return restSeconds;
    }

    public void setRestSeconds(long restSeconds) {
        this.restSeconds = restSeconds;
    }
}
