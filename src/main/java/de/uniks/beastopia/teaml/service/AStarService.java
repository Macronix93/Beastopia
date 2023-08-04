package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.Position;
import de.uniks.beastopia.teaml.rest.Tile;
import de.uniks.beastopia.teaml.rest.TileProperty;
import de.uniks.beastopia.teaml.utils.Direction;
import javafx.scene.Node;
import javafx.util.Pair;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AStarService {
    private boolean[][] map;
    private Position[][] mapOverride;
    private int[][] gscore;
    private int[][] fscore;
    private Position[][] parents;


    @Inject
    AStarService() {

    }

    public void buildMap(java.util.Map<Pair<Integer, Integer>, List<Pair<Tile, Node>>> mapInfo) {
        int maxX = mapInfo.keySet().stream().mapToInt(Pair::getKey).max().orElseThrow();
        int maxY = mapInfo.keySet().stream().mapToInt(Pair::getValue).max().orElseThrow();

        map = new boolean[maxX + 1][maxY + 1];
        mapOverride = new Position[maxX + 1][maxY + 1];

        for (var pos : mapInfo.keySet()) {
            int posx = pos.getKey();
            int posy = pos.getValue();

            List<List<TileProperty>> properties = new ArrayList<>();
            mapInfo.getOrDefault(new Pair<>(posx, posy), new ArrayList<>()).forEach(pair -> properties.add(pair.getKey().properties()));
            boolean walkable = properties.stream().allMatch(propertyList -> propertyList.stream().anyMatch(property -> property.name().equals("Walkable") && property.value().equals("true"))) && properties.stream().noneMatch(propertyList -> propertyList.stream().anyMatch(property -> property.name().equals("Jumpable")));

            for (var tileProperties : properties) {
                for (var prop : tileProperties) {
                    if (prop.name().equals("Jumpable")) {
                        int jumpDirection = Integer.parseInt(prop.value());
                        if (jumpDirection == Direction.UP.ordinal())
                            mapOverride[posx][posy] = new Position(posx, posy + 1);
                        else if (jumpDirection == Direction.DOWN.ordinal())
                            mapOverride[posx][posy] = new Position(posx, posy - 1);
                        else if (jumpDirection == Direction.LEFT.ordinal())
                            mapOverride[posx][posy] = new Position(posx + 1, posy);
                        else if (jumpDirection == Direction.RIGHT.ordinal())
                            mapOverride[posx][posy] = new Position(posx - 1, posy);
                    }
                }
            }

            map[posx][posy] = walkable;
        }
    }

    public void updateMap(Position pos, boolean walkable) {
        if (pos.x() < map.length && pos.y() < map[0].length && pos.x() >= 0 && pos.y() >= 0) {
            if (mapOverride[pos.x()][pos.y()] != null) {
                return;
            }
            map[pos.x()][pos.y()] = walkable;
        }
    }

    public boolean isWalkable(Position pos) {
        if (pos.x() < map.length && pos.y() < map[0].length && pos.x() >= 0 && pos.y() >= 0) {
            return map[pos.x()][pos.y()];
        }
        return false;
    }

    public boolean isJumpable(Position pos) {
        if (pos.x() < map.length && pos.y() < map[0].length && pos.x() >= 0 && pos.y() >= 0) {
            return mapOverride[pos.x()][pos.y()] != null;
        }
        return false;
    }

    public boolean isJumpableFrom(Position here, Position pos) {
        if (pos.x() < map.length && pos.y() < map[0].length && pos.x() >= 0 && pos.y() >= 0) {
            if (mapOverride[pos.x()][pos.y()] == null) {
                return false;
            }
            return mapOverride[pos.x()][pos.y()].equals(here);
        }
        return false;
    }

    public List<Position> findPath(Position start, Position end) {
        resetScores();

        Set<Position> open = new HashSet<>();

        gscore[start.x()][start.y()] = 0;
        fscore[start.x()][start.y()] = manhattanDistance(start, end);

        open.add(start);

        while (!open.isEmpty()) {
            Position current = open.stream().min((p1, p2) -> fscore[p1.x()][p1.y()] - fscore[p2.x()][p2.y()]).get();
            open.remove(current);

            if (current.equals(end)) {
                return createPath(current);
            }

            List<Position> neighbours = List.of(
                    new Position(current.x() + 1, current.y()),
                    new Position(current.x() - 1, current.y()),
                    new Position(current.x(), current.y() + 1),
                    new Position(current.x(), current.y() - 1)
            );

            for (Position neighbour : neighbours) {
                if (neighbour.x() < 0 || neighbour.y() < 0 ||
                        neighbour.x() >= map.length || neighbour.y() >= map[0].length) {
                    continue;
                }

                if (!isWalkable(neighbour) && !isJumpableFrom(current, neighbour)) {
                    continue;
                }

                int newGScore = gscore[current.x()][current.y()] + 1; // neighbours have distance of 1
                if (newGScore >= gscore[neighbour.x()][neighbour.y()]) {
                    continue;
                }

                parents[neighbour.x()][neighbour.y()] = current;
                gscore[neighbour.x()][neighbour.y()] = newGScore;
                fscore[neighbour.x()][neighbour.y()] = newGScore + manhattanDistance(neighbour, end);

                open.removeIf(p -> p.equals(neighbour));
                open.add(neighbour);
            }
        }

        return List.of();
    }

    private void resetScores() {
        gscore = new int[map.length][map[0].length];
        fscore = new int[map.length][map[0].length];
        parents = new Position[map.length][map[0].length];

        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                fscore[x][y] = Integer.MAX_VALUE;
                gscore[x][y] = Integer.MAX_VALUE;
                parents[x][y] = null;
            }
        }
    }

    private List<Position> createPath(Position end) {
        List<Position> path = new ArrayList<>();
        Position current = end;
        while (current != null && parents[current.x()][current.y()] != null) {
            path.add(0, current);
            current = parents[current.x()][current.y()];
        }

        // remove tiles after jumpable tiles
        for (int i = 0; i < path.size(); i++) {
            if (mapOverride[path.get(i).x()][path.get(i).y()] != null &&
                    i + 1 < path.size()) {
                path.remove(path.get(i + 1));
            }
        }

        return path;
    }

    private static int manhattanDistance(Position a, Position b) {
        return Math.abs(b.x() - a.x()) + Math.abs(b.y() - a.y());
    }
}
