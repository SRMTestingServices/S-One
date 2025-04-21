package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.WebDriverUtils;

public class ModelNPage {
    private final WebDriver driver;
    private WebDriverUtils utils;


    private final By launchButton = By.xpath("//button[@class='btn launchButton']");


    public ModelNPage(WebDriver driver) {
        this.driver = driver;
        this.utils = new WebDriverUtils(driver);
    }

    public void clickLaunchButton() {
        utils.click(launchButton);
    }


}
