package com.jenkinsBuild.base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import com.jenkinsBuild.util.TestUtil;
import com.jenkinsBuild.util.WebEventListener;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import io.github.bonigarcia.wdm.WebDriverManager;


public class BaseTest {
	public static WebDriver driver;
	public static Properties prop;
	public static Logger log;
	public static EventFiringWebDriver e_driver;
	public static WebEventListener e_listener;
	public static ExtentReports extent;
	public static ExtentTest extentTest;
	
	public BaseTest() {
		log=Logger.getLogger(this.getClass());
		prop=new Properties();
		try {
			FileInputStream file=new FileInputStream(System.getProperty("user.dir")+
					"./src/test/resources/configuration/config.properties"); 
			prop.load(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@BeforeTest
	public void initExtent() {
		TestUtil.setDateForLog4j();
		//telling where exactly extent report has to be generate.
		extent=new ExtentReports(System.getProperty("user.dir")+"/TestResults/ExtentReport"+TestUtil.getSystemDate()+".html");
		extent.addSystemInfo("Project :", "Cmics");
		extent.addSystemInfo("Host Name", "jaydeep local system");
		extent.addSystemInfo("User Name", "jaydeep.pal@innovaccer.com");
		extent.addSystemInfo("enviornment", "stage");	
	}
	public static void initialization(String browser) {
		if(browser.equals("chrome"))
		{
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
		}
		else if(browser.equals("IE"))
		{
			WebDriverManager.iedriver().setup();
			driver = new InternetExplorerDriver();
		}
		else if(browser.equals("firefox"))
		{
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
		}
		else
		{
			log.error(" Driver is not Set for any Browser");
		}
		e_driver=new EventFiringWebDriver(driver);
		//now creating object of webeventlistener and registering with EventFiringWebDriver
		e_listener=new WebEventListener();
		e_driver.register(e_listener);
		driver=e_driver;
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(TestUtil.Page_Load_TimeOut, TimeUnit.SECONDS);
		driver.get(prop.getProperty("url"));		
	}
	@AfterTest
	public void closeExtent() {
		extent.flush();
		extent.close();
	}
	@AfterMethod
	public void tearDown(ITestResult result) throws IOException {
		if(result.getStatus()==ITestResult.FAILURE)
		{
			extentTest.log(LogStatus.FAIL, "Test Case Failed is "+result.getName()); //To Add Name in Extent Report.
			extentTest.log(LogStatus.FAIL, "Test 	Case Failed is "+result.getThrowable()); //To Add Errors and Exceptions in Extent Report.
		
			String screenshotPath = TestUtil.getScreenshot(driver, result.getName());
			extentTest.log(LogStatus.FAIL, extentTest.addScreenCapture(screenshotPath)); //To Add Screenshot in Extent Report.
		}
		else if(result.getStatus()==ITestResult.SKIP)
		{
			extentTest.log(LogStatus.SKIP, "Test Case Skipped is " + result.getName());
		}
		else if(result.getStatus()==ITestResult.SUCCESS)
		{
			extentTest.log(LogStatus.PASS, "Test Case Passed is " + result.getName());
		}
		extent.endTest(extentTest); //Ending Test and Ends the Current Test and Prepare to Create HTML Report.
		//driver.quit();
		log.info("**************Test End and Browser Terminated*****************");
		
	}
  
}
