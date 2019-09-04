package com.revolut.transfers.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.transfers.model.AccountWithBalance;
import com.revolut.transfers.query.AccountQuery;
import com.revolut.transfers.util.ResponseConfigurator;
import spark.Request;
import spark.Response;

import java.util.Optional;

@Singleton
public class AccountController {
    private final AccountQuery accountQuery;
    private final ResponseConfigurator responseConfigurator;

    @Inject
    AccountController(AccountQuery accountQuery, ResponseConfigurator responseConfigurator) {
        this.accountQuery = accountQuery;
        this.responseConfigurator = responseConfigurator;
    }

    public String getAll(Request req, Response res) {
        return responseConfigurator.success(res, accountQuery.getAll());
    }

    public String getById(Request req, Response res) {
        try {
            final Integer accountId = Integer.valueOf(req.params("id"));
            final Optional<AccountWithBalance> account = accountQuery.getWithBalance(accountId);
            if (account.isEmpty()) {
                return responseConfigurator.notFound(res);
            } else {
                return responseConfigurator.success(res, account.get());
            }
        } catch (NumberFormatException ex) {
            return responseConfigurator.badRequest(res);
        }
    }
}