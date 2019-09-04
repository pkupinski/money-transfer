package com.revolut.transfers.query;

import com.revolut.transfers.model.Transaction;
import com.revolut.transfers.repo.TransactionRepo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public final class BalanceQueryTest {
    private static final Integer ACCOUNT_ID = 1;

    @Mock
    private TransactionRepo transactionRepo;

    private BalanceQuery balanceQuery;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        balanceQuery = new BalanceQuery(transactionRepo);
    }

    @Test
    public void shouldReturnZeroBalanceWhenNoTransactions() {
        assertResultBalance(BigDecimal.ZERO);
    }

    @Test
    public void shouldReturnTransactionValueWhenOne() {
        assertResultBalance(BigDecimal.TEN, Transaction.newTopUp(ACCOUNT_ID, BigDecimal.TEN));
    }

    @Test
    public void shouldReturnSumOfTransactionsWhenMultiple() {
        assertResultBalance(BigDecimal.TEN.add(BigDecimal.ONE),
                Transaction.newTopUp(1, BigDecimal.TEN),
                Transaction.newTransfer(2, 1, BigDecimal.ONE, null));
    }

    @Test
    public void shouldSubtractOutgoingTransactionsFromAmount() {
        assertResultBalance(BigDecimal.TEN.subtract(BigDecimal.ONE),
                Transaction.newTopUp(1, BigDecimal.TEN),
                Transaction.newTransfer(1, 2, BigDecimal.ONE, null));
    }

    private void assertResultBalance(BigDecimal expectedBalance, Transaction... sourceTransactions) {
        Mockito.when(transactionRepo.getByAccount(ACCOUNT_ID)).thenReturn(Arrays.asList(sourceTransactions));

        final BigDecimal balance = balanceQuery.getAccountBalance(ACCOUNT_ID);

        assertEquals(expectedBalance, balance);
    }
}
