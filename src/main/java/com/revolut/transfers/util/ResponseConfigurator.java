package com.revolut.transfers.util;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import spark.Response;

@Singleton
public class ResponseConfigurator {
    private final JsonParser parser;

    @Inject
    ResponseConfigurator(JsonParser parser) {
        this.parser = parser;
    }

    public String success(Response response, Object bodyObject) {
        Preconditions.checkNotNull(response);
        Preconditions.checkNotNull(bodyObject);

        response.type("application/json");
        response.status(200);

        return parser.toJson(bodyObject);
    }

    public String notFound(Response response) {
        return errorStatus(response, 404);
    }

    public String badRequest(Response response) {
        return errorStatus(response, 400);
    }

    public String unprocessableEntity(Response response) {
        return errorStatus(response, 422);
    }

    private static String errorStatus(Response response, int statusCode) {
        response.status(statusCode);
        return String.valueOf(statusCode);
    }
}
