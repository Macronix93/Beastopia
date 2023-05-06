package de.uniks.beastopia.teaml.rest;

import java.util.Date;

public record Region(
        Date createdAt,
        Date updatedAt,
        String _id,
        String name
) {
}
