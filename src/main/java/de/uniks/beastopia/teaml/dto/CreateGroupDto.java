package de.uniks.beastopia.teaml.dto;

import java.util.List;

public record CreateGroupDto(
        String name,
        List<String> members
)
{}