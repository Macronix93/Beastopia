package de.uniks.beastopia.teaml.rest;

import java.util.List;

public record Map(
        List<TileSetDescription> tilesets,
        List<Layer> layers,
        int width,
        int height,
        int tilewidth
) {
}
