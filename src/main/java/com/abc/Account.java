package com.abc;

import java.util.*;

/**
 * Customer's Bank Account
 */
public class Account extends ModelsBase implements IModelBase{
    private final AccountType accountType;
    /**
     * Key is the id and value is the transactions in a day
     */
    private Map<Long,TransactionsInADay> dailyTransactions;
    private final Date openingDate;

    public Account(AccountType accountType) {
        this.accountType = accountType;
        this.dailyTransactions = new HashMap<Long,TransactionsInADay>();
        this.openingDate = DateProvider.getInstance().now();
    }

    public List<TransactionsInADay> getTransactions() {
        return Collections.unmodifiableList(new ArrayList<TransactionsInADay>(this.dailyTransactions.values()));
    }

    public Date getOpeningDate() {
        return openingDate;
    }

    public AccountType getAccountType() {
        return this.accountType;
    }

    /**
     * Make a deposit
     * @param amount
     */
    public boolean deposit(double amount){
        if (amount > 0.0){
            return getTodaysTransaction().deposit(amount);
        }
        return false;
    }

    /**
     * Withdraw some money
     * @param amount
     * @return
     */
    public boolean withdraw(double amount){
        if (amount > 0.0 && this.getBalanceInfo().getBalance() >= amount){
            return getTodaysTransaction().withdraw(amount);
        }
        return false;
    }

    /**
     * Get account balance info
     * @return
     */
    public BalanceInfo getBalanceInfo(){
        return getAccountUtils().getInterestEarned(this.openingDate,this.getTransactions(),this.accountType);
    }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(Constants.NEW_LINE);
        result.append(accountType.getDetailedDescription());
        result.append(Constants.NEW_LINE);
        result.append(Constants.OPENING_DATE);
        result.append(Constants.TAB);
        result.append(DateProvider.getInstance().getDateString(this.getOpeningDate()));
        return result.toString();
    }

    private TransactionsInADay getTodaysTransaction(){
        TransactionsInADay transactionsInADay = getAccountUtils().getTodaysDailyTransaction(this.getTransactions());
        if (!this.dailyTransactions.containsKey(transactionsInADay.getId())){
            this.dailyTransactions.put(transactionsInADay.getId(),transactionsInADay);
        }
        return transactionsInADay;
    }

    private AccountUtils getAccountUtils() {
        return AccountUtils.getInstance();
    }
}
