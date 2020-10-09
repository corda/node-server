package net.corda.explorer.config.mixin;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@JsonSerialize(using = LocalDateTimeSerializer.class)
public abstract class LocalDateTimeMixin { }

class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String format = "dd MMM yyyy hh:ss a";
//        if(format == null || format.trim().length() == 0){
//            format = "dd MMM yyyy hh:ss a";
//        }
        gen.writeObject(value.format(DateTimeFormatter.ofPattern(format)));
    }
}