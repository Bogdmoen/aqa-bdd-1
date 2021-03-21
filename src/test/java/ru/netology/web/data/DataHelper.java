package ru.netology.web.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.util.Locale;

public  class DataHelper {
    private DataHelper() {}

    private static Faker faker = new Faker(new Locale("ru"));

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }


    public static AuthInfo getOtherAuthInfo(AuthInfo original) {
        return new AuthInfo("petya", "123qwerty");
    }

    @Value
    public static class VerificationCode {
        private String code;
    }

    @Value
    public static class Card {
        private String number;
    }

    public static VerificationCode getVerificationCodeFor(AuthInfo authInfo) {
        return new VerificationCode("12345");
    }

    public static Card[] getCardFor() {
       Card cardOne = new Card("5559 0000 0000 0001");
       Card cardTwo = new Card("5559 0000 0000 0002");
        return new Card[] {cardOne, cardTwo};
    }

    public static int getValidSumForTransfer() {
        return faker.random().nextInt(100, 2000);
    }

    public static String getInvalidCardNumber() {
        return faker.finance().creditCard();
    }



}
