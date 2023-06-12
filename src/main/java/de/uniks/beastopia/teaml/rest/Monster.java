package de.uniks.beastopia.teaml.rest;

import java.util.List;

public record Monster(
        String _id,
        String image,
        String name,
        List<String> type,
        String description

) {
}
