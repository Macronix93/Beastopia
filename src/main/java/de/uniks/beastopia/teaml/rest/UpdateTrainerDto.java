package de.uniks.beastopia.teaml.rest;

import java.util.List;

public record UpdateTrainerDto(
        String name,
        String image,
        List<String> team,
        String area
) {
}