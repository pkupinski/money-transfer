package com.revolut.transfers;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.revolut.transfers.module.MoneyTransfersModule;
import com.revolut.transfers.rest.RestServer;

public final class MoneyTransfersApp {

    private final RestServer restServer;

    public MoneyTransfersApp() {
        restServer = initializeServerInstance();
    }

    public void start() {
        restServer.start();
    }

    public void stop() {
        restServer.stop();
    }

    private RestServer initializeServerInstance() {
        final Injector injector = Guice.createInjector(new MoneyTransfersModule());
        return injector.getInstance(RestServer.class);
    }
}
