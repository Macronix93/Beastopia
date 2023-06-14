package de.uniks.beastopia.teaml.rest;

public record AchievementsSummary(
        String id,
        int started,
        int unlocked,
        int progress
) {
}
