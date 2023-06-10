package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.*;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {
    @Mock
    @SuppressWarnings("unused")
    TokenStorage tokenStorage;
    @Mock
    MessageApiService messageApiService;
    @InjectMocks
    MessageService messageService;

    @Test
    void sendMessageToGroup() {
        Group group = new Group(null, null, "GROUP", "GROUP", List.of());
        when(messageApiService.createMessage("groups", group._id(), new CreateMessageDto("Hello")))
                .thenReturn(Observable.just(new Message(null, null, "MSG_ID", "SENDER", "Hello")));
        Message message = messageService.sendMessageToGroup(group, "Hello").blockingFirst();
        assertEquals("MSG_ID", message._id());
        assertEquals("SENDER", message.sender());
        assertEquals("Hello", message.body());
        verify(messageApiService).createMessage("groups", group._id(), new CreateMessageDto("Hello"));
    }

    @Test
    void sendMessageToFriend() {
        User friend = new User(null, null, "FRIEND", "FRIEND", null, null, List.of());
        when(messageApiService.createMessage("global", friend._id(), new CreateMessageDto("Hello")))
                .thenReturn(Observable.just(new Message(null, null, "MSG_ID", "SENDER", "Hello")));
        Message message = messageService.sendMessageToFriend(friend, "Hello").blockingFirst();
        assertEquals("MSG_ID", message._id());
        assertEquals("SENDER", message.sender());
        assertEquals("Hello", message.body());
        verify(messageApiService).createMessage("global", friend._id(), new CreateMessageDto("Hello"));
    }

    @Test
    void sendMessageToRegion() {
        Region region = new Region(null, null, "REGIION", "REGION", null, null);
        when(messageApiService.createMessage("regions", region._id(), new CreateMessageDto("Hello")))
                .thenReturn(Observable.just(new Message(null, null, "MSG_ID", "SENDER", "Hello")));
        Message message = messageService.sendMessageToRegion(region, "Hello").blockingFirst();
        assertEquals("MSG_ID", message._id());
        assertEquals("SENDER", message.sender());
        assertEquals("Hello", message.body());
        verify(messageApiService).createMessage("regions", region._id(), new CreateMessageDto("Hello"));
    }

    @Test
    void getMessagesFromGroup() {
        when(messageApiService.getMessages("groups", "GROUP_ID", null, null, null))
                .thenReturn(Observable.just(List.of(new Message(null, null, "MSG_ID", "SENDER", "Hello"))));
        List<Message> message = messageService.getMessagesFromGroup("GROUP_ID").blockingFirst();
        assertEquals(1, message.size());
        assertEquals("MSG_ID", message.get(0)._id());
        assertEquals("SENDER", message.get(0).sender());
        assertEquals("Hello", message.get(0).body());
        verify(messageApiService).getMessages("groups", "GROUP_ID", null, null, null);
    }

    @Test
    void getMessagesFromFriend() {
        when(messageApiService.getMessages("global", "FRIEND_ID", null, null, null))
                .thenReturn(Observable.just(List.of(new Message(null, null, "MSG_ID", "SENDER", "Hello"))));
        List<Message> message = messageService.getMessagesFromFriend("FRIEND_ID").blockingFirst();
        assertEquals(1, message.size());
        assertEquals("MSG_ID", message.get(0)._id());
        assertEquals("SENDER", message.get(0).sender());
        assertEquals("Hello", message.get(0).body());
        verify(messageApiService).getMessages("global", "FRIEND_ID", null, null, null);
    }

    @Test
    void getMessagesFromRegion() {
        when(messageApiService.getMessages("regions", "REGION_ID", null, null, null))
                .thenReturn(Observable.just(List.of(new Message(null, null, "MSG_ID", "SENDER", "Hello"))));
        List<Message> message = messageService.getMessagesFromRegion("REGION_ID").blockingFirst();
        assertEquals(1, message.size());
        assertEquals("MSG_ID", message.get(0)._id());
        assertEquals("SENDER", message.get(0).sender());
        assertEquals("Hello", message.get(0).body());
        verify(messageApiService).getMessages("regions", "REGION_ID", null, null, null);
    }

    @Test
    void updateMessageGroup() {
        Group group = new Group(null, null, "GROUP", "GROUP", List.of());
        Message message = new Message(null, null, "MSG_ID", "SENDER", "Hello");
        when(messageApiService.updateMessage("groups", group._id(), message._id(), new UpdateMessageDto("World")))
                .thenReturn(Observable.just(new Message(null, null, "MSG_ID", "SENDER", "World")));
        Message updatedMessage = messageService.updateMessage(group, message, "World").blockingFirst();
        assertEquals("MSG_ID", updatedMessage._id());
        assertEquals("SENDER", updatedMessage.sender());
        assertEquals("World", updatedMessage.body());
        verify(messageApiService).updateMessage("groups", group._id(), message._id(), new UpdateMessageDto("World"));
    }

    @Test
    void updateMessageFriend() {
        User friend = new User(null, null, "FRIEND", "FRIEND", null, null, List.of());
        Message message = new Message(null, null, "MSG_ID", "SENDER", "Hello");
        when(messageApiService.updateMessage("global", friend._id(), message._id(), new UpdateMessageDto("World")))
                .thenReturn(Observable.just(new Message(null, null, "MSG_ID", "SENDER", "World")));
        Message updatedMessage = messageService.updateMessage(friend, message, "World").blockingFirst();
        assertEquals("MSG_ID", updatedMessage._id());
        assertEquals("SENDER", updatedMessage.sender());
        assertEquals("World", updatedMessage.body());
        verify(messageApiService).updateMessage("global", friend._id(), message._id(), new UpdateMessageDto("World"));
    }

    @Test
    void updateMessageRegion() {
        Region region = new Region(null, null, "REGION", "REGION", null, null);
        Message message = new Message(null, null, "MSG_ID", "SENDER", "Hello");
        when(messageApiService.updateMessage("regions", region._id(), message._id(), new UpdateMessageDto("World")))
                .thenReturn(Observable.just(new Message(null, null, "MSG_ID", "SENDER", "World")));
        Message updatedMessage = messageService.updateMessage(region, message, "World").blockingFirst();
        assertEquals("MSG_ID", updatedMessage._id());
        assertEquals("SENDER", updatedMessage.sender());
        assertEquals("World", updatedMessage.body());
        verify(messageApiService).updateMessage("regions", region._id(), message._id(), new UpdateMessageDto("World"));
    }

    @Test
    void deleteMessageGroup() {
        Group group = new Group(null, null, "GROUP", "GROUP", List.of());
        Message message = new Message(null, null, "MSG_ID", "SENDER", "Hello");
        when(messageApiService.deleteMessage("groups", group._id(), message._id()))
                .thenReturn(Observable.just(new Message(null, null, "MSG_ID", "SENDER", "Hello")));
        Message result = messageService.deleteMessage(group, message).blockingFirst();
        assertEquals("MSG_ID", result._id());
        assertEquals("SENDER", result.sender());
        assertEquals("Hello", result.body());
        verify(messageApiService).deleteMessage("groups", group._id(), message._id());
    }

    @Test
    void deleteMessageFriend() {
        User friend = new User(null, null, "FRIEND", "FRIEND", null, null, List.of());
        Message message = new Message(null, null, "MSG_ID", "SENDER", "Hello");
        when(messageApiService.deleteMessage("global", friend._id(), message._id()))
                .thenReturn(Observable.just(new Message(null, null, "MSG_ID", "SENDER", "Hello")));
        Message result = messageService.deleteMessage(friend, message).blockingFirst();
        assertEquals("MSG_ID", result._id());
        assertEquals("SENDER", result.sender());
        assertEquals("Hello", result.body());
        verify(messageApiService).deleteMessage("global", friend._id(), message._id());
    }

    @Test
    void deleteMessageRegion() {
        Region region = new Region(null, null, "REGION", "REGION", null, null);
        Message message = new Message(null, null, "MSG_ID", "SENDER", "Hello");
        when(messageApiService.deleteMessage("regions", region._id(), message._id()))
                .thenReturn(Observable.just(new Message(null, null, "MSG_ID", "SENDER", "Hello")));
        Message result = messageService.deleteMessage(region, message).blockingFirst();
        assertEquals("MSG_ID", result._id());
        assertEquals("SENDER", result.sender());
        assertEquals("Hello", result.body());
        verify(messageApiService).deleteMessage("regions", region._id(), message._id());
    }
}