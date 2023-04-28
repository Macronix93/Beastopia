package de.uniks.beastopia.teaml.dto;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import de.uniks.beastopia.teaml.model.User;
import java.util.Collections;
import java.util.Collection;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;

public class UpdateGroupDto
{
   public static final String PROPERTY_NAME = "name";
   public static final String PROPERTY_MEMBERS = "members";
   private String name;
   private List<User> members;

   public String getName()
   {
      return this.name;
   }

   public UpdateGroupDto setName(String value)
   {
      this.name = value;
      return this;
   }

   public List<User> getMembers()
   {
      return this.members != null ? Collections.unmodifiableList(this.members) : Collections.emptyList();
   }

   public UpdateGroupDto withMembers(User value)
   {
      if (this.members == null)
      {
         this.members = new ArrayList<>();
      }
      this.members.add(value);
      return this;
   }

   public UpdateGroupDto withMembers(User... value)
   {
      this.withMembers(Arrays.asList(value));
      return this;
   }

   public UpdateGroupDto withMembers(Collection<? extends User> value)
   {
      if (this.members == null)
      {
         this.members = new ArrayList<>(value);
      }
      else
      {
         this.members.addAll(value);
      }
      return this;
   }

   public UpdateGroupDto withoutMembers(User value)
   {
      this.members.removeAll(Collections.singleton(value));
      return this;
   }

   public UpdateGroupDto withoutMembers(User... value)
   {
      this.withoutMembers(Arrays.asList(value));
      return this;
   }

   public UpdateGroupDto withoutMembers(Collection<? extends User> value)
   {
      if (this.members != null)
      {
         this.members.removeAll(value);
      }
      return this;
   }

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder();
      result.append(' ').append(this.getName());
      return result.substring(1);
   }
}
