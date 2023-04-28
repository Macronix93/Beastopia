package de.uniks.beastopia.teaml.dto;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Collections;
import java.util.Collection;
import java.beans.PropertyChangeSupport;
import de.uniks.beastopia.teaml.model.User;
import java.util.Arrays;

public class UpdateUserDto
{
   public static final String PROPERTY_NAME = "name";
   public static final String PROPERTY_STATUS = "status";
   public static final String PROPERTY_AVATAR = "avatar";
   public static final String PROPERTY_FRIENDS = "friends";
   public static final String PROPERTY_PASSWORD = "password";
   private String name;
   private String status;
   private String avatar;
   private List<User> friends;
   private String password;

   public String getName()
   {
      return this.name;
   }

   public UpdateUserDto setName(String value)
   {
      this.name = value;
      return this;
   }

   public String getStatus()
   {
      return this.status;
   }

   public UpdateUserDto setStatus(String value)
   {
      this.status = value;
      return this;
   }

   public String getAvatar()
   {
      return this.avatar;
   }

   public UpdateUserDto setAvatar(String value)
   {
      this.avatar = value;
      return this;
   }

   public List<User> getFriends()
   {
      return this.friends != null ? Collections.unmodifiableList(this.friends) : Collections.emptyList();
   }

   public UpdateUserDto withFriends(User value)
   {
      if (this.friends == null)
      {
         this.friends = new ArrayList<>();
      }
      this.friends.add(value);
      return this;
   }

   public UpdateUserDto withFriends(User... value)
   {
      this.withFriends(Arrays.asList(value));
      return this;
   }

   public UpdateUserDto withFriends(Collection<? extends User> value)
   {
      if (this.friends == null)
      {
         this.friends = new ArrayList<>(value);
      }
      else
      {
         this.friends.addAll(value);
      }
      return this;
   }

   public UpdateUserDto withoutFriends(User value)
   {
      this.friends.removeAll(Collections.singleton(value));
      return this;
   }

   public UpdateUserDto withoutFriends(User... value)
   {
      this.withoutFriends(Arrays.asList(value));
      return this;
   }

   public UpdateUserDto withoutFriends(Collection<? extends User> value)
   {
      if (this.friends != null)
      {
         this.friends.removeAll(value);
      }
      return this;
   }

   public String getPassword()
   {
      return this.password;
   }

   public UpdateUserDto setPassword(String value)
   {
      this.password = value;
      return this;
   }

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder();
      result.append(' ').append(this.getName());
      result.append(' ').append(this.getStatus());
      result.append(' ').append(this.getAvatar());
      result.append(' ').append(this.getPassword());
      return result.substring(1);
   }
}
