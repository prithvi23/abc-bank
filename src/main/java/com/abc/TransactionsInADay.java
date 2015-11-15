package com.abc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class maintains a list of transactions in a day
 * Created by Prithvi on 11/14/15.
 */
public class TransactionsInADay extends ModelsBase implements IModelBase{
    private final Date date;
    private List<Transaction> transactions;

    public TransactionsInADay() {
        this.date =  DateProvider.getInstance().now();
        this.transactions = new ArrayList<Transaction>();
    }

    public Date getDate() {
        return date;
    }

    public boolean deposit(double amount){
        return this.transactions.add(new Transaction(amount,TransactionType.DEPOSIT));
    }

    public boolean withdraw(double amount){
        return this.transactions.add(new Transaction(amount,TransactionType.WITHDRAWL));
    }

    /**
     * Simply checks if a withdrawl is made today
     * @return
     */
    public boolean isWithdrawlMadeToday(){
        for(Transaction transaction : this.transactions){
            if (transaction.getTransationType() == TransactionType.WITHDRAWL){
                return true;
            }
        }
        return false;
    }

    /**
     * Simply sums or subtracts todays transactions amount based on the type
     * @return
     */
    public double getTodaysBalance(){
        double todaysBalance = 0;
        for(Transaction transaction : this.transactions){
           todaysBalance =  transaction.getTransationType() == TransactionType.DEPOSIT ?
                   todaysBalance + transaction.getAmount() : todaysBalance - transaction.getAmount();
        }
        return todaysBalance;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for(Transaction transaction : transactions){
            result.append(DateProvider.getInstance().getDateString(this.getDate()));
            result.append(Constants.TAB);
            result.append(transaction.toString());
            result.append(Constants.NEW_LINE);
        }
        return result.toString();
    }
}
