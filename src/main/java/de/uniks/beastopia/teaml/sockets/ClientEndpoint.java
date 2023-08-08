package de.uniks.beastopia.teaml.sockets;

import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("unused")
@javax.websocket.ClientEndpoint
public class ClientEndpoint {
    private final URI endpointURI;

    private final List<Consumer<String>> messageHandlers = Collections.synchronizedList(new ArrayList<>());

    Session userSession;

    public ClientEndpoint(URI endpointURI) {
        this.endpointURI = endpointURI;
    }

    public boolean isOpen() {
        return this.userSession != null && this.userSession.isOpen();
    }

    public void open() {
        if (isOpen()) {
            return;
        }

        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            //noinspection resource
            container.connectToServer(this, endpointURI);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @OnOpen
    public void onOpen(Session userSession) {
        this.userSession = userSession;
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        this.messageHandlers.clear();
        this.userSession = null;
    }

    @OnMessage
    public synchronized void onMessage(String message) {
        List<Consumer<String>> handlersCopy = new ArrayList<>(this.messageHandlers);
        for (Consumer<String> handler : handlersCopy) {
            handler.accept(message);
        }
    }

    @OnError
    public void onError(Throwable error) {
        error.printStackTrace();
    }

    public void addMessageHandler(Consumer<String> handler) {
        this.messageHandlers.add(handler);
    }

    public void removeMessageHandler(Consumer<String> handler) {
        this.messageHandlers.remove(handler);
    }

    public void sendMessage(String message) {
        if (this.userSession == null) {
            return;
        }
        this.userSession.getAsyncRemote().sendText(message);
    }

    public void close() {
        if (this.userSession == null) {
            return;
        }
        try {
            this.userSession.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean hasMessageHandlers() {
        return !this.messageHandlers.isEmpty();
    }
}
