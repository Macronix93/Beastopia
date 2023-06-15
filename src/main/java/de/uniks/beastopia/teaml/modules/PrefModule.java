package de.uniks.beastopia.teaml.modules;

import dagger.Module;
import dagger.Provides;
import de.uniks.beastopia.teaml.Main;

import javax.inject.Singleton;
import java.util.prefs.Preferences;

@Module
@SuppressWarnings("unused")
public class PrefModule {

    @Provides
    @Singleton
    Preferences prefs() {
        return Preferences.userNodeForPackage(Main.class);
    }


}
