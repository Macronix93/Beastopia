package de.uniks.beastopia.teaml.rest;

import java.util.HashMap;
import java.util.List;

public record MapObject(
        int height,
        int id,
        String name,
        //Map<String, String> properties,
        List<HashMap<String, String>> properties,
        List<HashMap<String, Double>> polygon,
        int rotation,
        String type,
        boolean visible,
        int width,
        int x,
        int y
) {
}
