package de.uniks.beastopia.teaml.rest;

public record CreateUserDto(
        String name,
        String avatar,
        String password
)
{}