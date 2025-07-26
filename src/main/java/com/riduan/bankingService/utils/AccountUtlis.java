package com.riduan.bankingService.utils;

import java.time.Year;

public class AccountUtlis {

    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "This user already has an account Created!";
    public static final String ACCOUNT_CREATION_SUCCESS = "002";
    public static final String ACCOUNT_CREATION_MESSAGE = "Account Successfully Created!";
    public static final String ACCOUNT_NOT_EXISTS_CODE = "003";
    public static final String ACCOUNT_NOT_EXISTS_MESSAGE = "Account is not found!";
    public static final String FOUND_ACCOUNT_SUCCESSFUL_CODE = "004";
    public static final String FOUND_ACCOUNT_SUCCESSFUL_MESSAGE = "Account found!";


    public static final String CREDITED_ACCOUNT_SUCCESSFUL_CODE = "005";
    public static final String CREDITED_ACCOUNT_SUCCESSFUL_MESSAGE = "Amount Credited Successful!";


    public static final String INSUFFICIENT_BALANCE_CODE = "006";
    public static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient Balance";
    public static final String ACCOUNT_DEBITED_SUCCESS = "007";
    public static final String ACCOUNT_DEBITED_MESSAGE = "Account has been successfully debited";


    public static final String TRANSFER_SUCCESSFUL_CODE = "008";
    public static final String TRANSFER_SUCCESSFUL_MESSAGE = "Amount transferred successfully! ";
    public static  String generateAccountNumber(){
        /*
         * 2023 + randomSixDigits
         * */
        Year currentYear = Year.now();
        int min = 100000;
        int max = 999999;


        // Generate a random number between min and max
        int randNumber = (int)Math.floor(Math.random() * (max - min +1) + min);
        //convert the current and randomNumber to strings, then concatenate
        String year = String.valueOf(currentYear);
        String randomNumber = String.valueOf((randNumber));

        StringBuilder accountNumber = new StringBuilder();

        return accountNumber.append(year).append(randomNumber).toString();
    }
}
