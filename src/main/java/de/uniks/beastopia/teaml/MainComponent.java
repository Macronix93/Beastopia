package de.uniks.beastopia.teaml;

import dagger.BindsInstance;
import dagger.Component;
import de.uniks.beastopia.teaml.controller.auth.LoginController;
import de.uniks.beastopia.teaml.modules.HttpModule;
import de.uniks.beastopia.teaml.modules.MainModule;
import de.uniks.beastopia.teaml.modules.PrefModule;
import de.uniks.beastopia.teaml.service.AuthService;
import de.uniks.beastopia.teaml.utils.Prefs;
import de.uniks.beastopia.teaml.utils.ThemeSettings;

import javax.inject.Singleton;

@Component(modules = {MainModule.class, HttpModule.class, PrefModule.class})
@Singleton
public interface MainComponent {
    AuthService authService();

    LoginController loginController();

    Prefs prefs();

    ThemeSettings themeSettings();

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder mainApp(App app);

        MainComponent build();
    }
}
