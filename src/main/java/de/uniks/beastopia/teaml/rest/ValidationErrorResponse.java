package de.uniks.beastopia.teaml.rest;

import java.util.List;

public record ValidationErrorResponse(
        int statusCode,
        List<String> message,
        String error
) {
}
