package de.uniks.beastopia.teaml.rest;

import de.uniks.beastopia.teaml.utils.Variant;

public record UpdateOpponentDto(
        String monster,
        Variant<AbilityMove, ChangeMonsterMove> move
)
{}
