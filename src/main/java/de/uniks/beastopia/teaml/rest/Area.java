package de.uniks.beastopia.teaml.rest;

import java.util.Date;

public record Area(
        Date createdAt,
        Date updatedAt,
        String _id,
        String region,
        String name,
        Position spawn,
        Map map
) {
}
