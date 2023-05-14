package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.UpdateUserDto;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.rest.UserApiService;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class FriendListServiceTest {
    @Mock
    UserApiService userApiService;
    @Mock
    TokenStorage tokenStorage;

    @InjectMocks
    FriendListService friendListService;

    List<User> users = new ArrayList<>(List.of(
            new User(null, null, "ID0", "user1", "online", null, List.of("ID1", "ID2")),
            new User(null, null, "ID1", "user2", "online", null, List.of()),
            new User(null, null, "ID2", "user3", "offline", null, List.of())
    ));

    @Test
    void getUsers() {
        when(userApiService.getUsers(null, null)).thenReturn(Observable.just(users));
        List<User> users = friendListService.getUsers().blockingFirst();
        verify(userApiService).getUsers(null, null);
        assertArrayEquals(this.users.toArray(), users.toArray());
    }

    @Test
    void getFriendIDs() {
        when(tokenStorage.getCurrentUser()).thenReturn(users.get(0));
        List<String> friendIDs = friendListService.getFriendIDs();
        verify(tokenStorage).getCurrentUser();
        assertArrayEquals(List.of("ID1", "ID2").toArray(), friendIDs.toArray());
    }

    @Test
    void getUser() {
        when(userApiService.getUser("ID1")).thenReturn(Observable.just(users.get(0)));
        User user = friendListService.getUser("ID1").blockingFirst();
        verify(userApiService).getUser("ID1");
        assertEquals(users.get(0), user);
    }

    @Test
    void isFriend() {
        when(tokenStorage.getCurrentUser()).thenReturn(users.get(0));
        assertFalse(friendListService.isFriend(users.get(0)));
        assertTrue(friendListService.isFriend(users.get(1)));
        assertTrue(friendListService.isFriend(users.get(2)));
        verify(tokenStorage, times(3)).getCurrentUser();
    }

    @Test
    void getFriends() {
        when(tokenStorage.getCurrentUser()).thenReturn(users.get(0));
        when(userApiService.getUsers(List.of("ID1", "ID2"), null)).thenReturn(Observable.just(List.of(users.get(1), users.get(2))));
        List<User> friends = friendListService.getFriends().blockingFirst();
        verify(tokenStorage, times(2)).getCurrentUser();
        verify(userApiService).getUsers(List.of("ID1", "ID2"), null);
        assertArrayEquals(List.of(users.get(1), users.get(2)).toArray(), friends.toArray());
    }

    @Test
    void getFriendsWithStatus() {
        when(tokenStorage.getCurrentUser()).thenReturn(users.get(0));
        when(userApiService.getUsers(List.of("ID1", "ID2"), "online")).thenReturn(Observable.just(List.of(users.get(1))));
        List<User> friends = friendListService.getFriends(FriendListService.Status.Online).blockingFirst();
        verify(tokenStorage, times(2)).getCurrentUser();
        verify(userApiService).getUsers(List.of("ID1", "ID2"), "online");
        assertArrayEquals(List.of(users.get(1)).toArray(), friends.toArray());

        when(tokenStorage.getCurrentUser()).thenReturn(users.get(1));
        friends = friendListService.getFriends(FriendListService.Status.Online).blockingFirst();
        verify(tokenStorage, times(3)).getCurrentUser();
        assertArrayEquals(List.of().toArray(), friends.toArray());
    }

    @Test
    void getFriendsEmpty() {
        when(tokenStorage.getCurrentUser()).thenReturn(users.get(1));
        List<User> friends = friendListService.getFriends().blockingFirst();
        verify(tokenStorage).getCurrentUser();
        verify(userApiService, times(0)).getUsers(any(), any());
        assertArrayEquals(List.of().toArray(), friends.toArray());
    }

    @Test
    void addFriend() {
        when(tokenStorage.getCurrentUser()).thenReturn(users.get(1));
        when(userApiService.updateUser(users.get(1)._id(), new UpdateUserDto(null, null, null, List.of(users.get(0)._id()), null)))
                .thenReturn(Observable.just(new User(null, null, "ID1", "user2", "online", null, List.of(users.get(0)._id()))));
        doAnswer(user -> {
            users.set(1, user.getArgument(0));
            return null;
        }).when(tokenStorage).setCurrentUser(any());
        friendListService.addFriend(users.get(0)).subscribe();
        verify(tokenStorage, atLeastOnce()).getCurrentUser();
        verify(userApiService).updateUser(users.get(1)._id(), new UpdateUserDto(null, null, null, List.of(users.get(0)._id()), null));
        assertEquals(1, users.get(1).friends().size());
        assertEquals(users.get(0)._id(), users.get(1).friends().get(0));
    }

    @Test
    void removeFriend() {
        when(tokenStorage.getCurrentUser()).thenReturn(users.get(0));
        when(userApiService.updateUser(users.get(0)._id(), new UpdateUserDto(null, null, null, List.of(users.get(2)._id()), null)))
                .thenReturn(Observable.just(new User(null, null, "ID0", "user1", "online", null, List.of())));
        doAnswer(user -> {
            users.set(0, user.getArgument(0));
            return null;
        }).when(tokenStorage).setCurrentUser(any());
        friendListService.removeFriend(users.get(1)).subscribe();
        verify(tokenStorage, atLeastOnce()).getCurrentUser();
        verify(userApiService).updateUser(users.get(0)._id(), new UpdateUserDto(null, null, null, List.of(users.get(2)._id()), null));
        assertEquals(0, users.get(0).friends().size());
    }
}