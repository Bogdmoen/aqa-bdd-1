package ru.netology.web.data;

import com.github.javafaker.Faker;
import lombok.Value;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.MoneyTransferPage;

import java.util.Locale;

public  class DataHelper {
    private DataHelper() {}

    private static final Faker faker = new Faker(new Locale("ru"));

    @Value
    public static class AuthInfo {
        String login;
        String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }


    public static AuthInfo getOtherAuthInfo(AuthInfo original) {
        return new AuthInfo("petya", "123qwerty");
    }

    @Value
    public static class VerificationCode {
        String code;
    }

    @Value
    public static class Card {
        String number;
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


    public static void setToInitialState(DashboardPage dashboardPage, MoneyTransferPage moneyTransferPage) {
        int firstAmount = dashboardPage.getCardBalance(1);
        int secondAmount = dashboardPage.getCardBalance(2);
        int transferAmount;
        if (firstAmount < secondAmount) {
            transferAmount = 10000 - firstAmount;
            dashboardPage.toTransferMoney(1);
            moneyTransferPage.transferSum(transferAmount, 2);
        }
        else {
            transferAmount = 10000 - secondAmount;
            dashboardPage.toTransferMoney(2);
            moneyTransferPage.transferSum(transferAmount, 1);
        }
    }

}
