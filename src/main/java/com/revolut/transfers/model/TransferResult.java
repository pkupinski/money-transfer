package com.revolut.transfers.model;

import com.google.common.base.MoreObjects;

import java.util.Objects;

public final class TransferResult {
    private final String requestId;
    private final TransferStatus status;
    private final String details;

    public static TransferResult success(String requestId) {
        return new TransferResult(requestId, TransferStatus.COMPLETED, null);
    }

    public static TransferResult notSufficientFunds(String requestId) {
        return new TransferResult(requestId, TransferStatus.NO_SUFFICIENT_FUNDS, "Not sufficient funds on source account");
    }

    public static TransferResult failed(String requestId, String details) {
        return new TransferResult(requestId, TransferStatus.FAILED, details);
    }

    private TransferResult(String requestId, TransferStatus status, String details) {
        Objects.requireNonNull(requestId);
        Objects.requireNonNull(status);

        this.requestId = requestId;
        this.status = status;
        this.details = details;
    }


    public String getRequestId() {
        return requestId;
    }

    public TransferStatus getStatus() {
        return status;
    }

    public String getDetails() {
        return details;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TransferResult) {
            final TransferResult that = (TransferResult) obj;
            return Objects.equals(this.requestId, that.requestId)
                    && Objects.equals(this.status, that.status)
                    && Objects.equals(this.details, that.details);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId, status, details);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .addValue(requestId)
                .addValue(status)
                .addValue(details)
                .toString();
    }
}
