package com.shadoww.authservice.model;

public enum Theme {
    LIGHT, DARK;

    public static Theme next(Theme theme) {
        return switch (theme) {
            case DARK -> LIGHT;
            case LIGHT -> DARK;
        };
    }
}