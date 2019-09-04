package com.revolut.transfers.repo;

import com.revolut.transfers.model.Account;
import com.revolut.transfers.model.Transaction;

import java.math.BigDecimal;

/**
 * Contains mocked accounts (and it's initial transactions) provided by memory based mock repositories.
 */
public final class MemoryMockedData {
    public static final Integer ACCOUNT_A_ID = 1;
    public static final Integer ACCOUNT_B_ID = 2;
    public static final Integer ACCOUNT_C_ID = 3;
    public static final Integer ACCOUNT_D_ID = 4;

    public static final Integer A_INIT_BALANCE = 100000;
    public static final Integer B_INIT_BALANCE = 1000;
    public static final Integer C_INIT_BALANCE = 10;
    public static final Integer D_INIT_BALANCE = 1;

    public static final Account ACCOUNT_A = new Account(ACCOUNT_A_ID, "a-100000");
    public static final Account ACCOUNT_B = new Account(ACCOUNT_B_ID, "b-1000");
    public static final Account ACCOUNT_C = new Account(ACCOUNT_C_ID, "c-10");
    public static final Account ACCOUNT_D = new Account(ACCOUNT_D_ID, "d-1");

    public static final Account[] ALL_ACCOUNTS = new Account[]{ACCOUNT_A, ACCOUNT_B, ACCOUNT_C, ACCOUNT_D};

    public static final Transaction TRANSACTION_A_1 = Transaction.newTopUp(ACCOUNT_A_ID, new BigDecimal(A_INIT_BALANCE - 100));
    public static final Transaction TRANSACTION_A_2 = Transaction.newTopUp(ACCOUNT_A_ID, new BigDecimal(100));
    public static final Transaction TRANSACTION_B = Transaction.newTopUp(ACCOUNT_B_ID, new BigDecimal(B_INIT_BALANCE));
    public static final Transaction TRANSACTION_C = Transaction.newTopUp(ACCOUNT_C_ID, new BigDecimal(C_INIT_BALANCE));
    public static final Transaction TRANSACTION_D = Transaction.newTopUp(ACCOUNT_D_ID, new BigDecimal(D_INIT_BALANCE));

    public static final Transaction[] ALL_TRANSACTIONS = new Transaction[]{TRANSACTION_A_1, TRANSACTION_A_2, TRANSACTION_B, TRANSACTION_C, TRANSACTION_D};

    private MemoryMockedData() {
    }
}
