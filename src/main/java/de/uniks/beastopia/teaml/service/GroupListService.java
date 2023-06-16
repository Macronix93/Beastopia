package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.CreateGroupDto;
import de.uniks.beastopia.teaml.rest.Group;
import de.uniks.beastopia.teaml.rest.GroupApiService;
import de.uniks.beastopia.teaml.rest.UpdateGroupDto;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class GroupListService {
    @Inject
    GroupApiService groupApiService;

    @Inject
    GroupListService() {
    }

    public Observable<List<Group>> getGroups() {
        return groupApiService.getGroups(null);
    }

    public Observable<Group> addGroup(String name, List<String> members) {
        return groupApiService.createGroup(new CreateGroupDto(name, members));
    }

    public Observable<Group> getGroup(String id) {
        return groupApiService.getGroup(id);
    }

    public Observable<Group> updateGroup(Group updatedGroup) {
        return groupApiService.updateGroup(updatedGroup._id(), new UpdateGroupDto(updatedGroup.name(), updatedGroup.members()));
    }

    public Observable<Group> addMember(Group group, String member) {
        List<String> members = new ArrayList<>(group.members());
        members.add(member);
        return groupApiService.updateGroup(group._id(), new UpdateGroupDto(group.name(), members));
    }

    public Observable<Group> removeMember(Group group, String member) {
        List<String> members = new ArrayList<>(group.members());
        members.remove(member);
        return groupApiService.updateGroup(group._id(), new UpdateGroupDto(group.name(), members));
    }

    public Observable<Group> deleteGroup(Group group) {
        return groupApiService.deleteGroup(group._id());
    }

    public String getGroupName(String idA, String idB) {
        String first = idA.substring(0, 5) + idA.substring(idA.length() - 5);
        String last = idB.substring(0, 5) + idB.substring(idB.length() - 5);

        return first + "_" + last;
    }

    public boolean isSingleChat(Group group) {
        if (group.members().size() != 2) {
            return false;
        }
        if (group.name().equals(getGroupName(group.members().get(0), group.members().get(1)))) {
            return true;
        }
        return group.name().equals(getGroupName(group.members().get(1), group.members().get(0)));
    }
}
