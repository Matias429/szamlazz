package hu.mbalazs.szamlazz.helpers;

public enum PaymentMethods {
    TRANSFER("átutalás"),
    CASH("készpénz"),
    CARD("bankkártya"),
    CHECK("csekk"),
    ONDELIVERY("utánvét"),
    PRESENT("ajándékutalvány"),
    BARION("barion"),
    BARTER("barter"),
    GROUP_COLLECTION("csoportos beszedés"),
    OTP("OTP Simple"),
    COMPENSATION("kompenzáció"),
    COUPON("kupon"),
    PAYPAL("PayPal"),
    PAYU("PayU"),
    SZEP("SZÉP kártya"),
    VOUCHER("utalvány");

    public final String label;

    PaymentMethods(String label) {
        this.label = label;
    }
}
