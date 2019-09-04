package com.revolut.transfers.repo;

import com.google.inject.ImplementedBy;
import com.revolut.transfers.model.Transaction;

import java.util.List;

@ImplementedBy(MemoryTransactionRepo.class)
public interface TransactionRepo {
    void add(Transaction transaction);

    List<Transaction> getByAccount(Integer accountId);
}
