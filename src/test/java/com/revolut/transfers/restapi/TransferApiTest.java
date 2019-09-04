package com.revolut.transfers.restapi;

import com.revolut.transfers.model.*;
import com.revolut.transfers.rest.RestRouter;
import org.junit.Test;

import java.math.BigDecimal;

import static com.revolut.transfers.repo.MemoryMockedData.*;
import static org.junit.Assert.assertEquals;

public final class TransferApiTest extends RestApiTestBase {
    private static final Transfer MISSING_SOURCE_ACCOUNT_TRANSFER = new Transfer("req1", 13, ACCOUNT_B_ID, new BigDecimal(1000), "ref");
    private static final Transfer MISSING_TARGET_ACCOUNT_TRANSFER = new Transfer("req1", ACCOUNT_A_ID, 13, new BigDecimal(1000), "ref");
    private static final Transfer NOT_SUFFICIENT_FUNDS_TRANSFER = new Transfer("req1", ACCOUNT_A_ID, ACCOUNT_B_ID, new BigDecimal(1000000), "ref");

    @Test
    public void shouldReturnFailureWhenOneOfTheAccountsIsMissing() {
        testFailingCase(MISSING_SOURCE_ACCOUNT_TRANSFER);
        testFailingCase(MISSING_TARGET_ACCOUNT_TRANSFER);
    }

    @Test
    public void shouldReturnNoSufficientFundsIfNotEnoughMoneyOnSourceAccount() {
        assertEquals(TransferStatus.NO_SUFFICIENT_FUNDS, postTransferSuccessfully(NOT_SUFFICIENT_FUNDS_TRANSFER).getStatus());
    }

    @Test
    public void shouldNotChangeAccountBalanceWhenTransferWasNotCompleted() {
        final BigDecimal initialBalanceA = getAccountSuccessfully(ACCOUNT_A_ID).getBalance();
        final BigDecimal initialBalanceB = getAccountSuccessfully(ACCOUNT_B_ID).getBalance();

        postTransferSuccessfully(MISSING_TARGET_ACCOUNT_TRANSFER);
        postTransferSuccessfully(MISSING_TARGET_ACCOUNT_TRANSFER);
        postTransferSuccessfully(NOT_SUFFICIENT_FUNDS_TRANSFER);

        final BigDecimal balanceA = getAccountSuccessfully(ACCOUNT_A_ID).getBalance();
        final BigDecimal balanceB = getAccountSuccessfully(ACCOUNT_B_ID).getBalance();

        assertEquals(initialBalanceA, balanceA);
        assertEquals(initialBalanceB, balanceB);
    }

    @Test
    public void shouldCompleteTransferAddingTransactionAndChangingAccountBalances() {
        final Transfer transfer = new Transfer("req1", ACCOUNT_A_ID, ACCOUNT_B_ID, new BigDecimal(1000), "ref");

        // do transfer and check result
        final TransferResult transferResult = postTransferSuccessfully(transfer);
        assertEquals(transferResult, TransferResult.success(transfer.getRequestId()));

        // check account A balance
        final AccountWithBalance accountA = getAccountSuccessfully(ACCOUNT_A_ID);
        assertEquals(BigDecimal.valueOf(A_INIT_BALANCE).subtract(transfer.getAmount()), accountA.getBalance());

        // check account B balance
        final AccountWithBalance accountB = getAccountSuccessfully(ACCOUNT_B_ID);
        assertEquals(BigDecimal.valueOf(B_INIT_BALANCE).add(transfer.getAmount()), accountB.getBalance());

        // check account A transactions
        final Transaction[] aTransactions = getTransactionsSuccessfully(ACCOUNT_A_ID);
        assertEquals(3, aTransactions.length);
        final Transaction addedATransaction = aTransactions[2];

        // check account B transactions
        final Transaction[] bTransactions = getTransactionsSuccessfully(ACCOUNT_B_ID);
        assertEquals(2, bTransactions.length);
        final Transaction addedBTransaction = bTransactions[1];

        // check both accounts have the same transaction added
        assertEquals(addedATransaction, addedBTransaction);
        assertEquals(transfer.getAmount(), addedATransaction.getAmount());
        assertEquals(transfer.getSourceAccountId(), addedATransaction.getSourceAccountId().get());
        assertEquals(transfer.getTargetAccountId(), addedATransaction.getTargetAccountId().get());
        assertEquals(transfer.getReference(), addedATransaction.getReference());
    }

    @Test
    public void shouldReturn422WhenTransferBodyIsInWrongFormat() {
        givenApiSpec()
                .body("{not a proper transfer}")
                .when()
                .post(RestRouter.API_PREFIX_AND_VERSION + "/transfer")
                .then()
                .assertThat()
                .statusCode(422);
    }

    private void testFailingCase(Transfer transfer) {
        assertEquals(TransferStatus.FAILED, postTransferSuccessfully(transfer).getStatus());
    }
}
