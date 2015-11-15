package com.abc;

/**
 * Created by Prithvi on 11/13/15.
 */
public class BalanceInfo {
    private final double balance;
    private final double interestEarned;

    public BalanceInfo(double balance, double interestEarned) {
        this.balance = balance;
        this.interestEarned = interestEarned;
    }

    public double getBalance() {
        return balance;
    }

    public double getInterestEarned() {
        return interestEarned;
    }
}
