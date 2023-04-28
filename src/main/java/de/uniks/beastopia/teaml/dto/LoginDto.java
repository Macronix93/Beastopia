package de.uniks.beastopia.teaml.dto;
import java.util.Objects;
import java.beans.PropertyChangeSupport;

public class LoginDto
{
   public static final String PROPERTY_NAME = "name";
   public static final String PROPERTY_PASSWORD = "password";
   private String name;
   private String password;

   public String getName()
   {
      return this.name;
   }

   public LoginDto setName(String value)
   {
      this.name = value;
      return this;
   }

   public String getPassword()
   {
      return this.password;
   }

   public LoginDto setPassword(String value)
   {
      this.password = value;
      return this;
   }

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder();
      result.append(' ').append(this.getName());
      result.append(' ').append(this.getPassword());
      return result.substring(1);
   }
}
