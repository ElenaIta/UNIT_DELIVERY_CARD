package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.*;

public class DeliveryCardTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    public void successfulFormSubmission() {
        $("[data-test-id=city] input").setValue("Тверь");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        String verificationDate = LocalDate.now().plusDays(3)         //Текущая дата плюс 3 дня
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));     //Формат даты день.месяц.год
        $("[data-test-id=date] input").setValue(verificationDate);
        $("[data-test-id=name] input").setValue("Устимова Агнесса");
        $("[data-test-id=phone] input").setValue("+79036118899");
        $("[data-test-id=agreement]").click();
        $x("//*[text()=\"Забронировать\"]").click();
        $("[data-test-id=notification]")
                .shouldHave(Condition.text("Успешно! Встреча успешно забронирована на " + verificationDate),
                        Duration.ofSeconds(15));                        //Загрузка не более 15 секунд
    }

    @Test
    public void successfulSubmissionAfterInteractingWithComplexElements() {
        $("[data-test-id=city] input").setValue("Тв");
        $(Selectors.byText("Тверь")).click();
        $("[data-test-id=date] input").click();
        int days = 4;                                                   //количество дней после даты по умолчанию
        for (int cycle = 0; cycle < days; cycle++) {
            $(".calendar").sendKeys(Keys.ARROW_RIGHT);
        }
        $(".calendar").sendKeys(Keys.ENTER);
        $("[data-test-id=name] input").setValue("Устимова Агнесса");
        $("[data-test-id=phone] input").setValue("+79036118899");
        $("[data-test-id=agreement]").click();
        $(".button").shouldHave(Condition.text("Забронировать")).click();
        String verificationDate = LocalDate.now().plusDays(7)           //Текущая дата плюс 7 дней
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));     //Формат даты день.месяц.год
        $("[data-test-id=notification]")
                .shouldHave(Condition.text("Успешно! Встреча успешно забронирована на " + verificationDate),
                        Duration.ofSeconds(15));                        //Загрузка не более 15 секунд
    }
}
