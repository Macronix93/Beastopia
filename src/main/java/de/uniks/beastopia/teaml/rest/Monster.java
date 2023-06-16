package de.uniks.beastopia.teaml.rest;

import java.util.Date;

public record Monster(
        Date createdAt,
        Date updatedAt,
        String _id,
        String trainer,
        int type,
        int level,
        int experience,
        MonsterAttributes attributes,
        MonsterAttributes currentAttributes
) {
}
