package page_tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;
import page_objects.LoginPageObject;
import page_objects.ProductsPageObject;

public class LoginPageTests extends BaseTest {

    LoginPageObject loginPageObject;
    ProductsPageObject productsPageObject;
    private static final Logger logger = LogManager.getLogger(LoginPageTests.class);

    @Test
    public void userLoginTest(){
        String username = "standard_user";
        String password = "secret_sauce";

        loginPageObject = new LoginPageObject(driver);
        productsPageObject = loginPageObject.userLogin(username, password);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        logger.info("*** username is: *** " +username);
        System.out.println("Console Output is: "+ username);
        System.out.println(productsPageObject.getTitleOfPage());

    }

}
