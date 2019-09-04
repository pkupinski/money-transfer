package com.revolut.transfers.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.transfers.model.Transfer;
import com.revolut.transfers.service.TransferService;
import com.revolut.transfers.util.JsonParser;
import com.revolut.transfers.util.ResponseConfigurator;
import spark.Request;
import spark.Response;

import java.util.Optional;

@Singleton
public class TransferController {
    private final TransferService transferService;
    private final ResponseConfigurator responseConfigurator;
    private final JsonParser parser;

    @Inject
    public TransferController(TransferService transferService, ResponseConfigurator responseConfigurator, JsonParser parser) {
        this.transferService = transferService;
        this.responseConfigurator = responseConfigurator;
        this.parser = parser;
    }

    public String transfer(Request req, Response res) {
        final Optional<Transfer> transferCommand = parser.toPojo(req.body(), Transfer.class);
        if (transferCommand.isEmpty()) {
            return responseConfigurator.unprocessableEntity(res);
        }

        return responseConfigurator.success(res, transferService.doTransfer(transferCommand.get()));
    }
}
