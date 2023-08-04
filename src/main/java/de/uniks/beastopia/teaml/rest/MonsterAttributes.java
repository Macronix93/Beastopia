package de.uniks.beastopia.teaml.rest;

public record MonsterAttributes(
        double health,
        int attack,
        int defense,
        int speed
) {
    public MonsterAttributes(double health, int attack, int defense, int speed) {
        this.health = Math.ceil(health);
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
    }
}