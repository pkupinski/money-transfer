package com.revolut.transfers.query;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.transfers.model.Account;
import com.revolut.transfers.model.AccountWithBalance;
import com.revolut.transfers.repo.AccountRepo;

import java.util.Collection;
import java.util.Optional;

@Singleton
public class AccountQuery {
    private final AccountRepo accountRepo;
    private final BalanceQuery balanceQuery;

    @Inject
    public AccountQuery(AccountRepo accountRepo, BalanceQuery balanceQuery) {
        this.accountRepo = accountRepo;
        this.balanceQuery = balanceQuery;
    }

    public boolean checkIfExists(Integer accountId) {
        return accountRepo.getAccount(accountId).isPresent();
    }

    public Optional<AccountWithBalance> getWithBalance(Integer accountId) {
        return accountRepo.getAccount(accountId).map(a -> new AccountWithBalance(a, balanceQuery.getAccountBalance(accountId)));
    }

    public Collection<Account> getAll() {
        return accountRepo.getAll();
    }
}
