package de.uniks.beastopia.teaml.model;
import java.util.Objects;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Collection;

public class User
{
   public static final String PROPERTY_CREATED_AT = "createdAt";
   public static final String PROPERTY_UPDATED_AT = "updatedAt";
   public static final String PROPERTY_NAME = "name";
   public static final String PROPERTY_STATUS = "status";
   public static final String PROPERTY_AVATAR = "avatar";
   public static final String PROPERTY_GROUPS = "groups";
   public static final String PROPERTY_ID = "id";
   public static final String PROPERTY_FRIENDS = "friends";
   private String createdAt;
   private String updatedAt;
   private String name;
   private String status;
   private String avatar;
   protected PropertyChangeSupport listeners;
   private List<Group> groups;
   private String id;
   private List<User> friends;

   public String getCreatedAt()
   {
      return this.createdAt;
   }

   public User setCreatedAt(String value)
   {
      if (Objects.equals(value, this.createdAt))
      {
         return this;
      }

      final String oldValue = this.createdAt;
      this.createdAt = value;
      this.firePropertyChange(PROPERTY_CREATED_AT, oldValue, value);
      return this;
   }

   public String getUpdatedAt()
   {
      return this.updatedAt;
   }

   public User setUpdatedAt(String value)
   {
      if (Objects.equals(value, this.updatedAt))
      {
         return this;
      }

      final String oldValue = this.updatedAt;
      this.updatedAt = value;
      this.firePropertyChange(PROPERTY_UPDATED_AT, oldValue, value);
      return this;
   }

   public String getName()
   {
      return this.name;
   }

   public User setName(String value)
   {
      if (Objects.equals(value, this.name))
      {
         return this;
      }

      final String oldValue = this.name;
      this.name = value;
      this.firePropertyChange(PROPERTY_NAME, oldValue, value);
      return this;
   }

   public String getStatus()
   {
      return this.status;
   }

   public User setStatus(String value)
   {
      if (Objects.equals(value, this.status))
      {
         return this;
      }

      final String oldValue = this.status;
      this.status = value;
      this.firePropertyChange(PROPERTY_STATUS, oldValue, value);
      return this;
   }

   public String getAvatar()
   {
      return this.avatar;
   }

   public User setAvatar(String value)
   {
      if (Objects.equals(value, this.avatar))
      {
         return this;
      }

      final String oldValue = this.avatar;
      this.avatar = value;
      this.firePropertyChange(PROPERTY_AVATAR, oldValue, value);
      return this;
   }

   public List<Group> getGroups()
   {
      return this.groups != null ? Collections.unmodifiableList(this.groups) : Collections.emptyList();
   }

   public User withGroups(Group value)
   {
      if (this.groups == null)
      {
         this.groups = new ArrayList<>();
      }
      if (!this.groups.contains(value))
      {
         this.groups.add(value);
         value.withMembers(this);
         this.firePropertyChange(PROPERTY_GROUPS, null, value);
      }
      return this;
   }

   public User withGroups(Group... value)
   {
      for (final Group item : value)
      {
         this.withGroups(item);
      }
      return this;
   }

   public User withGroups(Collection<? extends Group> value)
   {
      for (final Group item : value)
      {
         this.withGroups(item);
      }
      return this;
   }

   public User withoutGroups(Group value)
   {
      if (this.groups != null && this.groups.remove(value))
      {
         value.withoutMembers(this);
         this.firePropertyChange(PROPERTY_GROUPS, value, null);
      }
      return this;
   }

   public User withoutGroups(Group... value)
   {
      for (final Group item : value)
      {
         this.withoutGroups(item);
      }
      return this;
   }

   public User withoutGroups(Collection<? extends Group> value)
   {
      for (final Group item : value)
      {
         this.withoutGroups(item);
      }
      return this;
   }

   public String getId()
   {
      return this.id;
   }

   public User setId(String value)
   {
      if (Objects.equals(value, this.id))
      {
         return this;
      }

      final String oldValue = this.id;
      this.id = value;
      this.firePropertyChange(PROPERTY_ID, oldValue, value);
      return this;
   }

   public List<User> getFriends()
   {
      return this.friends != null ? Collections.unmodifiableList(this.friends) : Collections.emptyList();
   }

   public User withFriends(User value)
   {
      if (this.friends == null)
      {
         this.friends = new ArrayList<>();
      }
      if (!this.friends.contains(value))
      {
         this.friends.add(value);
         this.firePropertyChange(PROPERTY_FRIENDS, null, value);
      }
      return this;
   }

   public User withFriends(User... value)
   {
      for (final User item : value)
      {
         this.withFriends(item);
      }
      return this;
   }

   public User withFriends(Collection<? extends User> value)
   {
      for (final User item : value)
      {
         this.withFriends(item);
      }
      return this;
   }

   public User withoutFriends(User value)
   {
      if (this.friends != null && this.friends.remove(value))
      {
         this.firePropertyChange(PROPERTY_FRIENDS, value, null);
      }
      return this;
   }

   public User withoutFriends(User... value)
   {
      for (final User item : value)
      {
         this.withoutFriends(item);
      }
      return this;
   }

   public User withoutFriends(Collection<? extends User> value)
   {
      for (final User item : value)
      {
         this.withoutFriends(item);
      }
      return this;
   }

   public boolean firePropertyChange(String propertyName, Object oldValue, Object newValue)
   {
      if (this.listeners != null)
      {
         this.listeners.firePropertyChange(propertyName, oldValue, newValue);
         return true;
      }
      return false;
   }

   public PropertyChangeSupport listeners()
   {
      if (this.listeners == null)
      {
         this.listeners = new PropertyChangeSupport(this);
      }
      return this.listeners;
   }

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder();
      result.append(' ').append(this.getCreatedAt());
      result.append(' ').append(this.getUpdatedAt());
      result.append(' ').append(this.getId());
      result.append(' ').append(this.getName());
      result.append(' ').append(this.getStatus());
      result.append(' ').append(this.getAvatar());
      return result.substring(1);
   }

   public void removeYou()
   {
      this.withoutGroups(new ArrayList<>(this.getGroups()));
      this.withoutFriends(new ArrayList<>(this.getFriends()));
   }
}
