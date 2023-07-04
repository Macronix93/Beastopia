package de.uniks.beastopia.teaml.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;

public class VariantDeserializer<T, U> extends StdDeserializer<Variant<T, U>> {

    public VariantDeserializer() {
        super(Variant.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Variant<T, U> deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
        String json = context.readTree(jsonParser).toString();
        try {
            Class<T> tType = (Class<T>)
                    ((ParameterizedType) getClass()
                            .getGenericSuperclass())
                            .getActualTypeArguments()[0];
            T t = new Gson().fromJson(json, tType);
            Variant<T, U> variant = new Variant<>();
            variant.setT(t);
            return variant;
        } catch (JsonSyntaxException e) {
            Class<U> uType = (Class<U>)
                    ((ParameterizedType) getClass()
                            .getGenericSuperclass())
                            .getActualTypeArguments()[0];
            U u = new Gson().fromJson(json, uType);
            Variant<T, U> variant = new Variant<>();
            variant.setU(u);
            return variant;
        }
    }
}
