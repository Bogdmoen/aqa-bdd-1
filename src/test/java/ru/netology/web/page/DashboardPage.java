package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;

import static com.codeborne.selenide.Selenide.*;

public class DashboardPage {
    public static SelenideElement heading = $("[data-test-id='dashboard']");
    public static ElementsCollection cards = $$(".list__item");
    public static ElementsCollection depositButtons = $$("[data-test-id='action-deposit']");
    public static SelenideElement refreshButton = $("[data-test-id='action-reload']");
    public final static String balanceStart = "баланс: ";
    public final static String balanceFinish = " р.";

    public DashboardPage() {
        heading.shouldBe(Condition.visible);
    }

    public static int getCardBalance(int number) {
        number--;
        val text = cards.get(number).text();
        return extractBalance(text);
    }

    public static int extractBalance (String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return  Integer.parseInt(value);
    }



    public static MoneyTransferPage toTransferMoney (int card) {
        card--;
        depositButtons.get(card).click();
        return new MoneyTransferPage();
    }

    public static void setToInitialState() {
        int firstAmount = DashboardPage.getCardBalance(1);
        int secondAmount = DashboardPage.getCardBalance(2);
        int transferAmount;
        if (firstAmount < secondAmount) {
            transferAmount = 10000 - firstAmount;
            toTransferMoney(1);
            MoneyTransferPage.transferSum(transferAmount, 2);
        }
        else {
            transferAmount = 10000 - secondAmount;
            toTransferMoney(2);
            MoneyTransferPage.transferSum(transferAmount, 1);
        }

    }
}
