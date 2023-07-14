package de.uniks.beastopia.teaml.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class VariantDeserializer<T, U> extends StdDeserializer<Variant<T, U>> {

    public VariantDeserializer() {
        super(Variant.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Variant<T, U> deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
        String json = context.readTree(jsonParser).toString();

        Type superClass = getClass().getGenericSuperclass();

        if (superClass instanceof ParameterizedType parameterizedType) {
            Type[] typeArguments = parameterizedType.getActualTypeArguments();

            if (typeArguments.length == 2) {
                Type tType = typeArguments[0];
                Type uType = typeArguments[1];

                Variant<T, U> variant = new Variant<>();

                try {
                    if (tType instanceof Class) {
                        Class<T> tClass = (Class<T>) tType;
                        T t = new Gson().fromJson(json, tClass);
                        variant.setT(t);
                    }
                } catch (JsonSyntaxException e) {
                    if (uType instanceof Class) {
                        Class<U> uClass = (Class<U>) uType;
                        U u = new Gson().fromJson(json, uClass);
                        variant.setU(u);
                    }
                }

                return variant;
            }
        }

        return null;
    }
}
