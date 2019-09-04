package com.revolut.transfers.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents user account with additional information about balance.
 */
public class AccountWithBalance extends Account {
    private final BigDecimal balance;

    public AccountWithBalance(Account account, BigDecimal balance) {
        super(account.getId(), account.getName());
        Objects.requireNonNull(balance);
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AccountWithBalance && Objects.equals(this.balance, ((AccountWithBalance) obj).balance) && super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
