package steps;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.annotations.Steps;
import pages.GoogleHomePage;

public class GoogleSearchSteps extends BaseSteps{

    GoogleHomePage googleHomePage;

    @Step("Open Google home page")
    public void openGoogleHomePage() {
        googleHomePage.open();
    }

    @Step("Type in Search Box")
    public void typeInSearchBox(String text) {
        googleHomePage.typeInSearchBox(text);
    }
    @Step("Type Search Box and Press Enter")
    public void typeAndEnter(String text) {
        googleHomePage.typeAndEnter(text);
    }

    @Step("Type in Search Box and Press Tab")
    public void typeAndTab(String text) {
        googleHomePage.typeAndTab(text);
    }

    @Step("Click Search Button")
    public void clickSearchButton() {
        googleHomePage.clickSearchButton();
    }
}
