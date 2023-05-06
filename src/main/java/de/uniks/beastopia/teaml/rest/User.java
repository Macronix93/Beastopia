package de.uniks.beastopia.teaml.rest;

import java.util.Date;
import java.util.List;

public record User(
        Date createdAt,
        Date updatedAt,
        String _id,
        String name,
        String status,
        String avatar,
        List<String> friends
) {
}
