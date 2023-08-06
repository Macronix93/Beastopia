package de.uniks.beastopia.teaml.rest;

import de.uniks.beastopia.teaml.utils.Variant;

import java.util.List;

public record Opponent(
        String createdAt,
        String updatedAt,
        String _id,
        String encounter,
        String trainer,
        boolean isAttacker,
        boolean isNPC,
        String monster,
        Variant<AbilityMove, ChangeMonsterMove, UseItemMove> move,
        List<Result> results,
        int coins
) {
}
