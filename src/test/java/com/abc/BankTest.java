package com.abc;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BankTest {
    private static final double DOUBLE_DELTA = 1e-15;

    @Test
    public void customerSummary() {
        Bank bank = new Bank();
        bank.openAccount("John",AccountType.CHECKING);
        assertEquals("Customer Summary\n - John (1 account)", bank.getCustomerSummary());
    }

    @Test
    public void checkingAccount() {
        Bank bank = new Bank();
        Customer bill = bank.openAccount("Bill", AccountType.CHECKING);
        assertNotNull(bill);
        boolean deposit = bill.deposit(100.0, AccountType.CHECKING);
        assertTrue(deposit);
        assertEquals(0.1, bank.getTotalInterestPaid(), DOUBLE_DELTA);
    }

    @Test
    public void savings_account() {
        Bank bank = new Bank();
        Customer bill = bank.openAccount("Bill", AccountType.SAVINGS);
        assertNotNull(bill);
        boolean deposit = bill.deposit(1500.0, AccountType.SAVINGS);
        assertTrue(deposit);
        assertEquals(2.0, bank.getTotalInterestPaid(), DOUBLE_DELTA);
    }

    @Test
    public void maxi_savings_account() {
        Bank bank = new Bank();
        Customer bill = bank.openAccount("Bill", AccountType.MAXI_SAVINGS);
        assertNotNull(bill);
        boolean deposit = bill.deposit(3000, AccountType.MAXI_SAVINGS);
        assertTrue(deposit);
        assertEquals(150.0, bank.getTotalInterestPaid(), DOUBLE_DELTA);
    }
}
