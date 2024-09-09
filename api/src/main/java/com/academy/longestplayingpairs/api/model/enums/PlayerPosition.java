package com.academy.longestplayingpairs.api.model.enums;

public enum PlayerPosition {
    GK("Goalkeeper"),
    DF("Defender"),
    MF("Midfielder"),
    FW("Forward");

    private final String fullName;

    PlayerPosition(String fullName) {
        this.fullName = fullName;
    }

    public String gerFullName() {
        return fullName;
    }
}
