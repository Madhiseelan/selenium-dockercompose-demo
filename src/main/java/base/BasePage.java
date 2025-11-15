package base;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.util.Base64;


public class BasePage {
    public static String getScreenshot(String imageName, WebDriver driver)
    {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File file = ts.getScreenshotAs(OutputType.FILE);

        String filePath = "./screenshot/"+imageName;

        try {
            FileUtils.copyFile(file, new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return filePath;
    }

    public static String convertImg_Base64(String screenshotPath)
    {
        byte[] fileByte = new byte[0];
        try {
            fileByte = FileUtils.readFileToByteArray(new File(screenshotPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //String base64Img = Base64.encodeBase64String(fileByte);
        //String base64Img = java.util.Base64.getEncoder().encodeToString(fileByte);
        String base64Img;
        base64Img = Base64.getMimeEncoder().encodeToString(fileByte);
        return base64Img;
    }

}
