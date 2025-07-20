import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class LoginTest {

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\amanm_679qu5f\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");

        String[][] users = {
            {"admin", "admin123", "dashboard.html"},
            {"kisholi", "123", "stockkeeper-dashboard.html"},
            {"sara", "123", "cashier-dashboard.html"}
        };

        for (String[] user : users) {
            String usernameValue = user[0];
            String passwordValue = user[1];
            String expectedUrlPart = user[2];

            WebDriver driver = new ChromeDriver();
            try {
                driver.get("http://localhost:8080/mavenproject2-1.0-SNAPSHOT/login.html");

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
                WebElement password = driver.findElement(By.id("password"));

                username.clear();
                password.clear();

                username.sendKeys(usernameValue);
                password.sendKeys(passwordValue);

                WebElement loginButton = driver.findElement(By.tagName("button"));
                loginButton.click();

                Thread.sleep(5000);  // wait for redirect

                String currentUrl = driver.getCurrentUrl();

                if (currentUrl.contains(expectedUrlPart)) {
                    System.out.println(usernameValue + " login test passed!");
                } else {
                    System.out.println(usernameValue + " login test failed! URL: " + currentUrl);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                driver.quit();
            }
        }
    }
}
