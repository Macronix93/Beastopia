package de.uniks.beastopia.teaml.rest;

public record UpdateItemDto(
        int amount,
        int type,
        String monster
) {
}
