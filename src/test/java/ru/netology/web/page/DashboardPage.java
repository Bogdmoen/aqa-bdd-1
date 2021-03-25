package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;

import static com.codeborne.selenide.Selenide.*;

public class DashboardPage {
    public  SelenideElement heading = $("[data-test-id='dashboard']");
    public  ElementsCollection cards = $$(".list__item");
    public  ElementsCollection depositButtons = $$("[data-test-id='action-deposit']");
    public  SelenideElement refreshButton = $("[data-test-id='action-reload']");
    public final String balanceStart = "баланс: ";
    public final String balanceFinish = " р.";

    public DashboardPage() {
        heading.shouldBe(Condition.visible);
    }

    public int getCardBalance(int number) {
        number--;
        val text = cards.get(number).text();
        return extractBalance(text);
    }

    public int extractBalance (String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return  Integer.parseInt(value);
    }


    public MoneyTransferPage toTransferMoney (int card) {
        card--;
        depositButtons.get(card).click();
        return new MoneyTransferPage();
    }

    public void setToInitialState(MoneyTransferPage moneyTransferPage) {
        int firstAmount = getCardBalance(1);
        int secondAmount = getCardBalance(2);
        int transferAmount;
        if (firstAmount < secondAmount) {
            transferAmount = 10000 - firstAmount;
            toTransferMoney(1);
            moneyTransferPage.transferSum(transferAmount, 2);
        }
        else {
            transferAmount = 10000 - secondAmount;
            toTransferMoney(2);
            moneyTransferPage.transferSum(transferAmount, 1);
        }

    }
}
