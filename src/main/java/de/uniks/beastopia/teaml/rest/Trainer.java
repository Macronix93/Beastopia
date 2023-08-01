package de.uniks.beastopia.teaml.rest;

import java.util.Date;
import java.util.List;

public record Trainer(
        Date createdAt,
        Date updatedAt,
        String _id,
        String region,
        String user,
        String name,
        String image,
        List<String> team,
        List<Integer> encounteredMonsterTypes,
        List<String> visitedAreas,
        float coins,
        String area,
        int x,
        int y,
        int direction,
        NPCInfo npc
) {
}