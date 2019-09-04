package com.revolut.transfers.repo;

import com.google.inject.ImplementedBy;
import com.revolut.transfers.model.Account;

import java.util.Collection;
import java.util.Optional;

@ImplementedBy(MemoryAccountRepo.class)
public interface AccountRepo {
    Optional<Account> getAccount(Integer accountId);

    Collection<Account> getAll();
}
