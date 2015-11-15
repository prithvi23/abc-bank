package com.abc;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateProvider{

    private static DateProvider instance = new DateProvider();

    private DateProvider(){
    }

    public static DateProvider getInstance() {
        return instance;
    }

    public Date now() {
        return Calendar.getInstance().getTime();
    }

    public int getDiffInDays(Date newerDate, Date olderDate){
        int daysInPeriod = (int)( (newerDate.getTime() - olderDate.getTime())
                / (1000 * 60 * 60 * 24) );
        if (daysInPeriod <= 1 && newerDate .getTime() > olderDate.getTime()){
            return 1;
        }
        return daysInPeriod;
    }

    public boolean isLeapYear() {
        Calendar cal = Calendar.getInstance();
        return cal.getActualMaximum(Calendar.DAY_OF_YEAR) > 365;
    }

    public String getDateString(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_PATTERN);
        return formatter.format(date);
    }
}
