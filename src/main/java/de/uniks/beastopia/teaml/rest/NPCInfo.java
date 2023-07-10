package de.uniks.beastopia.teaml.rest;

import java.util.List;

public record NPCInfo(
        boolean walkRandomly,
        boolean encounterOnSight,
        boolean encounterOnTalk,
        boolean canHeal,
        List<String> encountered,
        List<Integer> starters
) {
}
