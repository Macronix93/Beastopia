package de.uniks.beastopia.teaml.rest;

import java.util.Date;
import java.util.Map;
public record Monster(
        Date createdAt,
        Date updatedAt,
        String _id,
        String trainer,
        int type,
        int level,
        int experience,
        Map<String, Integer> abilities,
        MonsterAttributes attributes,
        MonsterAttributes currentAttributes
) {
}
