package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.SeleniumUtils;
import utils.WaitUtils;

public class ModelNDashboardPage {
    private WebDriver driver;
    private SeleniumUtils seleniumUtils;
    private WaitUtils waitUtils;


    private final By mainMenu = By.xpath("//a[@name='~123~-mainMenu-rightMenu-opener0']");
    private final By viewMenu = By.xpath("//a[contains(@name,'mainMenu-rightMenu-viewMenu-opener')]");
    private final By customersMenu = By.xpath("//a[@name='~123~-mainMenu-rightMenu-viewMenu-customersMenu-opener0']");

    public ModelNDashboardPage(WebDriver driver) {
        this.driver = driver;
        seleniumUtils = new SeleniumUtils(driver);
        waitUtils = new WaitUtils(driver);
    }

    public ModelNDashboardPage clickMainMenu() {
        waitUtils.waitForElementToBeClickable(driver.findElement(mainMenu), 10);
        seleniumUtils.clickElement(driver.findElement(mainMenu));
        return this;
    }
    public ModelNDashboardPage clickViewMenu() {
        waitUtils.waitForElementToBeClickable(driver.findElement(viewMenu), 10);
        seleniumUtils.clickElement(driver.findElement(viewMenu));
        return this;
    }
    public ModelNDashboardPage clickCustomersMenu() {
        waitUtils.waitForElementToBeClickable(driver.findElement(customersMenu), 10);
        seleniumUtils.clickElement(driver.findElement(customersMenu));
        return this;
    }
}
