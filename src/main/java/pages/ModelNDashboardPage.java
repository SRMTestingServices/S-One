package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.WebDriverUtils;

public class ModelNDashboardPage {
    private final WebDriver driver;
    private final WebDriverUtils utils;


    private final By mainMenu = By.xpath("//a[@name='~123~-mainMenu-rightMenu-opener0']");
    private final By viewMenu = By.xpath("//a[contains(@name,'mainMenu-rightMenu-viewMenu-opener')]");
    private final By customersMenu = By.xpath("//a[@name='~123~-mainMenu-rightMenu-viewMenu-customersMenu-opener0']");
    private final By accountsMenu = By.xpath("//a[@name='~123~-mainMenu-rightMenu-viewMenu-customersMenu-Accounts0']");
    private final By accountStatus = By.xpath("//select[contains(@name,'searcher-buildContainer-ent-mco-account-status0')]");
    private final By customerType = By.xpath("//select[contains(@name,'searcher-buildContainer-ent-mco-account-orgType0')]");
    private final By domainType = By.xpath("//select[contains(@name,'searcher-buildContainer-ent-mco-account-domain0')]");
    private final By contractsType = By.xpath("//select[contains(@name,'searcher-buildContainer-ent-mco-account-allowContracts0')]");
    private final By saveAsButton = By.xpath("//a[@name='~123~-documentFrame-customers-searcher-saveAs-opener0']");

    public ModelNDashboardPage(WebDriver driver) {
        this.driver = driver;
        this.utils = new WebDriverUtils(driver);
    }

     public ModelNDashboardPage clickMainMenu() {
        utils.clickUsingJS(driver.findElement(mainMenu));
        return this;
    }
    public ModelNDashboardPage clickViewMenu() {
        utils.clickUsingJS(driver.findElement(viewMenu));
        return this;
    }
    public ModelNDashboardPage clickCustomersMenu() {
        utils.clickUsingJS(driver.findElement(customersMenu));
        return this;
    }
    public ModelNDashboardPage clickAccountsMenu() {
        utils.clickUsingJS(driver.findElement(accountsMenu));
        return this;
    }
    public void selectAccountsMenu(String value)
    {
        utils.waitForElementToBeVisible(accountStatus);
        utils.selectDropdownByValue(accountStatus,value);
    }
    public void selectCustomerValue(String value)
    {
        utils.waitForElementToBeVisible(customerType);
        utils.selectDropdownByValue(customerType,value);
    }
    public void selectDomainValue(String value)
    {
        utils.waitForElementToBeVisible(domainType);
        utils.selectDropdownByValue(domainType,value);
    }
    public void selectContractsValue(String value)
    {
        utils.waitForElementToBeVisible(contractsType);
        utils.selectDropdownByValue(contractsType,value);
    }
    public ModelNDashboardPage clickSaveAs() {
        utils.clickUsingJS(driver.findElement(saveAsButton));
        return this;
    }

}
