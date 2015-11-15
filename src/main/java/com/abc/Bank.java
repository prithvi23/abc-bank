package com.abc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Bank object
 */
public class Bank  extends ModelsBase implements IModelBase{

    private List<Customer> customers;

    public Bank() {
        customers = new ArrayList<Customer>();
    }

    public Customer addCustomer(String customerName) {
        Customer customer = new Customer(customerName);
        customers.add(customer);
        return customer;
    }

    public List<Customer> getCustomers() {
        return Collections.unmodifiableList(customers);
    }

    public String getCustomerSummary(){
        return AccountUtils.getInstance().getCustomerAccountReport(this);
    }

    public Customer openAccount(String customerName, AccountType accountType){
        Customer customer = addCustomer(customerName);
        customer.openAccount(accountType);
        return customer;
    }

    public double getTotalInterestPaid(){
        return AccountUtils.getInstance().getTotalInterestPaidBy(this);
    }
}
