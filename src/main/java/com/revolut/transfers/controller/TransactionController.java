package com.revolut.transfers.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.transfers.query.AccountQuery;
import com.revolut.transfers.query.TransactionQuery;
import com.revolut.transfers.util.ResponseConfigurator;
import spark.Request;
import spark.Response;

@Singleton
public class TransactionController {
    private final TransactionQuery transactionQuery;
    private final AccountQuery accountQuery;
    private final ResponseConfigurator responseConfigurator;

    @Inject
    public TransactionController(TransactionQuery transactionQuery, AccountQuery accountQuery, ResponseConfigurator responseConfigurator) {
        this.transactionQuery = transactionQuery;
        this.accountQuery = accountQuery;
        this.responseConfigurator = responseConfigurator;
    }

    public String getByAccount(Request req, Response res) {
        try {
            final Integer accountId = Integer.valueOf(req.queryParams("accountId"));

            if (accountQuery.checkIfExists(accountId)) {
                return responseConfigurator.success(res, transactionQuery.getByAccount(accountId));
            } else {
                return responseConfigurator.notFound(res);
            }
        } catch (NumberFormatException ex) {
            return responseConfigurator.badRequest(res);
        }
    }
}
