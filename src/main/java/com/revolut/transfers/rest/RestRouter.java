package com.revolut.transfers.rest;

import com.google.inject.Inject;
import com.revolut.transfers.controller.AccountController;
import com.revolut.transfers.controller.TransactionController;
import com.revolut.transfers.controller.TransferController;

import static spark.Spark.*;

public class RestRouter {
    private static final String API_PREFIX = "api";
    private static final String API_VERSION = "1.0";
    public static final String API_PREFIX_AND_VERSION = String.format("/%s/%s", API_PREFIX, API_VERSION);

    private final AccountController accountController;
    private final TransferController transferController;
    private final TransactionController transactionController;

    @Inject
    private RestRouter(AccountController accountController, TransferController transferController, TransactionController transactionController) {
        this.accountController = accountController;
        this.transferController = transferController;
        this.transactionController = transactionController;
    }

    void configureRoutes() {
        path(API_PREFIX_AND_VERSION, () -> {
            accountsRoutes();
            transfersRoutes();
            transactionsRoutes();
        });
    }

    private void accountsRoutes() {
        path("/accounts", () -> {
            get("", accountController::getAll);
            get("/:id", accountController::getById);
        });
    }

    private void transfersRoutes() {
        post("/transfer", transferController::transfer);
    }

    private void transactionsRoutes() {
        get("/transactions", transactionController::getByAccount);
    }
}
