package de.uniks.beastopia.teaml.rest;

public record Result(
        String type,
        String monster,
        int ability,
        String effectiveness,
        int item,
        String status
) {
}
