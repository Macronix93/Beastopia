package de.uniks.beastopia.teaml.modules;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import dagger.Module;
import dagger.Provides;
import de.uniks.beastopia.teaml.utils.*;

import javax.inject.Singleton;
import java.util.Locale;
import java.util.ResourceBundle;

@Module
@SuppressWarnings("unused")
public class MainModule {
    @Provides
    ResourceBundle bundle(Prefs prefs) {
        return ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang", Locale.forLanguageTag(prefs.getLocale()));
    }

    @Provides
    @Singleton
    ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new SimpleModule().addDeserializer(Variant.class, new VariantDeserializer<>()))
                .registerModule(new SimpleModule().addSerializer(new VariantSerializer<>()))
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
