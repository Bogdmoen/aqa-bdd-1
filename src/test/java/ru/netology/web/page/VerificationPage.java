package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Selenide.*;

public class VerificationPage {
    private static SelenideElement codeField = $("[data-test-id='code'] .input__control");
    private static SelenideElement verifyButton = $("[data-test-id='action-verify']");

    public VerificationPage() {
        codeField.shouldBe(Condition.visible);
    }

    public static DashboardPage validVerify(DataHelper.VerificationCode verificationCode) {
        codeField.setValue(verificationCode.getCode());
        verifyButton.click();
        return new DashboardPage();
    }
}
