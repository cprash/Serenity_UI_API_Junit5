package pages;

import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.WebDriver;

@DefaultUrl("https://www.google.com")
public class GoogleHomePage extends BasePage{

    public GoogleHomePage(WebDriver driver) {
        super(driver);
    }

    @FindBy(name = "q")
    private WebElementFacade searchBox;

    @FindBy(name = "btnK")
    private WebElementFacade searchButton;


    public void typeInSearchBox(String text) {
        searchBox.clear();
        searchBox.sendKeys(text);
    }

    public void typeAndEnter(String text) {
        searchBox.clear();
        searchBox.sendKeys(text + "\n");
    }

    public void typeAndTab(String text) {
        searchBox.clear();
        searchBox.sendKeys(text + "\t");
    }

    public void clickSearchButton() {
        searchButton.click();
    }
}
