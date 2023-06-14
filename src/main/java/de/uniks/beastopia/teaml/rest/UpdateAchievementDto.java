package de.uniks.beastopia.teaml.rest;

import java.util.Date;

public record UpdateAchievementDto(
        Date unlockedAt,
        int progress
) {
}
