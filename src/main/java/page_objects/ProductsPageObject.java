package page_objects;

import generic_keywords.WebElementsInteractions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductsPageObject extends WebElementsInteractions {

    WebDriver driver;

    private final By getTitleOfProductPage = By.xpath("//span[contains(text(), 'Products')]");

    private final By getTextOfFirstItem = By.xpath("//a[@id='item_4_title_link']/div");


    public ProductsPageObject(WebDriver driver)
    {
        super(driver);
    }
    public String getTitleOfPage()
    {
        return retrieveText(getTitleOfProductPage);
    }

    public String getItemName(){
        return retrieveText(getTextOfFirstItem);
    }
}
