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
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;

import static utils.ExtentReportHelper.getReportObject;

public class BaseTestAlternative {

    protected WebDriver driver;
    protected String browser;

    protected static ThreadLocal<ExtentTest> testLogger = new ThreadLocal<>();
    private static final ExtentReports reports = getReportObject();
    private static final Logger logger = LogManager.getLogger(BaseTest.class);

    // Initialize WebDriver once per <test> tag (TestNG's <test>), which matches your testng.xml usage.
    @Parameters({"browserName"})
    @BeforeTest(alwaysRun = true)
    public void initDriver(@Optional String browserName, ITestContext context) {
        // prefer explicit parameter, else fallback to the xml test parameter (defensive), else AppConstants
        if (browserName != null && !browserName.trim().isEmpty()) {
            browser = browserName;
        } else {
            // This gets the parameter from the <test> node if present
            String ctxParam = context.getCurrentXmlTest().getParameter("browserName");
            if (ctxParam != null && !ctxParam.trim().isEmpty()) {
                browser = ctxParam;
            } else {
                browser = AppConstants.browserName;
            }
        }

        logger.info("Resolved browser parameter: " + browser);
        logger.info("System property browserName: " + System.getProperty("browserName"));
        logger.info("Platform (AppConstants.platform): " + AppConstants.platform);

        ChromeOptions co = new ChromeOptions();
        FirefoxOptions fo = new FirefoxOptions();

        if (browser.equalsIgnoreCase("chrome")) {
            if (AppConstants.platform.equalsIgnoreCase("local")) {
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
            } else if (AppConstants.platform.equalsIgnoreCase("remote")) {
                // Use the hub URL (not individual node ports). Confirm your hub address/port.
                co.setPlatformName("linux");
                co.setPageLoadStrategy(PageLoadStrategy.EAGER);
                try {
                    // typically the hub URL is like http://localhost:4444/wd/hub
                    URL hub = new URL("http://localhost:4444/wd/hub");
                    driver = new RemoteWebDriver(hub, co);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                logger.error("Platform Not Supported ..!");
            }
        } else if (browser.equalsIgnoreCase("firefox")) {
            if (AppConstants.platform.equalsIgnoreCase("local")) {
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
            } else if (AppConstants.platform.equalsIgnoreCase("remote")) {
                fo.setPlatformName("linux");
                fo.setPageLoadStrategy(PageLoadStrategy.EAGER);
                try {
                    // use hub address here as well
                    URL hub = new URL("http://localhost:4444/wd/hub");
                    driver = new RemoteWebDriver(hub, fo);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                logger.error("Platform Not Supported ..!");
            }
        } else {
            logger.error("Browser Name entered is not supported: " + browser);
            throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        // Optional: maximize or set timeouts here if you want
        logger.info("Driver created for browser: " + browser);
    }

    // Keep per-method reporting & logging
    @BeforeMethod(alwaysRun = true)
    public void setupMethod(ITestResult iTestResult){
        ExtentTest test = reports.createTest(iTestResult.getMethod().getMethodName());
        testLogger.set(test);
        testLogger.get().log(Status.INFO, "Driver Start Time: " + LocalDateTime.now());
        testLogger.get().log(Status.INFO, "Browser used: " + browser);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownMethod(ITestResult iTestResult){
        if(iTestResult.isSuccess())
        {
            testLogger.get().log(Status.PASS, MarkupHelper.createLabel(iTestResult.getMethod().getMethodName()+ " is successful !" , ExtentColor.GREEN));
        } else
        {
            testLogger.get().log(Status.FAIL, "Test Failed Due to: " + iTestResult.getThrowable());
            String screenshot = BasePage.getScreenshot(iTestResult.getMethod().getMethodName() +".jpg", driver);
            testLogger.get().fail(MediaEntityBuilder.createScreenCaptureFromBase64String(BasePage.convertImg_Base64(screenshot)).build());
        }
        testLogger.get().log(Status.INFO, "Driver End Time : " + LocalDateTime.now());
        // do not quit driver here — quit once per <test> in @AfterTest to preserve same session for multiple methods
    }

    @AfterTest(alwaysRun = true)
    public void quitDriver(){
        if (driver != null) {
            try {
                driver.quit();
                logger.info("Driver quit for browser: " + browser);
            } catch (Exception e) {
                logger.warn("Error quitting driver: " + e.getMessage(), e);
            }
        }
    }

    @AfterClass
    public void flushTestReport()
    {
        reports.flush();
    }
}