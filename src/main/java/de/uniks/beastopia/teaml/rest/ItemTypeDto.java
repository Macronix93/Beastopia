package de.uniks.beastopia.teaml.rest;

public record ItemTypeDto(
        int id,
        String image,
        String name,
        int price,
        String description,
        String use
) {
}
