package de.uniks.beastopia.teaml.dto;
import java.util.Objects;
import java.beans.PropertyChangeSupport;

public class CreateUserDto
{
   public static final String PROPERTY_NAME = "name";
   public static final String PROPERTY_AVATAR = "avatar";
   public static final String PROPERTY_PASSWORD = "password";
   private String name;
   private String avatar;
   private String password;

   public String getName()
   {
      return this.name;
   }

   public CreateUserDto setName(String value)
   {
      this.name = value;
      return this;
   }

   public String getAvatar()
   {
      return this.avatar;
   }

   public CreateUserDto setAvatar(String value)
   {
      this.avatar = value;
      return this;
   }

   public String getPassword()
   {
      return this.password;
   }

   public CreateUserDto setPassword(String value)
   {
      this.password = value;
      return this;
   }

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder();
      result.append(' ').append(this.getName());
      result.append(' ').append(this.getAvatar());
      result.append(' ').append(this.getPassword());
      return result.substring(1);
   }
}
