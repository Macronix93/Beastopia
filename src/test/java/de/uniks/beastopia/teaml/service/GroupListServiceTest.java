package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.CreateGroupDto;
import de.uniks.beastopia.teaml.rest.Group;
import de.uniks.beastopia.teaml.rest.GroupApiService;
import de.uniks.beastopia.teaml.rest.UpdateGroupDto;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class GroupListServiceTest {

    @Mock
    @SuppressWarnings("unused")
    TokenStorage tokenStorage;
    @Mock
    GroupApiService groupApiService;
    @InjectMocks
    GroupListService groupListService;

    @Test
    void getGroups() {
        List<Group> groups = List.of(new Group(null, null, null, "GROUP0", List.of()));
        when(groupApiService.getGroups(null)).thenReturn(Observable.just(groups));
        List<Group> result = groupListService.getGroups().blockingFirst();
        assertEquals(groups, result);
        verify(groupApiService).getGroups(null);
    }

    @Test
    void getGroup() {
        Group group = new Group(null, null, "id", "GROUP0", List.of());
        when(groupApiService.getGroup("id")).thenReturn(Observable.just(group));
        Group result = groupListService.getGroup("id").blockingFirst();
        assertEquals(group, result);
        verify(groupApiService).getGroup("id");
    }

    @Test
    void addGroup() {
        Group group = new Group(null, null, "id", "GROUP0", List.of("ME"));
        CreateGroupDto createGroupDto = new CreateGroupDto("GROUP0", List.of("ME"));
        when(groupApiService.createGroup(createGroupDto)).thenReturn(Observable.just(group));
        Group result = groupListService.addGroup("GROUP0", List.of("ME")).blockingFirst();
        assertEquals(List.of("ME"), result.members());
        verify(groupApiService).createGroup(createGroupDto);
    }

    @Test
    void updateGroup() {
        Group group = new Group(null, null, "id", "GROUP1", List.of());
        UpdateGroupDto updateGroupDto = new UpdateGroupDto("GROUP1", List.of());
        when(groupApiService.updateGroup("id", updateGroupDto)).thenReturn(Observable.just(group));
        Group result = groupListService.updateGroup(group).blockingFirst();
        assertEquals(group, result);
        verify(groupApiService).updateGroup("id", updateGroupDto);
    }

    @Test
    void addMember() {
        Group group = new Group(null, null, "id", "GROUP1", List.of());
        Group resultGroup = new Group(null, null, "id", "GROUP1", List.of("memberID"));
        String memberID = "memberID";
        when(groupApiService.updateGroup(group._id(), new UpdateGroupDto(group.name(), List.of(memberID)))).thenReturn(Observable.just(resultGroup));
        Group result = groupListService.addMember(group, memberID).blockingFirst();
        assertEquals(List.of(memberID), result.members());
        verify(groupApiService).updateGroup(group._id(), new UpdateGroupDto(group.name(), List.of(memberID)));
    }

    @Test
    void removeMember() {
        Group group = new Group(null, null, "id", "GROUP1", List.of());
        String memberID = "memberID";
        when(groupApiService.updateGroup(group._id(), new UpdateGroupDto(group.name(), new ArrayList<>()))).thenReturn(Observable.just(group));
        Group result = groupListService.removeMember(group, memberID).blockingFirst();
        assertEquals(new ArrayList<>(), result.members());
        verify(groupApiService).updateGroup(group._id(), new UpdateGroupDto(group.name(), new ArrayList<>()));
    }

    @Test
    void deleteGroup() {
        Group group = new Group(null, null, "id", "GROUP1", List.of());
        when(groupApiService.deleteGroup("id")).thenReturn(Observable.just(group));
        Group result = groupListService.deleteGroup(group).blockingFirst();
        assertEquals(group, result);
        verify(groupApiService).deleteGroup("id");
    }
}