package de.uniks.beastopia.teaml.rest;

import java.util.List;

public record Chunk(
        List<Integer> data,
        int height,
        int width,
        int x,
        int y
) {
}
