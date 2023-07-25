package de.uniks.beastopia.teaml.rest;

import java.util.List;

public record Tile(
        int id,
        List<TileProperty> properties
) {
}
