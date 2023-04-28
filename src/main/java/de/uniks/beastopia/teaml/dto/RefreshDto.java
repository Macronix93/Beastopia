package de.uniks.beastopia.teaml.dto;
import java.util.Objects;
import java.beans.PropertyChangeSupport;

public class RefreshDto
{
   public static final String PROPERTY_REFRESH_TOKEN = "refreshToken";
   private String refreshToken;

   public String getRefreshToken()
   {
      return this.refreshToken;
   }

   public RefreshDto setRefreshToken(String value)
   {
      this.refreshToken = value;
      return this;
   }

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder();
      result.append(' ').append(this.getRefreshToken());
      return result.substring(1);
   }
}
