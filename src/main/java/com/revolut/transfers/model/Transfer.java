package com.revolut.transfers.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents request to do a transfer between accounts.
 */
public class Transfer {
    private final String requestId;
    private final Integer sourceAccountId;
    private final Integer targetAccountId;
    private final BigDecimal amount;
    private final String reference;

    public Transfer(String requestId, Integer sourceAccountId, Integer targetAccountId, BigDecimal amount, String reference) {
        Objects.requireNonNull(requestId);
        Objects.requireNonNull(sourceAccountId);
        Objects.requireNonNull(targetAccountId);
        Objects.requireNonNull(amount);

        this.requestId = requestId;
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.amount = amount;
        this.reference = reference;
    }

    public String getRequestId() {
        return requestId;
    }

    public Integer getSourceAccountId() {
        return sourceAccountId;
    }

    public Integer getTargetAccountId() {
        return targetAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getReference() {
        return reference;
    }
}
