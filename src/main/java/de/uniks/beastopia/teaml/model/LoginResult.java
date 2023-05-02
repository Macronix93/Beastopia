package de.uniks.beastopia.teaml.model;
import java.util.Objects;
import java.beans.PropertyChangeSupport;

public class LoginResult
{
   public static final String PROPERTY_ACCESS_TOKEN = "accessToken";
   public static final String PROPERTY_REFRESH_TOKEN = "refreshToken";
   public static final String PROPERTY_USER = "user";
   private String accessToken;
   private String refreshToken;
   protected PropertyChangeSupport listeners;
   private User user;

   public String getAccessToken()
   {
      return this.accessToken;
   }

   public LoginResult setAccessToken(String value)
   {
      if (Objects.equals(value, this.accessToken))
      {
         return this;
      }

      final String oldValue = this.accessToken;
      this.accessToken = value;
      this.firePropertyChange(PROPERTY_ACCESS_TOKEN, oldValue, value);
      return this;
   }

   public String getRefreshToken()
   {
      return this.refreshToken;
   }

   public LoginResult setRefreshToken(String value)
   {
      if (Objects.equals(value, this.refreshToken))
      {
         return this;
      }

      final String oldValue = this.refreshToken;
      this.refreshToken = value;
      this.firePropertyChange(PROPERTY_REFRESH_TOKEN, oldValue, value);
      return this;
   }

   public User getUser()
   {
      return this.user;
   }

   public LoginResult setUser(User value)
   {
      if (this.user == value)
      {
         return this;
      }

      final User oldValue = this.user;
      this.user = value;
      this.firePropertyChange(PROPERTY_USER, oldValue, value);
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
      result.append(' ').append(this.getAccessToken());
      result.append(' ').append(this.getRefreshToken());
      return result.substring(1);
   }

   public void removeYou()
   {
      this.setUser(null);
   }
}
