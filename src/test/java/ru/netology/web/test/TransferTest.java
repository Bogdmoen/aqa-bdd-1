package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.MoneyTransferPage;
import ru.netology.web.page.VerificationPage;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;
import static ru.netology.web.data.DataHelper.*;

public class TransferTest {

    private LoginPage loginPage;
    private VerificationPage verificationPage;
    private DashboardPage dashboardPage;
    private MoneyTransferPage moneyTransferPage;

    @BeforeEach
    public void setUp() {
        open("http://localhost:9999");
//        LoginPage.validLogin(getAuthInfo());
//        VerificationPage.validVerify(getVerificationCodeFor(getAuthInfo()));
        loginPage = new LoginPage();
        val authInfo = getAuthInfo();
        verificationPage = loginPage.validLogin(authInfo);
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
        dashboardPage.setToInitialState(moneyTransferPage);
    }


//    @Test
//    public void shouldLogin () {
//        val loginPage = new LoginPage();
//        val authInfo = getAuthInfo();
//        val verificationPage = loginPage.validLogin(authInfo);
//        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
//        val dashboardPage = verificationPage.validVerify(verificationCode);
//        System.out.println(dashboardPage.extractBalance("баланс: 1000 р."));
//    }

//    @Test
//    public void shouldExtractBalance() {
//        System.out.println(DashboardPage.getCardBalance(1));
//    }

    @Test
    public void shouldTransferSumToFirst() {
        int amount = DataHelper.getValidSumForTransfer();
        int initialBalance = dashboardPage.getCardBalance(1);
        dashboardPage.toTransferMoney(1);
        moneyTransferPage.transferSum(amount, 2);
        int actual = dashboardPage.getCardBalance(1);
        int expected = initialBalance + amount;
        assertEquals(expected, actual);
    }

    @Test
    public void shouldTransferSumToSecond() {
        int amount = DataHelper.getValidSumForTransfer();
        int initialBalance = dashboardPage.getCardBalance(2);
        dashboardPage.toTransferMoney(2);
        moneyTransferPage.transferSum(amount, 1);
        int actual = dashboardPage.getCardBalance(2);
        int expected = initialBalance + amount;
        assertEquals(expected, actual);
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
