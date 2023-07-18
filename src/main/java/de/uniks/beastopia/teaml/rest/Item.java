package de.uniks.beastopia.teaml.rest;

public record Item(
        String createdAt,
        String updatedAt,
        String _id,
        String trainer,
        int type,
        int amount
) {
}
