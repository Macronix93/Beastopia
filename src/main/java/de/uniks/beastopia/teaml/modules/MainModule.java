package de.uniks.beastopia.teaml.modules;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.Module;
import dagger.Provides;
import de.uniks.beastopia.teaml.utils.ThemeSettings;

import javax.inject.Singleton;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

@Module
public class MainModule {

    @Provides
    ResourceBundle bundle(Preferences preferences) {
        String userLocale = Locale.getDefault().toLanguageTag();
        if (!userLocale.equals("en-EN") && !userLocale.equals("de-DE")) {
            userLocale = "en-EN";
        }
        final String locale = preferences.get("locale", userLocale);
        return ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang", Locale.forLanguageTag(locale));
    }

    @Provides
    @Singleton
    ObjectMapper objectMapper() {
        return new ObjectMapper()
                .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .setSerializationInclusion(JsonInclude.Include.NON_ABSENT);
    }

    @Provides
    @Singleton
    ThemeSettings themeSettings() {
        return new ThemeSettings();
    }
}
