package de.uniks.beastopia.teaml.dto;
import java.util.Objects;
import java.beans.PropertyChangeSupport;

public class UpdateMessageDto
{
   public static final String PROPERTY_BODY = "body";
   private String body;

   public String getBody()
   {
      return this.body;
   }

   public UpdateMessageDto setBody(String value)
   {
      this.body = value;
      return this;
   }

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder();
      result.append(' ').append(this.getBody());
      return result.substring(1);
   }
}
