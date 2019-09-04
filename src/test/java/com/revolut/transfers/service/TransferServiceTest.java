package com.revolut.transfers.service;

import com.revolut.transfers.model.*;
import com.revolut.transfers.query.BalanceQuery;
import com.revolut.transfers.repo.AccountRepo;
import com.revolut.transfers.repo.TransactionRepo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;

public final class TransferServiceTest {
    private static final Integer ACCOUNT_1_ID = 1;
    private static final Integer ACCOUNT_2_ID = 2;
    private static final Integer NOT_EXISTING_ACCOUNT_ID = 13;

    @Mock
    private AccountRepo accountRepo;
    @Mock
    private TransactionRepo transactionRepo;
    @Mock
    private BalanceQuery balanceQuery;

    private TransferService transferService;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        transferService = new TransferService(accountRepo, transactionRepo, balanceQuery);

        Mockito.when(accountRepo.getAccount(ACCOUNT_1_ID)).thenReturn(Optional.of(new Account(ACCOUNT_1_ID, "first account")));
        Mockito.when(accountRepo.getAccount(ACCOUNT_2_ID)).thenReturn(Optional.of(new Account(ACCOUNT_2_ID, "second account")));

        Mockito.when(balanceQuery.getAccountBalance(ACCOUNT_1_ID)).thenReturn(BigDecimal.ONE);
    }

    @Test
    public void shouldFailWhenSourceAccountDoesNotExist() {
        assertStatus(TransferStatus.FAILED, NOT_EXISTING_ACCOUNT_ID, ACCOUNT_1_ID, 1);
    }

    @Test
    public void shouldFailWhenTargetAccountDoesNotExist() {
        assertStatus(TransferStatus.FAILED, ACCOUNT_1_ID, NOT_EXISTING_ACCOUNT_ID, 1);
    }

    @Test
    public void shouldFailWhenTransferIsToTheSameAccount() {
        assertStatus(TransferStatus.FAILED, ACCOUNT_1_ID, ACCOUNT_1_ID, 1);
    }

    @Test
    public void shouldReturnNoSufficientFundsWhenNoSufficientFunds() {
        assertStatus(TransferStatus.NO_SUFFICIENT_FUNDS, ACCOUNT_1_ID, ACCOUNT_2_ID, 10);
    }

    @Test
    public void shouldFailOnZeroAmount() {
        assertStatus(TransferStatus.FAILED, ACCOUNT_1_ID, ACCOUNT_2_ID, 0);
    }

    @Test
    public void shouldFailOnNegativeAmount() {
        assertStatus(TransferStatus.FAILED, ACCOUNT_1_ID, ACCOUNT_2_ID, -1);
    }

    @Test
    public void shouldSucceedOnProperDefinedTransfer() {
        final Transfer transfer = createTransfer(ACCOUNT_1_ID, ACCOUNT_2_ID, 1);

        final TransferResult result = transferService.doTransfer(transfer);
        assertEquals(TransferStatus.COMPLETED, result.getStatus());
        assertEquals(transfer.getRequestId(), result.getRequestId());
        assertNull(result.getDetails());
    }

    @Test
    public void onSuccessShouldAddNewTransactionToRepo() {
        final Transfer transfer = createTransfer(ACCOUNT_1_ID, ACCOUNT_2_ID, 1);

        transferService.doTransfer(transfer);

        final ArgumentCaptor<Transaction> argument = ArgumentCaptor.forClass(Transaction.class);
        Mockito.verify(transactionRepo).add(argument.capture());

        final Transaction addedTransaction = argument.getValue();
        assertNotNull(addedTransaction.getId());
        assertEquals(transfer.getSourceAccountId(), addedTransaction.getSourceAccountId().get());
        assertEquals(transfer.getTargetAccountId(), addedTransaction.getTargetAccountId().get());
        assertEquals(transfer.getAmount(), addedTransaction.getAmount());
        assertEquals(transfer.getReference(), addedTransaction.getReference());
    }

    private void assertStatus(TransferStatus expectedStatus, Integer sourceAccountId, Integer targetAccountId, Integer amount) {
        final TransferResult result = transferService.doTransfer(createTransfer(sourceAccountId, targetAccountId, amount));
        assertEquals(expectedStatus, result.getStatus());
    }

    private static Transfer createTransfer(Integer source, Integer target, Integer amount) {
        return new Transfer("r1", source, target, new BigDecimal(amount), null);
    }
}
