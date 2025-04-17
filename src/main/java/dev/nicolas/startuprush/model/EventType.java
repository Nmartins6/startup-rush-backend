package dev.nicolas.startuprush.model;

public enum EventType {
    PITCH(6),
    BUGS(-4),
    TRACTION(3),
    INVESTOR_ANGRY(-6),
    FAKE_NEWS(-8);

    private final int points;

    EventType(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }
}
