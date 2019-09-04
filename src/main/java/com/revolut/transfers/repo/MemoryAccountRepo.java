package com.revolut.transfers.repo;

import com.google.inject.Singleton;
import com.revolut.transfers.model.Account;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
class MemoryAccountRepo implements AccountRepo {
    private static final Map<Integer, Account> ACCOUNTS = Stream.of(MemoryMockedData.ALL_ACCOUNTS)
            .collect(Collectors.toMap(Account::getId, a -> a));

    @Override
    public Optional<Account> getAccount(Integer accountId) {
        return Optional.ofNullable(ACCOUNTS.get(accountId));
    }

    @Override
    public Collection<Account> getAll() {
        return ACCOUNTS.values();
    }
}
