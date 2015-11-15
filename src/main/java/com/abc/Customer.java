package com.abc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * Customer of the bank
 */
public class Customer  extends ModelsBase implements IModelBase{

    private final String name;
    /**
     * Assuming one account per type
     */
    private Map<AccountType,Account> accounts;

    public Customer(String name) {
        this.name = name;
        this.accounts = new HashMap<AccountType, Account>();
    }

    /**
     * Create an account if type doesn't exist
     * @param accountType
     * @return
     */
    public Account openAccount(AccountType accountType) {
        if (accounts.containsKey(accountType)) {
            return accounts.get(accountType);
        }
        Account account = new Account(accountType);
        accounts.put(accountType, account);
        return account;
    }

    public BalanceInfo getTotalInterestEarned() {
        double totalInterestEarned = 0.0;
        double totalBalance = 0.0;
        for(Account account : this.accounts.values()){
            BalanceInfo balanceInfo = AccountUtils.getInstance().getInterestEarned(account.getOpeningDate(), account.getTransactions(),account.getAccountType());
            totalInterestEarned += balanceInfo.getInterestEarned();
            totalBalance += balanceInfo.getBalance();
        }
        return new BalanceInfo(totalBalance,totalInterestEarned);
    }

    public boolean deposit(double amount, AccountType accountType){
        if (amount > 0 && this.accounts.containsKey(accountType)){
            return this.accounts.get(accountType).deposit(amount);
        }
        return false;
    }

    public boolean withdraw(double amount, AccountType accountType){
        if (amount > 0 && this.accounts.containsKey(accountType)){
            return this.accounts.get(accountType).deposit(amount);
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format((accounts.size() > 1 ? Constants.TOTAL_ACCOUNTS_PLURAL : Constants.TOTAL_ACCOUNTS), getName(), getNumberOfAccounts());
    }

    public String getStatement(){
        StringBuilder statement = new StringBuilder(String.format(Constants.STATEMENT_FOR,this.name));
        for(Account account : this.accounts.values()){
            statement.append(AccountUtils.getInstance().getAccountStatement(account));
        }
        return statement.toString();
    }

    public int getNumberOfAccounts() {
        return accounts.size();
    }

    public Map<AccountType, Account> getAccounts() {
        return Collections.unmodifiableMap(this.accounts);
    }

    public String getName() {
        return name;
    }

    public boolean sendFromCheckingToSaving(double amount){
        if (this.accounts.containsKey(AccountType.CHECKING) &&
                this.accounts.containsKey(AccountType.SAVINGS)){
            return AccountUtils.getInstance().transferMoney(this.getCheckingAccount(),this.getSavingsAccount(),amount);
        }
        return false;
    }

    /**
     * gets the checking account if exists
     * @return
     */
    public Account getCheckingAccount() {
        return this.accounts.get(AccountType.CHECKING);
    }

    /**
     * gets the savings account if exists
     * @return
     */
    public Account getSavingsAccount() {
        return this.accounts.get(AccountType.SAVINGS);
    }

    /**
     * gets the maxi savings account if exists
     * @return
     */
    public Account getMaxiSavingsAccount() {
        return this.accounts.get(AccountType.MAXI_SAVINGS);
    }
}
