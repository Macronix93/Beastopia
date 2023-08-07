package de.uniks.beastopia.teaml.rest;

public record Result(
        String type,
        int ability,
        int item,
        String effectiveness,
        String monster,
        String status
) {
}
