package de.uniks.beastopia.teaml.rest;

import java.util.Date;
import java.util.List;

public record Group(
        Date createdAt,
        Date updatedAt,
        String _id,
        String name,
        List<String> members
) {
}