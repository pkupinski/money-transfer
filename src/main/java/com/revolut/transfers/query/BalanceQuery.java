package com.revolut.transfers.query;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.transfers.model.Transaction;
import com.revolut.transfers.repo.TransactionRepo;

import java.math.BigDecimal;
import java.util.List;

@Singleton
public class BalanceQuery {
    private final TransactionRepo transactionRepo;

    @Inject
    public BalanceQuery(TransactionRepo transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    public BigDecimal getAccountBalance(Integer accountId) {
        final List<Transaction> accountTransfers = transactionRepo.getByAccount(accountId);
        return accountTransfers.stream()
                .map(t -> transactionToAmount(t, accountId))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal transactionToAmount(Transaction transaction, Integer accountId) {
        if (transaction.isOutgoingForAccount(accountId)) {
            return transaction.getAmount().negate();
        } else if (transaction.isIncomingForAccount(accountId)) {
            return transaction.getAmount();
        } else {
            throw new IllegalArgumentException("Transaction not related to account"); // this shouldn't happen
        }
    }
}
