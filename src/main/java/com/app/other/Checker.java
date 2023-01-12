package com.app.other;

import java.time.LocalDate;

public class Checker {
    public static boolean checkDateManufacture(String str)
    {

        String [] temp=str.split("\\.");
        if(temp.length!=3)
        {
            return false;
        }
        try {
            LocalDate localDate = LocalDate.of(Integer.parseInt(temp[2]), Integer.parseInt(temp[1]), Integer.parseInt(temp[0]));
            LocalDate now=LocalDate.now();
            if(now.isBefore(localDate)||localDate.isBefore(localDate.minusYears(50)))
            {
                return false;
            }
        }
        catch (Exception ex)
        {
            return false;
        }

        return true;


    }
    public static boolean checkDateExpiration(String str)
    {

        String [] temp=str.split("\\.");
        if(temp.length!=3)
        {
            return false;
        }

        try {
            LocalDate localDate = LocalDate.of(Integer.parseInt(temp[2]), Integer.parseInt(temp[1]), Integer.parseInt(temp[0]));
            LocalDate now=LocalDate.now();
            if(now.isAfter(localDate)||localDate.isBefore(localDate.minusYears(50)))
            {
                return false;
            }
        }
        catch (Exception ex)
        {
            return false;
        }

        return true;


    }
}
