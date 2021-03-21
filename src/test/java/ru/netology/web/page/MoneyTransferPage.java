package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.conditions.Visible;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class MoneyTransferPage {
    public static SelenideElement fieldAmount = $("[data-test-id='amount'] .input__control");
    public static SelenideElement fieldFrom = $("[data-test-id='from'] .input__control");
    public static SelenideElement fieldTo = $("[data-test-id='to'] .input__control");
    public static SelenideElement transferButton = $("[data-test-id='action-transfer']");
    public static SelenideElement cancelButton = $("[data-test-id='action-cancel']");
    public static SelenideElement errorNotification = $("[data-test-id='error-notification']");
    public static SelenideElement errorNotificationText = $("[data-test-id='error-notification'] .notification__content");

    public MoneyTransferPage() {
        $(By.xpath("//h1[text()='Пополнение карты']")).shouldBe(visible);
    }

    public static void transferSum(int amount, int card) {
        card--;
        fieldAmount.sendKeys(Keys.chord(Keys.CONTROL, "A"), Keys.DELETE);
        fieldAmount.setValue(String.valueOf(amount));
        fieldFrom.sendKeys(Keys.chord(Keys.CONTROL, "A"), Keys.DELETE);
        fieldFrom.setValue(DataHelper.getCardFor()[card].getNumber());
        transferButton.click();
    }

    public static void getEmptyFieldAlert() {
        transferButton.click();
        errorNotification.shouldBe(visible);
        errorNotificationText.shouldHave(exactText("Ошибка! Произошла ошибка"));
    }

    public static void getEmptyCardFieldAlert() {
        fieldAmount.setValue(String.valueOf(DataHelper.getValidSumForTransfer()));
        transferButton.click();
        errorNotification.shouldBe(visible);
        errorNotificationText.shouldHave(exactText("Ошибка! Произошла ошибка"));
    }

    public static void getWrongCardFieldAlert() {
        fieldAmount.setValue(String.valueOf(DataHelper.getValidSumForTransfer()));
        fieldFrom.setValue(DataHelper.getInvalidCardNumber());
        transferButton.click();
        errorNotification.shouldBe(visible);
        errorNotificationText.shouldHave(exactText("Ошибка! Произошла ошибка"));
    }

    public static void getEmptySumAlert(int card) {
        fieldFrom.setValue(DataHelper.getCardFor()[card].getNumber());
        transferButton.click();
        errorNotification.shouldBe(visible);
        errorNotification.shouldHave(exactText("Ошибка! Произошла ошибка"));
    }

    public static void getInsufficientBalanceAlert(int card) {
        int amount = DashboardPage.getCardBalance(card);
        int transfer = amount * 2;
        fieldAmount.setValue(String.valueOf(transfer));
        fieldFrom.setValue(DataHelper.getCardFor()[card].getNumber());
        transferButton.click();
        errorNotification.shouldBe(visible);
        errorNotification.shouldHave(exactText("Ошибка! Произошла ошибка"));
    }
}
