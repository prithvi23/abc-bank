package com.abc;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Prithvi on 11/14/15.
 * Account type
 */
public enum AccountType {
    CHECKING,
    SAVINGS,
    MAXI_SAVINGS;

    private static Map<AccountType,String> descriptionMap;

    static {
        descriptionMap = new HashMap<AccountType, String>();
        descriptionMap.put(CHECKING,"Checking Account");
        descriptionMap.put(SAVINGS,"Savings Account");
        descriptionMap.put(MAXI_SAVINGS,"Maxi Savings Account");
    }

    public String getDetailedDescription(){
        return descriptionMap.get(this);
    }
}
