package com.abc;

/**
 * Transaction with an amount on a given date
 */
public final class Transaction  extends ModelsBase implements IModelBase{
    private final double amount;
    private final TransactionType transationType;

    @Override
    public String toString() {
        return String.format(transationType.getDetailedDescription(),amount);
    }

    /**
     * Transaction constructor
     * @param amount
     * @param transactionType
     */
    public Transaction(double amount, TransactionType transactionType) {
        this.amount = amount;
        this.transationType = transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public TransactionType getTransationType() {
        return transationType;
    }
}
