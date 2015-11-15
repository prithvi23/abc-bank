package com.abc;

import org.junit.Test;

import static org.junit.Assert.*;

public class CustomerTest {

    @Test //Test customer statement generation
    public void testApp(){
        Bank bank = new Bank();
        Customer oscar = bank.addCustomer("Oscar");
        assertNotNull(oscar);
        assertEquals(0, oscar.getNumberOfAccounts());
        Account checkingAccount = oscar.openAccount(AccountType.CHECKING);
        assertEquals(1, oscar.getNumberOfAccounts());
        Account savingsAccount = oscar.openAccount(AccountType.SAVINGS);
        assertEquals(2, oscar.getNumberOfAccounts());
        checkingAccount.deposit(100.0);
        String statement = oscar.getStatement();
        assertTrue(statement.contains("Checking Account"));
        assertTrue(statement.contains("Total $100.000000"));
        savingsAccount.deposit(4000.0);
        savingsAccount.withdraw(200.0);
        statement = oscar.getStatement();
        assertTrue(statement.contains("Statement for Oscar"));
        assertTrue(statement.contains("Savings Account"));
        assertTrue(statement.contains("Total $3800.000000"));
    }

    @Test
    public void testAccounts(){
        Bank bank = new Bank();
        Customer oscar = bank.openAccount("Oscar", AccountType.CHECKING);
        assertNotNull(oscar);
        assertEquals(1, oscar.getNumberOfAccounts());
        //Check that only one checking account is allowed per customer as per current design
        oscar.openAccount(AccountType.CHECKING);
        assertEquals(1, oscar.getNumberOfAccounts());
        oscar.openAccount(AccountType.SAVINGS);
        assertEquals(2, oscar.getNumberOfAccounts());
        oscar.openAccount(AccountType.MAXI_SAVINGS);
        assertEquals(3, oscar.getNumberOfAccounts());
        assertEquals("Customer Summary\n - Oscar (3 accounts)", bank.getCustomerSummary());
    }

    @Test
    public void canTransferBetweenbAccounts(){
        Bank bank = new Bank();
        Customer oscar = bank.addCustomer("Oscar");
        assertNotNull(oscar);
        assertEquals(0, oscar.getNumberOfAccounts());
        Account checkingAccount = oscar.openAccount(AccountType.CHECKING);
        assertEquals(1, oscar.getNumberOfAccounts());
        Account savingsAccount = oscar.openAccount(AccountType.SAVINGS);
        assertEquals(2, oscar.getNumberOfAccounts());
        checkingAccount.deposit(100.0);
        String statement = oscar.getStatement();
        assertTrue(statement.contains("Checking Account"));
        assertTrue(statement.contains("Total $100.000000"));
        savingsAccount.deposit(4000.0);
        savingsAccount.withdraw(200.0);
        boolean success = oscar.sendFromCheckingToSaving(100.0);
        assertTrue(success);
        success = oscar.sendFromCheckingToSaving(100.0);
        assertFalse(success);
    }

}
