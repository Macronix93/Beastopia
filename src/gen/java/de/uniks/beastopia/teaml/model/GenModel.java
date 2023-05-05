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

    public class Message {
        String createdAt;
        String updatedAt;
        String _id;
        String body;

    }

    public class Group {
        String createdAt;
        String updatedAt;
        String _id;
        String name;
    }

    public class Region {
        String createdAt;
        String updatedAt;
        String _id;
        String name;
    }

    public class ValidationErrorRespone {
        int statusCode;
        String error;
        String message;
    }

}
