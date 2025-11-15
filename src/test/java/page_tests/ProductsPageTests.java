package page_tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;
import page_objects.LoginPageObject;
import page_objects.ProductsPageObject;

public class ProductsPageTests extends BaseTest {

    private static final Logger logger = LogManager.getLogger(ProductsPageTests.class);
    LoginPageObject loginPageObject;
    ProductsPageObject productsPageObject;

    @Test
    public void testItemName(){
        logger.info("*** Products page - Logger Test - test Item name ***");
        loginPageObject = new LoginPageObject(driver);
        productsPageObject = loginPageObject.userLogin("standard_user", "secret_sauce");
        System.out.println(productsPageObject.getItemName());
    }
}
