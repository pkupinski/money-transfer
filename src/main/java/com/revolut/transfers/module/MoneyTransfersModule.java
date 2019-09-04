package com.revolut.transfers.module;

import com.google.inject.AbstractModule;
import com.revolut.transfers.rest.RestServer;

public class MoneyTransfersModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(RestServer.class);
    }
}
