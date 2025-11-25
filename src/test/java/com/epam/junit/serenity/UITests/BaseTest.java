package com.epam.junit.serenity.UITests;

import net.serenitybdd.annotations.Managed;
import net.serenitybdd.annotations.Steps;
import org.openqa.selenium.WebDriver;
import steps.GoogleSearchSteps;

public class BaseTest {

    @Managed
    protected WebDriver driver;

    @Steps
    GoogleSearchSteps googleSearchSteps;
}
