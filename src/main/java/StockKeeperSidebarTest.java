import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class StockKeeperSidebarTest {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\amanm_679qu5f\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        try {
            driver.get("http://localhost:8080/mavenproject2-1.0-SNAPSHOT/login.html");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            
            // Login as Stock Keeper
            WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
            WebElement password = driver.findElement(By.id("password"));
            username.sendKeys("kisholi");
            password.sendKeys("123");
            driver.findElement(By.tagName("button")).click();

            // Wait for dashboard to load
            Thread.sleep(3000); 
            wait.until(ExpectedConditions.urlContains("dashboard"));

            // Check each sidebar section
            String[] sectionIds = {"analyticsBtn", "manageItemsBtn", "chatBtn", "logoutBtn"};
            for (String id : sectionIds) {
                WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
                if (element.isDisplayed()) {
                    System.out.println(id + " is visible.");
                } else {
                    System.out.println(id + " is NOT visible!");
                }
            }

            System.out.println("✅ Stock Keeper Sidebar Test Passed!");

        } catch (Exception e) {
            System.out.println("❌ Test Failed!");
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
