package com.flarecraft.xrptipper.transactions.util;

import java.math.BigInteger;

public class PaymentUtils {

    public static BigInteger convertIntToDrops(double amount) {

        System.out.println(amount);
        System.out.println((long) amount * 1000000L);
        BigInteger returnAmount = BigInteger.valueOf((long) (amount * 1000000L));
        System.out.println("return amount is: " + returnAmount);
        return returnAmount;
    }
}