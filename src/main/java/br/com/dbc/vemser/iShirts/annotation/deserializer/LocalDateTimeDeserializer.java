package br.com.dbc.vemser.iShirts.annotation.deserializer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.DataFormatException;
public class LocalDateTimeDeserializer extends StdDeserializer<LocalDateTime>{
    private static final long serialVersionUID = 9152770723354619045L;
    public LocalDateTimeDeserializer() { this(null);}
    protected LocalDateTimeDeserializer(Class<LocalDateTime> type) { super(type);}
    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        if (parser.getValueAsString().isEmpty()) {
            return null;
        }
        String data = parser.getValueAsString();
        try{
            LocalDateTime.parse(data, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz"));
        }catch(DateTimeException erro){
            return LocalDateTime.MAX;
        }
        return LocalDateTime.parse(parser.getValueAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz"));
    }

}
