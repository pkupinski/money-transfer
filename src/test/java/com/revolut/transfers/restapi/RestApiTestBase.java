package com.revolut.transfers.restapi;

import com.revolut.transfers.MoneyTransfersApp;
import com.revolut.transfers.model.AccountWithBalance;
import com.revolut.transfers.model.Transaction;
import com.revolut.transfers.model.Transfer;
import com.revolut.transfers.model.TransferResult;
import com.revolut.transfers.rest.RestRouter;
import com.revolut.transfers.rest.RestServer;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.After;
import org.junit.Before;

import static io.restassured.RestAssured.given;

/**
 * Base class for REST API tests. Starts and stops the rest server. Has some helper methods.
 */
public abstract class RestApiTestBase {
    private final MoneyTransfersApp moneyTransfersApp = new MoneyTransfersApp();

    @Before
    public void before() {
        moneyTransfersApp.start();
    }

    @After
    public void stop() {
        moneyTransfersApp.stop();
    }

    static RequestSpecification givenApiSpec() {
        return given()
                .port(RestServer.SERVER_PORT)
                .contentType(ContentType.JSON);
    }

    static Response getAccountResponse(Integer accountId) {
        return givenApiSpec()
                .when()
                .get(RestRouter.API_PREFIX_AND_VERSION + "/accounts/" + accountId);

    }

    static Response getTransactionResponse(Integer accountId) {
        return givenApiSpec()
                .when()
                .get(RestRouter.API_PREFIX_AND_VERSION + "/transactions?accountId=" + accountId);
    }

    static Response getTransferResponse(Transfer transfer) {
        return givenApiSpec()
                .body(transfer)
                .when()
                .post(RestRouter.API_PREFIX_AND_VERSION + "/transfer");

    }

    static AccountWithBalance getAccountSuccessfully(Integer accountId) {
        return extractFromSuccessfulResponse(getAccountResponse(accountId), AccountWithBalance.class);
    }

    static Transaction[] getTransactionsSuccessfully(Integer accountId) {
        return extractFromSuccessfulResponse(getTransactionResponse(accountId), Transaction[].class);
    }

    static TransferResult postTransferSuccessfully(Transfer transfer) {
        return extractFromSuccessfulResponse(getTransferResponse(transfer), TransferResult.class);
    }

    static void assert404(Response response) {
        response
                .then()
                .assertThat()
                .statusCode(404);
    }

    private static <T> T extractFromSuccessfulResponse(Response response, Class<T> resultClass) {
        return response
                .then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .as(resultClass);
    }
}
