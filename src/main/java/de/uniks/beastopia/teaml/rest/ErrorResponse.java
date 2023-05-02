package de.uniks.beastopia.teaml.rest;

public record ErrorResponse(
        int statusCode,
        String message,
        String error
) {
}
