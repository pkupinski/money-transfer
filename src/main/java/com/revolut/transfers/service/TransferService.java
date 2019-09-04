package com.revolut.transfers.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.transfers.model.Account;
import com.revolut.transfers.model.Transaction;
import com.revolut.transfers.model.Transfer;
import com.revolut.transfers.model.TransferResult;
import com.revolut.transfers.query.BalanceQuery;
import com.revolut.transfers.repo.AccountRepo;
import com.revolut.transfers.repo.TransactionRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Handles all transfers requested by the system.
 */
@Singleton
public class TransferService {
    private final static Logger LOG = LoggerFactory.getLogger(TransferService.class);

    private final AccountLocker accountLocker = new AccountLocker();
    private final AccountRepo accountRepo;
    private final TransactionRepo transactionRepo;
    private final BalanceQuery balanceQuery;

    @Inject
    TransferService(AccountRepo accountRepo, TransactionRepo transactionRepo, BalanceQuery balanceQuery) {
        this.accountRepo = accountRepo;
        this.transactionRepo = transactionRepo;
        this.balanceQuery = balanceQuery;
    }

    public TransferResult doTransfer(Transfer transfer) {
        LOG.info(String.format("Beginning transaction for request id: %s", transfer.getRequestId()));

        final Optional<Account> sourceAccount = accountRepo.getAccount(transfer.getSourceAccountId());
        final Optional<Account> targetAccount = accountRepo.getAccount(transfer.getTargetAccountId());

        // check account existence
        if (sourceAccount.isEmpty())
            return TransferResult.failed(transfer.getRequestId(), "Source account doesn't exist");
        if (targetAccount.isEmpty())
            return TransferResult.failed(transfer.getRequestId(), "Target account doesn't exist");
        // check same account transfer
        if (transfer.getSourceAccountId().equals(transfer.getTargetAccountId()))
            return TransferResult.failed(transfer.getRequestId(), "Cannot transfer money to the same account");
        // check if amount is larger than 0
        if (transfer.getAmount().signum() <= 0)
            return TransferResult.failed(transfer.getRequestId(), "Cannot transfer amount smaller or equal to 0");

        try {
            accountLocker.lock(transfer.getSourceAccountId(), transfer.getTargetAccountId());

            final BigDecimal sourceBalance = balanceQuery.getAccountBalance(sourceAccount.get().getId());
            if (sourceBalance.subtract(transfer.getAmount()).signum() < 0) {
                // no sufficient funds found
                return TransferResult.notSufficientFunds(transfer.getRequestId());
            } else {
                transactionRepo.add(Transaction.newTransfer(sourceAccount.get().getId(), targetAccount.get().getId(), transfer.getAmount(), transfer.getReference()));
                return TransferResult.success(transfer.getRequestId());
            }
        } finally {
            // release locks even there will be unexpected exception
            accountLocker.release(transfer.getSourceAccountId(), transfer.getTargetAccountId());
            LOG.info(String.format("Finished processing transaction for request id: %s with result", transfer.getRequestId()));
        }
    }
}
