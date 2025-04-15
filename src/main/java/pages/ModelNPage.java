package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.SeleniumUtils;
import utils.WaitUtils;

public class ModelNPage {
    private final WebDriver driver;
    private final SeleniumUtils seleniumUtils;
    private final WaitUtils waitUtils;


    private final By launchButton = By.xpath("//button[@class='btn launchButton']");


    public ModelNPage(WebDriver driver) {
        this.driver = driver;
        seleniumUtils = new SeleniumUtils(driver);
        waitUtils = new WaitUtils(driver);
    }

    public void clickLaunchButton() {
        waitUtils.waitForElementToBeClickable(driver.findElement(launchButton), 10);
        seleniumUtils.clickElement(driver.findElement(launchButton));
    }


}
