package de.uniks.beastopia.teaml.model;
import java.util.Objects;
import java.beans.PropertyChangeSupport;

public class Region
{
   public static final String PROPERTY_CREATED_AT = "createdAt";
   public static final String PROPERTY_UPDATED_AT = "updatedAt";
   public static final String PROPERTY_NAME = "name";
   public static final String PROPERTY__ID = "_id";
   private String createdAt;
   private String updatedAt;
   private String name;
   protected PropertyChangeSupport listeners;
   private String _id;

   public String getCreatedAt()
   {
      return this.createdAt;
   }

   public Region setCreatedAt(String value)
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

   public Region setUpdatedAt(String value)
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

   public Region setName(String value)
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

   public String get_id()
   {
      return this._id;
   }

   public Region set_id(String value)
   {
      if (Objects.equals(value, this._id))
      {
         return this;
      }

      final String oldValue = this._id;
      this._id = value;
      this.firePropertyChange(PROPERTY__ID, oldValue, value);
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
      result.append(' ').append(this.get_id());
      result.append(' ').append(this.getName());
      return result.substring(1);
   }
}
