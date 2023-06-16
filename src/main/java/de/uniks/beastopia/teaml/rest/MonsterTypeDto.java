package de.uniks.beastopia.teaml.rest;

import java.util.List;

public record MonsterTypeDto(
        int id,
        String name,
        String image,
        List<String> type,
        String description
) {
}
