package de.uniks.beastopia.teaml;

import javafx.application.Application;

public class Main {
    public static final String API_URL = "https://stpmon.uniks.de/api/v3";
    public static final String WS_URL = "wss://stpmon.uniks.de/ws/v3";
    public static final String UDP_URL = "stpmon.uniks.de:30011";

    public static void main(String[] args) {
        Application.launch(App.class, args);
    }
}
