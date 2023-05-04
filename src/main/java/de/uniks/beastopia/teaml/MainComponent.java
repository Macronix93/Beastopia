package de.uniks.beastopia.teaml;

import dagger.BindsInstance;
import dagger.Component;
import de.uniks.beastopia.teaml.controller.LoginController;
import de.uniks.beastopia.teaml.controller.*;
import de.uniks.beastopia.teaml.controller.FriendController;
import de.uniks.beastopia.teaml.controller.FriendListController;
import de.uniks.beastopia.teaml.controller.RegistrationController;
import de.uniks.beastopia.teaml.modules.HttpModule;
import de.uniks.beastopia.teaml.modules.MainModule;
import de.uniks.beastopia.teaml.modules.PrefModule;

import javax.inject.Singleton;

@Component(modules = {MainModule.class, HttpModule.class, PrefModule.class})
@Singleton
public interface MainComponent {

    LoginController loginController();

    App app();
    RegistrationController registrationController();

    FriendListController friendListController();

    FriendController friendController();

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder mainApp(App app);

        MainComponent build();
    }
}
