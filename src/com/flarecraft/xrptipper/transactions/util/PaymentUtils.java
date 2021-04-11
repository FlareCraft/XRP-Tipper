package com.flarecraft.xrptipper.transactions.util;

import java.math.BigInteger;

public class PaymentUtils {

    public static BigInteger convertIntToDrops(double amount) {

        BigInteger returnAmount = BigInteger.valueOf((long) (amount * 1000000L));
        return returnAmount;
    }
}