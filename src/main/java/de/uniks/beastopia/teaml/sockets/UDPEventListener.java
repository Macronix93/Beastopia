package de.uniks.beastopia.teaml.sockets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.rest.Event;
import de.uniks.beastopia.teaml.service.TokenStorage;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Pattern;

@Singleton
@SuppressWarnings("unused")
public class UDPEventListener {
    private final TokenStorage tokenStorage;
    private final ObjectMapper mapper;
    private ClientEndpoint endpoint;

    @Inject
    public UDPEventListener(TokenStorage tokenStorage, ObjectMapper objectMapper) {
        this.tokenStorage = tokenStorage;
        this.mapper = objectMapper;
    }

    private void ensureOpen() {
        if (endpoint != null && endpoint.isOpen()) {
            return;
        }

        final URI endpointURI;
        try {
            endpointURI = new URI(Main.UDP_URL + "/events?authToken=" + tokenStorage.getAccessToken());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        endpoint = new ClientEndpoint(endpointURI);
        endpoint.open();
    }

    public <T> Observable<Event<T>> listen(String pattern, Class<T> type) {
        return Observable.create(emitter -> {
            this.ensureOpen();
            send(Map.of("event", "subscribe", "data", pattern));
            final Consumer<String> handler = createPatternHandler(mapper, pattern, type, emitter);
            endpoint.addMessageHandler(handler);
            emitter.setCancellable(() -> removeEventHandler(pattern, handler));
        });
    }

    public static <T> Consumer<String> createPatternHandler(ObjectMapper mapper, String pattern, Class<T> type, ObservableEmitter<Event<T>> emitter) {
        // regions.*.created -> regions\.[^.]*\.created
        final Pattern regex = Pattern.compile(pattern.replace(".", "\\.").replace("*", "[^.]*"));
        return eventStr -> {
            try {
                final JsonNode node = mapper.readTree(eventStr);
                final String event = node.get("event").asText();
                if (!regex.matcher(event).matches()) {
                    return;
                }

                final T data = mapper.treeToValue(node.get("data"), type);
                emitter.onNext(new Event<>(event, data));
            } catch (Exception e) {
                emitter.onError(e);
            }
        };
    }

    private void removeEventHandler(String pattern, Consumer<String> handler) {
        if (endpoint == null) {
            return;
        }

        send(Map.of("event", "unsubscribe", "data", pattern));
        endpoint.removeMessageHandler(handler);
        if (!endpoint.hasMessageHandlers()) {
            close();
        }
    }

    private void send(Map<String, String> message) {
        ensureOpen();
        try {
            endpoint.sendMessage(mapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void close() {
        if (endpoint == null) {
            return;
        }

        endpoint.close();
        endpoint = null;
    }
}
