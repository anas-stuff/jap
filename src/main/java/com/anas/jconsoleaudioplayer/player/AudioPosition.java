package com.anas.jconsoleaudioplayer.player;

public record AudioPosition(long currentMicroseconds, long totalMicroseconds) {
    public long getCurrentMicroseconds() {
        return currentMicroseconds;
    }

    public long getTotalMicroseconds() {
        return totalMicroseconds;
    }

    public int getCurrentSeconds() {
        return (int) (currentMicroseconds / 1000000);
    }

    public int getTotalSeconds() {
        return (int) (totalMicroseconds / 1000000);
    }

    public int getCurrentMinutes() {
        return this.getCurrentSeconds() / 60;
    }

    public int getTotalMinutes() {
        return this.getTotalSeconds() / 60;
    }

    public int getCurrentHours() {
        return this.getCurrentMinutes() / 60;
    }

    public int getTotalHours() {
        return this.getTotalMinutes() / 60;
    }
    public double getCurrentPercent() {
        return currentMicroseconds / (double) totalMicroseconds;
    }

    public String toString() {
        return String.format("%02d:%02d:%02d/%02d:%02d:%02d",
                this.getCurrentHours(), this.getCurrentMinutes(), this.getCurrentSeconds(),
                this.getTotalHours(), this.getTotalMinutes(), this.getTotalSeconds());
    }
}
