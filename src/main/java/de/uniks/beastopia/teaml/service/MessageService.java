package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.*;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import java.util.List;

public class MessageService {
    public enum Namespace {
        Groups,
        Regions,
        Global
    }

    @Inject
    TokenStorage tokenStorage;

    @Inject
    MessageApiService messageApiService;

    @Inject
    public MessageService() {
    }

    public Observable<Message> sendMessageToGroup(Group group, String content) {
        return sendMessage(Namespace.Groups, group._id(), content);
    }

    public Observable<Message> sendMessageToFriend(User friend, String content) {
        return sendMessage(Namespace.Global, friend._id(), content);
    }

    public Observable<Message> sendMessageToRegion(Region region, String content) {
        return sendMessage(Namespace.Regions, region._id(), content);
    }

    public Observable<List<Message>> getMessagesFromGroup(Group group, String content) {
        return getMessages(Namespace.Groups, group._id());
    }

    public Observable<List<Message>> getMessagesFromFriend(User friend) {
        return getMessages(Namespace.Global, friend._id());
    }

    public Observable<List<Message>> getMessagesFromRegion(String regionId) {
        return getMessages(Namespace.Regions, regionId);
    }

    public Observable<Message> updateMessage(Group group, Message message, String newContent) {
        return updateMessage(Namespace.Groups, group._id(), message, newContent);
    }

    public Observable<Message> updateMessage(User friend, Message message, String newContent) {
        return updateMessage(Namespace.Global, friend._id(), message, newContent);
    }

    public Observable<Message> updateMessage(Region region, Message message, String newContent) {
        return updateMessage(Namespace.Regions, region._id(), message, newContent);
    }

    public Observable<Message> deleteMessage(Group group, Message message) {
        return deleteMessage(Namespace.Groups, group._id(), message);
    }

    public Observable<Message> deleteMessage(User friend, Message message) {
        return deleteMessage(Namespace.Global, friend._id(), message);
    }

    public Observable<Message> deleteMessage(Region region, Message message) {
        return deleteMessage(Namespace.Regions, region._id(), message);
    }

    private Observable<Message> sendMessage(Namespace namespace, String parent, String content) {
        return messageApiService.createMessage(namespaceToString(namespace), parent, new CreateMessageDto(content));
    }

    private Observable<List<Message>> getMessages(Namespace namespace, String parent) {
        return messageApiService.getMessages(namespaceToString(namespace), parent, null, null, null);
    }

    private Observable<Message> updateMessage(Namespace namespace, String parent, Message message, String newContent) {
        return messageApiService.updateMessage(namespaceToString(namespace), parent, message._id(), new UpdateMessageDto(newContent));
    }

    private Observable<Message> deleteMessage(Namespace namespace, String parent, Message message) {
        return messageApiService.deleteMessage(namespaceToString(namespace), parent, message._id());
    }

    private String namespaceToString(Namespace namespace) {
        return switch (namespace) {
            case Groups -> "groups";
            case Regions -> "regions";
            case Global -> "global";
        };
    }
}
