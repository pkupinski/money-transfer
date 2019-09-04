package com.revolut.transfers.query;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.transfers.model.Transaction;
import com.revolut.transfers.repo.TransactionRepo;

import java.util.List;

@Singleton
public class TransactionQuery {
    private final TransactionRepo transactionRepo;

    @Inject
    TransactionQuery(TransactionRepo transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    public List<Transaction> getByAccount(Integer accountId) {
        return transactionRepo.getByAccount(accountId);
    }
}
