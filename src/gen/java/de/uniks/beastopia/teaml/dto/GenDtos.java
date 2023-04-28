package de.uniks.beastopia.teaml.dto;

import de.uniks.beastopia.teaml.model.GenModel;
import org.fulib.builder.ClassModelDecorator;
import org.fulib.builder.ClassModelManager;
import org.fulib.builder.Type;
import org.fulib.builder.reflect.DTO;

import java.util.List;

public class GenDtos implements ClassModelDecorator {
    @Override
    public void decorate(ClassModelManager m) {
        m.getClassModel().setDefaultPropertyStyle(Type.POJO);
        m.haveNestedClasses(GenDtos.class);
    }

    @DTO(model = GenModel.User.class, pick = {"name"})
    class LoginDto {
        String password;
    }

    @DTO(model = GenModel.LoginResult.class, pick = {"refreshToken"})
    class RefreshDto {
    }

    @DTO(model = GenModel.User.class, pick = {"name", "avatar"})
    class CreateUserDto {
        String password;
    }

    @DTO(model = GenModel.User.class, pick = {"name", "status", "avatar"})
    class UpdateUserDto {
        List<GenModel.User> friends;
        String password;
    }

    @DTO(model = GenModel.Group.class, pick = {"name"})
    class CreateGroupDto {
        List<GenModel.User> members;
    }

    @DTO(model = GenModel.Group.class, pick = {"name"})
    class UpdateGroupDto {
        List<GenModel.User> members;
    }

    @DTO(model = GenModel.Message.class, pick = {"body"})
    class CreateMessageDto {
    }

    @DTO(model = GenModel.Message.class, pick = {"body"})
    class UpdateMessageDto {
    }
}
