package com.revolut.transfers.repo;

import com.google.inject.Singleton;
import com.revolut.transfers.model.Transaction;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
class MemoryTransactionRepo implements TransactionRepo {
    private static final List<Transaction> TRANSACTIONS = new LinkedList<>(Arrays.asList(MemoryMockedData.ALL_TRANSACTIONS));

    @Override
    public void add(Transaction transaction) {
        TRANSACTIONS.add(transaction);
    }

    @Override
    public List<Transaction> getByAccount(Integer accountId) {
        return TRANSACTIONS.stream()
                .filter(t -> t.isIncomingForAccount(accountId) || t.isOutgoingForAccount(accountId))
                .collect(Collectors.toList());
    }
}
