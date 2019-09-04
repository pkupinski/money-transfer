package com.revolut.transfers.restapi;

import com.revolut.transfers.model.Account;
import com.revolut.transfers.model.AccountWithBalance;
import com.revolut.transfers.rest.RestRouter;
import org.junit.Test;

import java.math.BigDecimal;

import static com.revolut.transfers.repo.MemoryMockedData.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public final class AccountRestApiTest extends RestApiTestBase {

    @Test
    public void shouldReturn404ForNotExistingAccount() {
        assert404(getAccountResponse(13));
    }

    @Test
    public void shouldReturnAccountWithBalanceForExistingAccount() {
        final AccountWithBalance account = getAccountSuccessfully(ACCOUNT_A_ID);
        assertEquals(new AccountWithBalance(ACCOUNT_A, new BigDecimal(A_INIT_BALANCE)), account);
    }

    @Test
    public void shouldReturnAllAccounts() {
        final Account[] allAccounts = givenApiSpec()
                .when()
                .get(RestRouter.API_PREFIX_AND_VERSION + "/accounts")
                .then()
                .extract()
                .as(AccountWithBalance[].class);

        assertArrayEquals(new Account[]{ACCOUNT_A, ACCOUNT_B, ACCOUNT_C, ACCOUNT_D}, allAccounts);
    }
}
