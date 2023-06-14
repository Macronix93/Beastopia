package de.uniks.beastopia.teaml.rest;

import java.util.Date;

public record Achievement(
        Date createdAt,
        Date updatedAt,
        String id,
        String user,
        Date unlockedAt,
        int progress
) {
}
