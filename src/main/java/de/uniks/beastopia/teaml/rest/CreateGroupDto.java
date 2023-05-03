package de.uniks.beastopia.teaml.rest;

import java.util.List;

public record CreateGroupDto(
        String name,
        List<String> members
)
{}