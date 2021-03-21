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

    @BeforeEach
    public void setUp() {
        open("http://localhost:9999");
        LoginPage.validLogin(getAuthInfo());
        VerificationPage.validVerify(getVerificationCodeFor(getAuthInfo()));
    }

    @AfterEach
    public void setInitialData(TestInfo testInfo){
        if (testInfo.getTags().contains("skip")) {
            return;
        }
        DashboardPage.setToInitialState();
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
        int initialBalance = DashboardPage.getCardBalance(1);
        DashboardPage.toTransferMoney(1);
        MoneyTransferPage.transferSum(amount, 2);
        int actual = DashboardPage.getCardBalance(1);
        int expected = initialBalance + amount;
        assertEquals(expected, actual);
    }

    @Test
    public void shouldTransferSumToSecond() {
        int amount = DataHelper.getValidSumForTransfer();
        int initialBalance = DashboardPage.getCardBalance(2);
        DashboardPage.toTransferMoney(2);
        MoneyTransferPage.transferSum(amount, 1);
        int actual = DashboardPage.getCardBalance(2);
        int expected = initialBalance + amount;
        assertEquals(expected, actual);
    }

    @Test
    @Tag("skip")
    public void shouldHaveEmptyFormsAlert() {
        DashboardPage.toTransferMoney(1);
        MoneyTransferPage.getEmptyFieldAlert();
    }

    @Test
    @Tag("skip")
    public void shouldHaveEmptyCardAlert() {
        DashboardPage.toTransferMoney(1);
        MoneyTransferPage.getEmptyCardFieldAlert();
    }

    @Test
    @Tag("skip")
    public void shouldHaveEmptySumAlert() {
        DashboardPage.toTransferMoney(2);
        MoneyTransferPage.getEmptySumAlert(1);
    }

    @Test
    @Tag("skip")
    public void shouldHaveWrongCardNumberAlert () {
        DashboardPage.toTransferMoney(1);
        MoneyTransferPage.getWrongCardFieldAlert();
    }

    @Test
    @Tag("skip")
    public void shouldHaveInsufficientBalanceAlert() {
        DashboardPage.toTransferMoney(1);
        MoneyTransferPage.getInsufficientBalanceAlert(1);
    }

}
