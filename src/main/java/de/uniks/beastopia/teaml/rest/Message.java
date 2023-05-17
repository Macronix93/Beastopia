package de.uniks.beastopia.teaml.rest;

import java.time.Instant;

public record Message(
        Instant createdAt,
        Instant updatedAt,
        String _id,
        String sender,
        String body
) {
}
