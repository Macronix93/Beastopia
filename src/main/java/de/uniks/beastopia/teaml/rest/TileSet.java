package de.uniks.beastopia.teaml.rest;

public record TileSet(
        int columns,
        String image,
        int imageheight,
        int imagewidth,
        int margin,
        String name,
        int spacing,
        int tilecount,
        int tileheight
) {
}
