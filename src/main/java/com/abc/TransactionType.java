package com.abc;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Prithvi on 11/14/15.
 */
public enum TransactionType {
    DEPOSIT,
    WITHDRAWL;

    private static Map<TransactionType,String> descriptionMap;

    static {
        descriptionMap = new HashMap<TransactionType, String>();
        descriptionMap.put(DEPOSIT,"  deposit $%f");
        descriptionMap.put(WITHDRAWL,"  withdrawal $%f");
    }

    public String getDetailedDescription(){
        return descriptionMap.get(this);
    }
}
