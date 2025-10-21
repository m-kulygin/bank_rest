package com.example.bankcards.util;

public class CardNumberMasker {
    public static String maskCardNumber(String fullNumber) {
        return "**** **** **** " + fullNumber.substring(fullNumber.length() - 4);
    }
}
