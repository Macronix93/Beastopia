package de.uniks.beastopia.teaml.rest;

import java.util.Date;

public record Message(
        Date createdAt,
        Date updatedAt,
        String _id,
        String sender,
        String body
) {
}
