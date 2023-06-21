package de.uniks.beastopia.teaml.rest;

import java.util.Date;
import java.util.List;

public record Monster(
        Date createdAt,
        Date updatedAt,
        String _id,
        String trainer,
        int type,
        int level,
        int experience,
        List<Integer> abilities,
        MonsterAttributes attributes,
        MonsterAttributes currentAttributes
) {
}
