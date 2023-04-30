package de.uniks.beastopia.teaml.model;
import java.util.Objects;
import java.beans.PropertyChangeSupport;

public class ValidationErrorRespone
{
   public static final String PROPERTY_STATUS_CODE = "statusCode";
   public static final String PROPERTY_ERROR = "error";
   public static final String PROPERTY_MESSAGE = "message";
   private int statusCode;
   private String error;
   private String message;
   protected PropertyChangeSupport listeners;

   public int getStatusCode()
   {
      return this.statusCode;
   }

   public ValidationErrorRespone setStatusCode(int value)
   {
      if (value == this.statusCode)
      {
         return this;
      }

      final int oldValue = this.statusCode;
      this.statusCode = value;
      this.firePropertyChange(PROPERTY_STATUS_CODE, oldValue, value);
      return this;
   }

   public String getError()
   {
      return this.error;
   }

   public ValidationErrorRespone setError(String value)
   {
      if (Objects.equals(value, this.error))
      {
         return this;
      }

      final String oldValue = this.error;
      this.error = value;
      this.firePropertyChange(PROPERTY_ERROR, oldValue, value);
      return this;
   }

   public String getMessage()
   {
      return this.message;
   }

   public ValidationErrorRespone setMessage(String value)
   {
      if (Objects.equals(value, this.message))
      {
         return this;
      }

      final String oldValue = this.message;
      this.message = value;
      this.firePropertyChange(PROPERTY_MESSAGE, oldValue, value);
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
      result.append(' ').append(this.getError());
      result.append(' ').append(this.getMessage());
      return result.substring(1);
   }
}
