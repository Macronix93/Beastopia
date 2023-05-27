package de.uniks.beastopia.teaml.rest;

import java.util.Date;

public record Trainer(
        Date createdAt,
        Date updatedAt,
        String _id,
        String region,
        String userId,
        String name,
        String image,
        int coins,
        String area,
        int x_pos,
        int y_pos,
        int direction,
        NPCInfo npc

) {
}
