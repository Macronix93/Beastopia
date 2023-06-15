package de.uniks.beastopia.teaml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.Module;
import dagger.Provides;
import de.uniks.beastopia.teaml.rest.*;
import de.uniks.beastopia.teaml.utils.ThemeSettings;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import static org.mockito.Mockito.mock;

@SuppressWarnings({"SameReturnValue", "unused"})
@Module
public class TestModule {

    @Provides
    static Preferences prefs() {
        return mock(Preferences.class);
    }

    @Provides
    static ResourceBundle resourceBundle() {
        return ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang", Locale.forLanguageTag("en"));
    }

    @Provides
    ObjectMapper mapper() {
        return new ObjectMapper()
                .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .setSerializationInclusion(JsonInclude.Include.NON_ABSENT);
    }

    @Provides
    @SuppressWarnings("unused")
    UserApiService user() {
        return null;
    }

    @Provides
    @SuppressWarnings("unused")
    AuthApiService auth() {
        return null;
    }

    @Provides
    @SuppressWarnings("unused")
    GroupApiService group() {
        return null;
    }

    @Provides
    @SuppressWarnings("unused")
    MessageApiService message() {
        return null;
    }

    @Provides
    @SuppressWarnings("unused")
    RegionApiService region() {
        return null;
    }

    @Provides
    @SuppressWarnings("unused")
    AreaApiService area() {
        return null;
    }

    @Provides
    @SuppressWarnings("unused")
    PresetsApiService presets() {
        return null;
    }

    @Provides
    @SuppressWarnings("unused")
    TrainerApiService trainer() {
        return null;
    }

    @Provides
    @SuppressWarnings("unused")
    AchievementsApiService achievements() {
        return null;
    }

    @Provides
    @SuppressWarnings("unused")
    ThemeSettings themeSettings() {
        return new ThemeSettings();
    }
}
