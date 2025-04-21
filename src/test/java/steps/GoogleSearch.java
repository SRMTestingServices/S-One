package steps;

import base.DriverManager;
import database.DatabaseUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import pages.GooglePage;
import pages.LoginPage;
import reporting.HtmlReportGenerator;
import reporting.Status;
import reporting.TestStep;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;

import api.RestAssuredUtil;
import utils.ScreenshotUtil;
import utils.StepNamePlugin;

public class GoogleSearch {
    private final WebDriver driver;
    public static Logger log = LogManager.getLogger(LoginPage.class);
    private RestAssuredUtil restAssuredUtil;
    private String baseurl="";
    private String endpoint="";
    String url = "jdbc:mysql://localhost:3306/testdb"; 
    String username = ""; 
    String password = ""; 
    String driverClass = "com.mysql.cj.jdbc.Driver"; 
    String query = "";
    List<TestStep> testSteps = new ArrayList<>();
    private GooglePage gPage;
    
    public GoogleSearch() {
        this.driver = DriverManager.getDriver();
        gPage=new GooglePage(driver);
    }

    @BeforeClass
	public void setup() {
		restAssuredUtil = new RestAssuredUtil(baseurl);
	}

	@Given("I open the browser and navigate to {string}")
	public void iOpenTheBrowserAndNavigateTo(String arg0) {
		driver.get(arg0);
		log.info("Driver is initialized");
		/*// API code
		Response response = restAssuredUtil.get(endpoint);
		Assert.assertTrue(restAssuredUtil.validateStatusCode(response, 200), "GET request failed");
		System.out.println("Response: " + response.asString());
		testSteps.add(new TestStep("2", "Step 2", "Test step 2 description", Status.FAIL, System.getProperty("user.dir")+"\\src\\test\\resources\\screenshots\\image2.png"));
		// DB Code
		DatabaseUtils.executeSelectQuery(url, username, password, driverClass, query);*/

		//testSteps.add(new TestStep("2", "Step 2", "Test step 2 description", Status.PASS, ScreenshotUtil.captureScreenshot(driver)));
	}

    @Then("the page title should be {string}")
    public void thePageTitleShouldBe(String expectedTitle) {
        String actualTitle = driver.getTitle();
        Assert.assertEquals(actualTitle, expectedTitle, "Page title mismatch!");
        gPage.enterSearchBox("Deepan Fernandez");
        log.info("Page title is validated");
        //testSteps.add(new TestStep("3", "Step 3", "Entered search text", Status.PASS, ScreenshotUtil.captureScreenshot(driver)));
        //HtmlReportGenerator.generateHtmlReport("Chrome", "Login Test", "Project X", testSteps,false);//if we want show image directly then pass end argument as false
    }
    
    @AfterTest
    public void generateReport() {
//    	HtmlReportGenerator.generateHtmlReport("Chrome", "Login Test", "Project X", testSteps,true);//if we want image as Link to click and see then enable it as true 
    	//HtmlReportGenerator.generateHtmlReport("Chrome", "Login Test", "Project - Google", testSteps,false);//if we want show image directly then pass end argument as false
    }
    
}
