package de.uniks.beastopia.teaml.dto;

import java.util.List;

public record UpdateGroupDto(
        String name,
        List<String> members
)
{}