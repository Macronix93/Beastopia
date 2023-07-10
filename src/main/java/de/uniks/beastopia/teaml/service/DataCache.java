package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.rest.Achievement;
import de.uniks.beastopia.teaml.rest.Area;
import de.uniks.beastopia.teaml.rest.Encounter;
import de.uniks.beastopia.teaml.rest.MonsterTypeDto;
import de.uniks.beastopia.teaml.rest.Opponent;
import de.uniks.beastopia.teaml.rest.Region;
import de.uniks.beastopia.teaml.rest.Trainer;
import de.uniks.beastopia.teaml.rest.User;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.util.Pair;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Singleton
public class DataCache {
    private List<User> users = new ArrayList<>();
    private List<Area> areas = new ArrayList<>();
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final List<Trainer> trainers = new ArrayList<>();
    private final List<Pair<String, Image>> characters = new ArrayList<>();
    private List<Achievement> myAchievements = new ArrayList<>();
    private final Map<String, String> achievementDescriptions = new HashMap<>();
    private final List<String> visitedAreas = new ArrayList<>();
    Trainer trainer;
    Region joinedRegion;
    private List<MonsterTypeDto> allBeasts = new ArrayList<>();
    private List<Opponent> currentOpponents = new ArrayList<>();
    Encounter currentEncounter;

    @Inject
    public DataCache() {
    }

    @SuppressWarnings("unused")
    public void addUser(User user) {
        users.add(user);
    }

    /**
     * Sets the list of all users the server is aware of
     *
     * @param users The list of all users
     */
    public void setAllUsers(List<User> users) {
        this.users = new ArrayList<>(users);
    }

    /**
     * Adds a range of user to the list of all users the server is aware of
     *
     * @param users The list of all users
     */
    @SuppressWarnings("unused")
    public void addUsers(List<User> users) {
        this.users.addAll(users);
    }

    /**
     * Removes a user from the list of all users the server is aware of
     *
     * @param user The user to remove
     */
    @SuppressWarnings("unused")
    public void removeUser(User user) {
        users.remove(user);
    }

    public List<User> getAllUsers() {
        return users;
    }

    public User getUser(String id) {
        return users.stream()
                .filter(user -> user._id().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void setRegion(Region region) {
        this.joinedRegion = region;
    }

    public Region getJoinedRegion() {
        return joinedRegion;
    }

    public void setAreas(List<Area> areas) {
        this.areas = new ArrayList<>(areas);
    }

    public List<Area> getAreas() {
        return this.areas;
    }

    public Area getArea(String id) {
        return areas.stream()
                .filter(area -> area._id().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setCharacters(List<String> characters) {
        synchronized (this.characters) {
            for (String character : characters) {
                if (this.characters.stream().anyMatch(pair -> pair.getKey().equals(character)))
                    continue;
                this.characters.add(new Pair<>(character, null));
            }

            this.characters.removeIf(pair -> characters.stream().noneMatch(character -> character.equals(pair.getKey())));
        }
    }

    public void setCharacterImage(String name, Image image) {
        synchronized (characters) {
            characters.removeIf(pair -> pair.getKey().equals(name));
            characters.add(new Pair<>(name, image));
        }
    }

    public void setTrainers(List<Trainer> trainers) {
        this.trainers.clear();
        this.trainers.addAll(trainers);
    }

    public List<Trainer> getTrainers() {
        return trainers;
    }

    public Trainer getTrainer(String id) {
        return trainers.stream()
                .filter(trainer -> trainer._id().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Pair<String, Image>> getCharacters() {
        synchronized (characters) {
            return new ArrayList<>(characters);
        }
    }

    public Pair<String, Image> getCharacterImage(String image) {
        synchronized (characters) {
            return characters.stream()
                    .filter(pair -> pair.getKey().equals(image))
                    .findFirst()
                    .orElse(null);
        }
    }

    public Image getImageAvatar(User user) {
        Image imageAvatar;
        if (user.avatar() != null && user.avatar().contains("data:image/png;base64,")) {
            String avatar = user.avatar().replace("data:image/png;base64,", "").trim();
            byte[] imageData = Base64.getDecoder().decode(avatar);
            imageAvatar = new Image(new ByteArrayInputStream(imageData));
        } else if (user.avatar() != null && user.avatar().contains("https://")) {
            imageAvatar = loadImage(user.avatar(), 40.0, 40.0, false, false);
        } else {
            imageAvatar = loadImage(Objects.requireNonNull(Main.class.getResource("assets/user.png")).toString(),
                    40.0, 40.0, false, false);
        }
        return imageAvatar;
    }

    @SuppressWarnings("SameParameterValue")
    private static Image loadImage(String imageUrl, double width, double height, boolean preserveRatio, boolean smooth) {
        return new Image(imageUrl, width, height, preserveRatio, smooth);
    }

    public String getAvatarDataUrl(BufferedImage bufferedImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes.toByteArray());
    }

    public File provideImageFile(App app) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Avatar");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        return fileChooser.showOpenDialog(app.getStage());
    }

    public BufferedImage provideBufferedImage(File file) {
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return resizeImage(bufferedImage);
    }

    private BufferedImage resizeImage(BufferedImage bufferedImage) {
        BufferedImage resized = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = resized.createGraphics();
        graphics.drawImage(bufferedImage, 0, 0, 64, 64, null);
        graphics.dispose();
        return resized;
    }

    public void setAllBeasts(List<MonsterTypeDto> beasts) {
        this.allBeasts = beasts;
    }

    public MonsterTypeDto getBeastDto(int id) {
        return allBeasts.stream()
                .filter(monsterTypeDto -> monsterTypeDto.id() == id)
                .findFirst()
                .orElse(null);
    }

    public void addMyAchievement(Achievement achievement) {
        this.myAchievements.add(achievement);
    }

    public void setMyAchievements(List<Achievement> achievements) {
        this.myAchievements = new ArrayList<>(achievements);
    }

    public List<Achievement> getMyAchievements() {
        return this.myAchievements;
    }

    public void addAchievementDescription(String achievementId, String achievementDescription) {
        this.achievementDescriptions.put(achievementId, achievementDescription);
    }

    public Map<String, String> getAchievementDescriptions() {
        return this.achievementDescriptions;
    }

    public void addVisitedArea(String id) {
        this.visitedAreas.add(id);
    }

    public List<String> getVisitedAreas() {
        return this.visitedAreas;
    }

    public void setCurrentOpponents(List<Opponent> opponents) {
        this.currentOpponents = new ArrayList<>(opponents);
    }

    public List<Opponent> getCurrentOpponents() {
        return this.currentOpponents;
    }

    public void setCurrentEncounter(Encounter encounter) {
        this.currentEncounter = encounter;
    }

    public Encounter getCurrentEncounter() {
        return this.currentEncounter;
    }
}
