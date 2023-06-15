package de.uniks.beastopia.teaml;

import dagger.Component;

import javax.inject.Singleton;

@Component(modules = {TestModule.class})
@Singleton
public interface TestComponent extends MainComponent {
    @Component.Builder
    interface Builder extends MainComponent.Builder {
        @Override
        TestComponent build();
    }
}
