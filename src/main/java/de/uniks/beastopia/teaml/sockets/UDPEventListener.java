package de.uniks.beastopia.teaml.sockets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.rest.Event;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.utils.PatternHandlerProvider;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.channels.AsynchronousCloseException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Singleton
@SuppressWarnings("unused")
public class UDPEventListener {
    private final ObjectMapper mapper;
    DatagramSocket clientSocket;
    final List<Consumer<String>> messageHandlers = new ArrayList<>();
    Thread receiver;
    @Inject
    PatternHandlerProvider patternHandlerProvider;

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
            final String host = Main.UDP_URL.split(":")[0];
            final InetAddress address = InetAddress.getByName(host);
            final int port = Integer.parseInt(Main.UDP_URL.split(":")[1]);
            clientSocket.connect(address, port);
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
            final Consumer<String> handler = patternHandlerProvider.createPatternHandler(mapper, pattern, type, emitter);
            synchronized (messageHandlers) {
                messageHandlers.add(handler);
            }
            emitter.setCancellable(() -> removeEventHandler(pattern, handler));
        });
    }

    private void removeEventHandler(String pattern, Consumer<String> handler) {
        if (clientSocket == null) {
            return;
        }

        send(Map.of("event", "unsubscribe", "data", pattern));

        synchronized (messageHandlers) {
            messageHandlers.remove(handler);
        }
        if (messageHandlers.isEmpty()) {
            close();
        }
    }

    public void send(Map<String, String> message) {
        try {
            send(mapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(String message) {
        ensureOpen();
        try {
            final String host = Main.UDP_URL.split(":")[0];
            final InetAddress address = InetAddress.getByName(host);
            final int port = Integer.parseInt(Main.UDP_URL.split(":")[1]);
            final byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
            final DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, port);
            clientSocket.send(packet);
//            System.out.println("Sent: " + message);
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
//                System.out.println("Received: " + message);
                synchronized (messageHandlers) {
                    messageHandlers.forEach(handler -> handler.accept(message));
                }
            } catch (AsynchronousCloseException e) {
                // main thread closed the socket
                // exit gracefully
                return;
            } catch (SocketException e) {
                // socket was closed, try again
                if (clientSocket != null) {
                    ensureOpen();
                } else {
                    return;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
