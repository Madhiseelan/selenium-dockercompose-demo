package page_tests;

import base.AppConstants;
import base.BasePage;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;

import static utils.ExtentReportHelper.getReportObject;

public class BaseTest {

    protected WebDriver driver;
    protected String browser;

    protected static ThreadLocal<ExtentTest> testLogger = new ThreadLocal<>();
    private static final ExtentReports reports = getReportObject();
    private static final Logger logger = LogManager.getLogger(BaseTest.class);

    @Parameters({"browserName"})
    @BeforeMethod(alwaysRun = true)
    public void setupTest(@Optional String browserName, ITestResult iTestResult){

        ChromeOptions co = new ChromeOptions();
        FirefoxOptions fo = new FirefoxOptions();

        if(browserName!=null)
        {
            browser = browserName;
        } else {
            browser = AppConstants.browserName;
        }
        logger.info("Logger Msg: Browser Name is " + browser);
        System.out.println("Browser Name : " + browser);

        logger.info("Resolved browser parameter: " + browser);
        logger.info("System property browserName: " + System.getProperty("browserName"));
        logger.info("Platform (AppConstants.platform): " + AppConstants.platform);

        if(browser.equalsIgnoreCase("chrome"))
        {
            if(AppConstants.platform.equalsIgnoreCase("local")){
                //co.addArguments("--remote-allow-origins=*");
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
            }
            else if(AppConstants.platform.equalsIgnoreCase("remote"))
            {
                co.setPlatformName("linux");
                co.setPageLoadStrategy(PageLoadStrategy.EAGER);
                try {
                    //driver = new RemoteWebDriver(new URL("http://localhost:4441"), co); //This is for docker-compose standalone
                    driver = new RemoteWebDriver(new URL("http://localhost:4444"), co);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }
            else if (AppConstants.platform.equalsIgnoreCase("remote_git")){
                co.addArguments("--headless"); //for github actions only
                co.addArguments("--disable-gpu");
                co.addArguments("--no-sandbox");
                WebDriverManager.chromedriver().setup();
                co.addArguments("--remote-allow-origins=*");
                driver = new ChromeDriver();
            }
            else{
                logger.error("Platform Not Supported ..!");
            }
        }
        else if (browser.equalsIgnoreCase("firefox") )
        {
            if(AppConstants.platform.equalsIgnoreCase("local")) {
                //fo.addArguments("--remote-allow-origins=*");
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
            }
            else if(AppConstants.platform.equalsIgnoreCase("remote"))
            {
                fo.setPlatformName("linux");
                fo.setPageLoadStrategy(PageLoadStrategy.EAGER);
                try {
                    //driver = new RemoteWebDriver(new URL("http://localhost:4442"), fo);
                    driver = new RemoteWebDriver(new URL("http://localhost:4444"), fo);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }
            else{
                logger.error("Platform Not Supported ..!");
            }
        }
        else {
            System.out.println("Browser Name entered is not supported");
            logger.info("*** Logger Msg: Invalid Browser Name ");
        }
        ExtentTest test =  reports.createTest(iTestResult.getMethod().getMethodName());
        testLogger.set(test);
        testLogger.get().log(Status.INFO, "Driver Start Time: " + LocalDateTime.now());

    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult iTestResult){

        if(iTestResult.isSuccess())
        {
            testLogger.get().log(Status.PASS, MarkupHelper.createLabel(iTestResult.getMethod().getMethodName()+ " is successfull !" , ExtentColor.GREEN));
        } else
        {
            testLogger.get().log(Status.FAIL, "Test Failed Due to: " + iTestResult.getThrowable());
            String screenshot = BasePage.getScreenshot(iTestResult.getMethod().getMethodName() +".jpg", driver);
            testLogger.get().fail(MediaEntityBuilder.createScreenCaptureFromBase64String(BasePage.convertImg_Base64(screenshot)).build());
        }
        testLogger.get().log(Status.INFO, "Driver End Time : " + LocalDateTime.now());
        driver.quit();
    }


    @AfterClass
    public void flushTestReport()
    {
        reports.flush();
    }
}
