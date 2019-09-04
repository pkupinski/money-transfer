package com.revolut.transfers.model;

import com.google.common.base.Preconditions;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Represents single transaction (transfer or top-up).
 * Top-up transaction have empty source account id.
 */
public class Transaction {
    private final UUID id = UUID.randomUUID();
    private final Integer sourceAccountId;
    private final Integer targetAccountId;
    private final TransactionType type;
    private final BigDecimal amount;
    private final String reference;


    public static Transaction newTransfer(Integer sourceAccountId, Integer targetAccountId, BigDecimal amount, String reference) {
        Preconditions.checkNotNull(targetAccountId);
        return new Transaction(sourceAccountId, targetAccountId, amount, reference);
    }

    public static Transaction newTopUp(Integer targetAccountId, BigDecimal amount) {
        return new Transaction(null, targetAccountId, amount, null);
    }

    private Transaction(Integer sourceAccountId, Integer targetAccountId, BigDecimal amount, String reference) {
        Preconditions.checkArgument(sourceAccountId != null || targetAccountId != null);
        Preconditions.checkNotNull(amount);
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.amount = amount;
        this.reference = reference;
        this.type = sourceAccountId != null ? TransactionType.TRANSFER : TransactionType.TOP_UP;
    }

    public UUID getId() {
        return id;
    }

    public Optional<Integer> getSourceAccountId() {
        return Optional.ofNullable(sourceAccountId);
    }

    public Optional<Integer> getTargetAccountId() {
        return Optional.ofNullable(targetAccountId);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getReference() {
        return reference;
    }

    public TransactionType getType() {
        return type;
    }

    public boolean isOutgoingForAccount(Integer accountId) {
        return getSourceAccountId().isPresent() && getSourceAccountId().get().equals(accountId);
    }

    public boolean isIncomingForAccount(Integer accountId) {
        return getTargetAccountId().isPresent() && getTargetAccountId().get().equals(accountId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Transaction) {
            final Transaction that = (Transaction) obj;
            return Objects.equals(this.id, that.id)
                    && Objects.equals(this.sourceAccountId, that.sourceAccountId)
                    && Objects.equals(this.targetAccountId, that.targetAccountId)
                    && Objects.equals(this.amount, that.amount)
                    && Objects.equals(this.reference, that.reference);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
