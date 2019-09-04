package com.revolut.transfers.restapi;

import com.revolut.transfers.model.Transaction;
import com.revolut.transfers.repo.MemoryMockedData;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TransactionRestApiTest extends RestApiTestBase {
    @Test
    public void shouldReturn404ForNotExistingAccount() {
        assert404(getTransactionResponse(13));
    }

    @Test
    public void shouldReturnTransactionLogForExistingAccount() {
        final Transaction[] result = getTransactionsSuccessfully(MemoryMockedData.ACCOUNT_A_ID);

        assertEquals(2, result.length);
        assertEquals(MemoryMockedData.TRANSACTION_A_1, result[0]);
        assertEquals(MemoryMockedData.TRANSACTION_A_2, result[1]);
    }
}
