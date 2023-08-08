package de.uniks.beastopia.teaml.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.beastopia.teaml.rest.Event;
import io.reactivex.rxjava3.core.ObservableEmitter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.function.Consumer;
import java.util.regex.Pattern;

@Singleton
public class PatternHandlerProvider {

    @Inject
    public PatternHandlerProvider() {

    }

    public <T> Consumer<String> createPatternHandler(ObjectMapper mapper, String pattern, Class<T> type, ObservableEmitter<Event<T>> emitter) {
        // regions.*.created -> regions\.[^.]*\.created
        final Pattern regex = Pattern.compile(pattern.replace(".", "\\.").replace("*", "[^.]*"));
        return eventStr -> {
            try {
                final JsonNode node = mapper.readTree(eventStr);
                final String event = node.get("event").asText();
                if (!regex.matcher(event).matches()) {
                    return;
                }

                final T data = mapper.treeToValue(node.get("data"), type);
                emitter.onNext(new Event<>(event, data));
            } catch (Exception e) {
                emitter.onError(e);
            }
        };
    }
}
