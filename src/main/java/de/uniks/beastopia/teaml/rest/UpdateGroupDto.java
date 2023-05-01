package de.uniks.beastopia.teaml.rest;

import java.util.List;

public record UpdateGroupDto(
        String name,
        List<String> members
)
{}