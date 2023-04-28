package de.uniks.beastopia.teaml.model;

import org.fulib.builder.ClassModelDecorator;
import org.fulib.builder.ClassModelManager;
import org.fulib.builder.reflect.Link;

import java.util.List;

public class GenModel implements ClassModelDecorator {

    @Override
    public void decorate(ClassModelManager mm) {
        mm.haveNestedClasses(GenModel.class);
    }

    public class User {
        String createdAt;
        String updatedAt;
        String name;
        String status;
        String avatar;

        @Link
        List<User> friends;
        @Link("members")
        List<Group> groups;
    }

    public class LoginResult {
        String accessToken;
        String refreshToken;

        @Link
        User user;
    }

    public class Message {
        String createdAt;
        String updatedAt;
        String id;
        String body;

        @Link
        User sender;
    }

    public class Group {
        String createdAt;
        String updatedAt;
        String id;
        String name;

        @Link("groups")
        List<User> members;
    }

    public class Region {
        String createdAt;
        String updatedAt;
        String id;
        String name;
    }

}
