package de.uniks.beastopia.teaml.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class VariantSerializer <T,U> extends StdSerializer<Variant<T,U>> {

    public VariantSerializer() {
        super(Variant.class, true);
    }

    @Override
    public void serialize(Variant<T,U> variant, JsonGenerator generator, SerializerProvider provider) throws IOException {
        if (variant.isT()) {
            generator.writeObject(variant.getT());
        } else if (variant.isU()) {
            generator.writeObject(variant.getU());
        }
    }
}
