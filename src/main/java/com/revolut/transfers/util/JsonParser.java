package com.revolut.transfers.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Singleton
public class JsonParser {
    private static final Logger LOG = LoggerFactory.getLogger(JsonParser.class);
    private final Gson gson = new Gson();

    public <T> Optional<T> toPojo(String jsonString, Class<T> classType) {
        try {
            return Optional.of(gson.fromJson(jsonString, classType));
        } catch (JsonSyntaxException ex) {
            LOG.error("Failed to parse JSON string", ex);
            return Optional.empty();
        }
    }

    public String toJson(Object data) {
        return gson.toJson(data);
    }
}
