package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.MoneyTransferPage;
import ru.netology.web.page.VerificationPage;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;
import static ru.netology.web.data.DataHelper.*;

public class TransferTest {

    private DashboardPage dashboardPage;
    private MoneyTransferPage moneyTransferPage;

    @BeforeEach
    public void setUp() {
        open("http://localhost:9999");
        LoginPage loginPage = new LoginPage();
        val authInfo = getAuthInfo();
        VerificationPage verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);
        moneyTransferPage = dashboardPage.toTransferMoney(1);
        moneyTransferPage.cancelTransfer();
    }

    @AfterEach
    public void setInitialData(TestInfo testInfo){
        if (testInfo.getTags().contains("skip")) {
            return;
        }
        DataHelper.setToInitialState(dashboardPage, moneyTransferPage);
    }

    @Test
    public void shouldTransferSumToFirst() {
        int amount = DataHelper.getValidSumForTransfer();
        int initialBalanceFirst = dashboardPage.getCardBalance(1);
        int initialBalanceSecond = dashboardPage.getCardBalance(2);
        dashboardPage.toTransferMoney(1);
        moneyTransferPage.transferSum(amount, 2);
        int[] expected = {initialBalanceFirst + amount, initialBalanceSecond - amount};
        int[] actual =  {dashboardPage.getCardBalance(1), dashboardPage.getCardBalance(2)};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldTransferSumToSecond() {
        int amount = DataHelper.getValidSumForTransfer();
        int initialBalanceFirst = dashboardPage.getCardBalance(1);
        int initialBalanceSecond = dashboardPage.getCardBalance(2);
        dashboardPage.toTransferMoney(2);
        moneyTransferPage.transferSum(amount, 1);
        int[] expected = {initialBalanceFirst - amount, initialBalanceSecond + amount};
        int[] actual = {dashboardPage.getCardBalance(1), dashboardPage.getCardBalance(2)};
        assertArrayEquals(expected, actual);
    }

    @Test
    @Tag("skip")
    public void shouldHaveEmptyFormsAlert() {
        dashboardPage.toTransferMoney(1);
        moneyTransferPage.getEmptyFieldAlert();
    }

    @Test
    @Tag("skip")
    public void shouldHaveEmptyCardAlert() {
        dashboardPage.toTransferMoney(1);
        moneyTransferPage.getEmptyCardFieldAlert();
    }

    @Test
    @Tag("skip")
    public void shouldHaveEmptySumAlert() {
        dashboardPage.toTransferMoney(2);
        moneyTransferPage.getEmptySumAlert(1);
    }

    @Test
    @Tag("skip")
    public void shouldHaveWrongCardNumberAlert () {
        dashboardPage.toTransferMoney(1);
        moneyTransferPage.getWrongCardFieldAlert();
    }

    @Test
    public void shouldHaveInsufficientBalanceAlert() {
        int balance = dashboardPage.getCardBalance(1);
        dashboardPage.toTransferMoney(1);
        moneyTransferPage.getInsufficientBalanceAlert(1, balance);
    }

    @Test
    public void shouldCancelTransfer() {
        dashboardPage.toTransferMoney(1);
        moneyTransferPage.cancelTransfer();
        dashboardPage.cards.first().shouldBe(visible);
    }

}
