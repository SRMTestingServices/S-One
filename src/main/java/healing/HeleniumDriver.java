package healing;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.epam.healenium.SelfHealingDriver;

public class HeleniumDriver {

	public void selfHealingDriver() throws MalformedURLException {
		WebDriver delegate = new ChromeDriver();
		//create Self-healing driver
		SelfHealingDriver driver = SelfHealingDriver.create(delegate);
		
		
		
		ChromeOptions options = new ChromeOptions();
		//Remote Web Driver to connect to Healenium with URL http://<hlm-proxy-host>:<hlm-proxy-port>. In the case of a local launch: http://localhost:8085
		WebDriver heeleniumServerDriver = new RemoteWebDriver(new URL("http://localhost:8085"), options);
		
		
		//InfraSetup
		
	//	https://healenium.io/docs/download_and_install/hlm_web     // install docker for healenium
	//	https://healenium.io/frameworks#rec639834049	   -> framework sepcifc
	//reporting server    http://localhost:7878/healenium/report/    -> change the server according to that 
	
	}
}
