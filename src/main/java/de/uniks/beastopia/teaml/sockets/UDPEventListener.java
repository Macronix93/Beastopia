package de.uniks.beastopia.teaml.sockets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.rest.Event;
import de.uniks.beastopia.teaml.service.TokenStorage;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Pattern;

@Singleton
@SuppressWarnings("unused")
public class UDPEventListener {
    private final ObjectMapper mapper;
    DatagramSocket clientSocket;
    List<Consumer<String>> messageHandlers = new ArrayList<>();
    Thread receiver;

    @Inject
    public UDPEventListener(TokenStorage tokenStorage, ObjectMapper objectMapper) {
        this.mapper = objectMapper;
    }

    private void ensureOpen() {
        if (clientSocket != null && clientSocket.isConnected()) {
            return;
        }

        try {
            clientSocket = new DatagramSocket();
            this.receiver = new Thread(this::receive);
            this.receiver.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> Observable<Event<T>> listen(String pattern, Class<T> type) {
        return Observable.create(emitter -> {
            this.ensureOpen();
            send(Map.of("event", "subscribe", "data", pattern));
            final Consumer<String> handler = createPatternHandler(mapper, pattern, type, emitter);
            messageHandlers.add(handler);
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
        if (clientSocket == null) {
            return;
        }

        send(Map.of("event", "unsubscribe", "data", pattern));
        messageHandlers.remove(handler);
        if (messageHandlers.isEmpty()) {
            close();
        }
    }

    public void send(Map<String, String> message) {
        ensureOpen();
        try {
            final String json = mapper.writeValueAsString(message);
            final String host = Main.UDP_URL.split(":")[0];
            final InetAddress address = InetAddress.getByName(host);
            final int port = Integer.parseInt(Main.UDP_URL.split(":")[1]);
            final byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            final DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, port);
            clientSocket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void close() {
        if (clientSocket == null) {
            return;
        }

        clientSocket.close();
        clientSocket = null;
    }

    private void receive() {
        while (clientSocket != null) {
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                clientSocket.receive(packet);
                final String message = new String(packet.getData(), packet.getOffset(), packet.getLength());
                messageHandlers.forEach(handler -> handler.accept(message));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
