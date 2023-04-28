package de.uniks.beastopia.teaml;

import dagger.BindsInstance;
import dagger.Component;

import javax.inject.Singleton;

@Component(modules = { MainModule.class, HttpModule.class, PrefModule.class })
@Singleton
public interface MainComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder mainApp(App app);

        MainComponent build();
    }
}

//in App final MainComponent component = DaggerMainComponent.builder().mainApp(this).build();
//in Controller @Inject und public ...controller() {}
