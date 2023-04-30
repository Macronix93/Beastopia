package de.uniks.beastopia.teaml.model;
import java.util.Objects;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Collection;

public class Group
{
   public static final String PROPERTY_CREATED_AT = "createdAt";
   public static final String PROPERTY_UPDATED_AT = "updatedAt";
   public static final String PROPERTY_ID = "id";
   public static final String PROPERTY_NAME = "name";
   public static final String PROPERTY_MEMBERS = "members";
   private String createdAt;
   private String updatedAt;
   private String id;
   private String name;
   protected PropertyChangeSupport listeners;
   private List<User> members;

   public String getCreatedAt()
   {
      return this.createdAt;
   }

   public Group setCreatedAt(String value)
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

   public Group setUpdatedAt(String value)
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

   public String getId()
   {
      return this.id;
   }

   public Group setId(String value)
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

   public String getName()
   {
      return this.name;
   }

   public Group setName(String value)
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

   public List<User> getMembers()
   {
      return this.members != null ? Collections.unmodifiableList(this.members) : Collections.emptyList();
   }

   public Group withMembers(User value)
   {
      if (this.members == null)
      {
         this.members = new ArrayList<>();
      }
      if (!this.members.contains(value))
      {
         this.members.add(value);
         value.withGroups(this);
         this.firePropertyChange(PROPERTY_MEMBERS, null, value);
      }
      return this;
   }

   public Group withMembers(User... value)
   {
      for (final User item : value)
      {
         this.withMembers(item);
      }
      return this;
   }

   public Group withMembers(Collection<? extends User> value)
   {
      for (final User item : value)
      {
         this.withMembers(item);
      }
      return this;
   }

   public Group withoutMembers(User value)
   {
      if (this.members != null && this.members.remove(value))
      {
         value.withoutGroups(this);
         this.firePropertyChange(PROPERTY_MEMBERS, value, null);
      }
      return this;
   }

   public Group withoutMembers(User... value)
   {
      for (final User item : value)
      {
         this.withoutMembers(item);
      }
      return this;
   }

   public Group withoutMembers(Collection<? extends User> value)
   {
      for (final User item : value)
      {
         this.withoutMembers(item);
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
      return result.substring(1);
   }

   public void removeYou()
   {
      this.withoutMembers(new ArrayList<>(this.getMembers()));
   }
}
