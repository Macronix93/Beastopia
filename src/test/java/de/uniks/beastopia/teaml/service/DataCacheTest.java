package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DataCacheTest {

    @SuppressWarnings("unused")
    @Mock
    PresetsService presetsService;

    @InjectMocks
    DataCache dataCache;

    @Test
    public void getSetUsers() {
        User user = new User(null, null, "ID", "NAME", "STATUS", "AVATAR", List.of());
        dataCache.addUser(user);
        assertEquals(dataCache.getUser("ID"), user);

        dataCache.setAllUsers(List.of());
        assertEquals(dataCache.getAllUsers(), List.of());

        dataCache.addUsers(List.of(user));
        assertEquals(dataCache.getAllUsers(), List.of(user));

        dataCache.removeUser(user);
        assertEquals(dataCache.getAllUsers(), List.of());
    }

    @Test
    public void fastTravel() {
        List<String> areas = List.of("AREA1", "AREA2");
        List<Area> areaList = List.of(
                new Area(null, null, "AREA1", "REGION", "AREA1", new Position(0, 0), null),
                new Area(null, null, "AREA2", "REGION", "AREA2", null, null)
        );
        Trainer trainer = new Trainer(null, null, "ID", "REGION", "USER", "NAME", "IMAGE", List.of(), List.of(), areas, 0, "AREA", 0, 0, 0, null);

        dataCache.setTrainer(trainer);
        dataCache.setAreas(areaList);
        areas.forEach(dataCache::addVisitedArea);

        assertEquals(dataCache.getTrainer(), trainer);
        assertEquals(dataCache.getAreas(), areaList);
        assertEquals(dataCache.getArea("AREA1"), areaList.get(0));
        assertEquals(dataCache.getVisitedAreas(), areas);

        assertTrue(dataCache.areaVisited("AREA1"));
        assertFalse(dataCache.areaVisited("AREA3"));

        assertTrue(dataCache.isFastTravelable("AREA1"));
        assertFalse(dataCache.isFastTravelable("AREA2"));
        assertFalse(dataCache.isFastTravelable("AREA3"));

        assertEquals(dataCache.getAreaByName("AREA1"), areaList.get(0));
        assertNull(dataCache.getAreaByName("AREA3"));
    }

    @Test
    public void region() {
        Region region = new Region(null, null, "ID", "NAME", null, null);
        dataCache.setRegion(region);
        assertEquals(dataCache.getJoinedRegion(), region);
    }

    @Test
    public void charactersImages() {
        Map<String, Trainer> trainers = Map.of(
                "1", new Trainer(null, null, "ID1", "REGION", "USER", "NAME1", "IMAGE1", List.of(), List.of(), List.of(), 0, "AREA", 0, 0, 0, null),
                "2", new Trainer(null, null, "ID2", "REGION", "USER", "NAME2", "IMAGE2", List.of(), List.of(), List.of(), 0, "AREA", 0, 0, 0, null)
        );

        List<Image> images = List.of(
                new WritableImage(1, 1),
                new WritableImage(2, 2)
        );

        List<String> characters = List.of(
                "IMAGE1",
                "IMAGE2"
        );

        dataCache.setTrainers(trainers);

        assertEquals(dataCache.getTrainers(), trainers);

        dataCache.setCharacters(characters);
        dataCache.setCharacterImage(characters.get(0), images.get(0));
        dataCache.setCharacterResizedImage(characters.get(0), images.get(0));
        dataCache.setCharacterImage(characters.get(1), images.get(1));
        dataCache.setCharacterResizedImage(characters.get(1), images.get(1));

        assertEquals(dataCache.getOrLoadTrainerImage(characters.get(0), false).blockingFirst(), images.get(0));
        assertEquals(dataCache.getOrLoadTrainerImage(characters.get(1), false).blockingFirst(), images.get(1));

        assertEquals(dataCache.getTrainer("ID1"), trainers.get("ID1"));

        assertEquals(dataCache.getCharacters().size(), 2);
        assertEquals(dataCache.getCharacterImage(characters.get(0)).getValue(), images.get(0));

        assertFalse(dataCache.getAvatarDataUrl(new BufferedImage(2, 2, 2)).isEmpty());
        assertThrows(RuntimeException.class, () -> dataCache.provideBufferedImage(new File("")));
    }

    @Test
    public void beasts() {
        List<MonsterTypeDto> monsters = List.of(
                new MonsterTypeDto(1, "NAME", "IMAGE", List.of("FIRE", "NORMAL"), "TESTBEAST")
        );

        dataCache.setAllBeasts(monsters);

        assertEquals(dataCache.getBeastDto(1), monsters.get(0));
        assertNull(dataCache.getBeastDto(2));

        List<Opponent> opponents = List.of(
                new Opponent(null, null, "ID", "ENCOUNTER", "TRAINER", false, true, "MONSTER", null, null, 0)
        );

        dataCache.setCurrentOpponents(opponents);
        assertEquals(dataCache.getCurrentOpponents(), opponents);

        assertEquals(dataCache.getOpponentByTrainerID("TRAINER"), opponents.get(0));
        assertNull(dataCache.getOpponentByTrainerID("TRAINER2"));

        Encounter encounter = new Encounter(null, null, "ID", "REGION", true);
        dataCache.setCurrentEncounter(encounter);
        assertEquals(dataCache.getCurrentEncounter(), encounter);
    }

    @Test
    public void achievements() {
        List<Achievement> achievements = List.of(
                new Achievement(null, null, "ID", "NAME", null, 2)
        );

        dataCache.addMyAchievement(achievements.get(0));
        assertEquals(dataCache.getMyAchievements(), achievements);

        dataCache.setMyAchievements(List.of());
        assertEquals(dataCache.getMyAchievements(), List.of());

        dataCache.setMyAchievements(achievements);
        dataCache.addAchievementDescription(achievements.get(0).id(), "DESCRIPTION");

        assertEquals(dataCache.getAchievementDescriptions().get(achievements.get(0).id()), "DESCRIPTION");
    }

    @Test
    public void items() {
        java.util.Map<Integer, Image> itemImages = Map.of(
                1, new WritableImage(1, 1)
        );

        dataCache.setItemImages(itemImages);
        assertEquals(dataCache.getItemImages(), itemImages);
    }

    @Test
    public void map() {
        TileSet tileSet = new TileSet(0, "IMAGE", 2, 2, 20, "NAME", 3, 2, 1, List.of());
        dataCache.setTileset(tileSet);
        assertEquals(dataCache.getMapTileset(), tileSet);

        Image mapImage = new WritableImage(1, 1);
        dataCache.setMapImage(mapImage);
        assertEquals(dataCache.getMapImage(), mapImage);
    }
}