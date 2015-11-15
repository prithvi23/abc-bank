package com.abc;

import com.sun.tools.internal.jxc.ap.Const;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Prithvi on 11/14/15.
 */
public final class AccountUtils {
    private static AccountUtils accountUtils = new AccountUtils();

    public static AccountUtils getInstance(){
        return accountUtils;
    }


    public final double getAccruedInterest(double dailyBalance, double todaysInterestRate){
        return dailyBalance*todaysInterestRate/100;
    }

    /**
     * Itereate from the end till the transactions of last 10 days are found
     * @param currentTransactionsInADay
     * @param previousTransactionsInADay
     * @return
     */
    public boolean isAnyWithdrawlsMadeInLastTenDays(TransactionsInADay currentTransactionsInADay, List<TransactionsInADay> previousTransactionsInADay){
        for(int i = previousTransactionsInADay.size()-1; i > 0; i--){
            TransactionsInADay transaction = previousTransactionsInADay.get(i);
            // Check if the previous transaction is within 10 days
            if (getDateProvider().getDiffInDays(currentTransactionsInADay.getDate(), transaction.getDate()) <= 10 ){
                if (transaction.isWithdrawlMadeToday()){
                    return true;
                }
            }else{
                // If the previous transaction happened before 10 days break out of the loop
                break;
            }
        }
        return false;
    }


    /**
     * Calculates interest
     * @param accruedDailyInterest
     * @param currentAccountBalance
     * @param interestRate
     * @return
     */
    public double getAccruedDailyInterestFlatRate(double accruedDailyInterest, double currentAccountBalance, double interestRate) {
        return accruedDailyInterest + getAccruedInterest(currentAccountBalance, interestRate);
    }


    /**
     * Get today's daily transaction from a list
     * @param dailyTransactions
     * @return
     */
    public TransactionsInADay getTodaysDailyTransaction(List<TransactionsInADay> dailyTransactions){
        if (dailyTransactions.size() == 1){
            if (isTodaysTransationList(dailyTransactions.get(0))){
                return dailyTransactions.get(0);
            }
        }else{
            for(int i = dailyTransactions.size() -1; i > 0; i--){
                TransactionsInADay dailyTransaction = dailyTransactions.get(i);
                if (isTodaysTransationList(dailyTransaction)){
                    return dailyTransaction;
                }
            }
        }
        return new TransactionsInADay();
    }

    /**
     * Accrued interest formula: https://en.wikipedia.org/wiki/Accrued_interest
     * I = T * Principal * APR
     * T = Days in Period /Days in Year
     * @return
     */
    public BalanceInfo getInterestEarned(Date accountOpeningDate,List<TransactionsInADay> dailyTransactions, AccountType accountType) {
        if (dailyTransactions.size() == 0){
            return new BalanceInfo(0,0);
        }
        double accruedDailyInterest = 0.0;
        double currentAccountBalance = 0.0;
        for(int i = 0; i < dailyTransactions.size(); i++){
            TransactionsInADay transactionsInADay = dailyTransactions.get(i);
            currentAccountBalance = currentAccountBalance + transactionsInADay.getTodaysBalance() + accruedDailyInterest;
            switch (accountType){
                case CHECKING:
                    // Flat rate of 0.1% for checking account
                    accruedDailyInterest = getAccruedDailyInterestFlatRate(accruedDailyInterest, currentAccountBalance, 0.1);
                    break;
                case SAVINGS:
                    if (currentAccountBalance <= 1000){
                        // Flat rate of 0.1% for savings account for first 1000
                        accruedDailyInterest = getAccruedDailyInterestFlatRate(accruedDailyInterest, currentAccountBalance, 0.1);
                    }else{
                        // Flat rate of 0.1% for savings account for first 1000
                        accruedDailyInterest = getAccruedDailyInterestFlatRate(accruedDailyInterest, 1000, 0.1);
                        // 0.2 % from then on
                        accruedDailyInterest = getAccruedDailyInterestFlatRate(accruedDailyInterest, currentAccountBalance-1000, 0.2);
                    }
                    break;
                case MAXI_SAVINGS:
                    // Need to check if there are any withdrawls made in the last 10 days
                    List<TransactionsInADay> previousTransactionsInADay = i > 0 ? dailyTransactions.subList(0,i-1) : new ArrayList<TransactionsInADay>();
                    if (AccountUtils.getInstance().isAnyWithdrawlsMadeInLastTenDays(transactionsInADay,previousTransactionsInADay)){
                        // If withdrawls are made then it is 0.1%
                        accruedDailyInterest = getAccruedDailyInterestFlatRate(accruedDailyInterest, currentAccountBalance, 0.1);
                    }else{
                        // else it is 5%
                        accruedDailyInterest = getAccruedDailyInterestFlatRate(accruedDailyInterest, currentAccountBalance, 5.0);
                    }
                    break;
            }
        }
        return new BalanceInfo(currentAccountBalance,accruedDailyInterest);
    }

    /**
     * Returns the account statement
     * @param account
     * @return
     */
    public String getAccountStatement( Account account){
        StringBuilder result = new StringBuilder();
        result.append(Constants.NEW_LINE);
        result.append(account.toString());
        result.append(Constants.NEW_LINE);
        for(TransactionsInADay transactionsInADay : account.getTransactions()){
            result.append(transactionsInADay.toString());
        }
        result.append(Constants.NEW_LINE);
        result.append(String.format(Constants.TOTAL,account.getBalanceInfo().getBalance()));
        return result.toString();
    }

    /**
     * Returns customer name and the total number of accounts
     * @param bank
     * @return
     */
    public String getCustomerAccountReport(Bank bank){
        StringBuilder result = new StringBuilder(Constants.CUSTOMER_SUMMARY);
        for(Customer customer : bank.getCustomers()){
            result.append(customer.toString());
        }
        return result.toString();
    }

    /**
     * Gets the total interest paid by bank
     * @param bank
     * @return
     */
    public double getTotalInterestPaidBy(Bank bank){
        double totalInterestPaid = 0.0;
        for(Customer customer : bank.getCustomers()){
            BalanceInfo balanceInfo = customer.getTotalInterestEarned();
            totalInterestPaid += balanceInfo.getInterestEarned();
        }
        return totalInterestPaid;
    }

    /**
     * Transfer money between accounts
     * @param source
     * @param destination
     * @param amount
     */
    public boolean transferMoney(Account source, Account destination, double amount){
        if (amount > 0.0 && source != null && destination != null &&
                source.getBalanceInfo().getBalance() >= amount){
            return source.withdraw(amount) && destination.deposit(amount);
        }
        return false;
    }

    private DateProvider getDateProvider(){
        return DateProvider.getInstance();
    }

    /**
     * Would be easy to autowire if using spring
     * @param dailyTransaction
     * @return
     */
    private boolean isTodaysTransationList(TransactionsInADay dailyTransaction) {
        Date now = getDateProvider().now();
        return getDateProvider().getDiffInDays(now, dailyTransaction.getDate()) <= 1;
    }
}
