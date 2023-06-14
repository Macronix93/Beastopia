package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.rest.Area;
import de.uniks.beastopia.teaml.rest.Region;
import de.uniks.beastopia.teaml.rest.Trainer;
import de.uniks.beastopia.teaml.rest.User;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.util.Pair;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Singleton
public class DataCache {
    private List<User> users = new ArrayList<>();
    private List<Region> regions = new ArrayList<>();
    private List<Area> areas = new ArrayList<>();
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final List<Trainer> trainers = new ArrayList<>();
    private final List<Pair<String, Image>> characters = new ArrayList<>();
    Trainer trainer;
    Region joinedRegion;

    @Inject
    public DataCache() {
    }

    @SuppressWarnings("unused")
    public void addUser(User user) {
        users.add(user);
    }

    public void setAllUsers(List<User> users) {
        this.users = new ArrayList<>(users);
    }

    @SuppressWarnings("unused")
    public void addUsers(List<User> users) {
        this.users.addAll(users);
    }

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

    public List<Region> getRegions() {
        return regions;
    }

    public void setRegions(List<Region> regions) {
        this.regions = new ArrayList<>(regions);
    }

    public Region getRegion(String id) {
        return regions.stream()
                .filter(region -> region._id().equals(id))
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
        try {
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
        } catch (FileNotFoundException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return imageAvatar;
    }

    @SuppressWarnings("SameParameterValue")
    private static Image loadImage(String imageUrl, double width, double height, boolean preserveRatio, boolean smooth) throws FileNotFoundException, URISyntaxException {
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
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"));
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
        BufferedImage resized = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
        resized.getGraphics().drawImage(bufferedImage, 0, 0, null);
        return resized;
    }
}
