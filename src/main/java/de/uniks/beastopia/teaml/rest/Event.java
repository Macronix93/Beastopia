package de.uniks.beastopia.teaml.rest;

public record Event<T>(
        String event,
        T data
) {
    @SuppressWarnings("unused")
    public String suffix() {
        return event.substring(event.lastIndexOf(".") + 1);
    }
}
