import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class StockKeeperChatTest {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\amanm_679qu5f\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");

        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        try {
            driver.get("http://localhost:8080/mavenproject2-1.0-SNAPSHOT/login.html");

            // Declare WebDriverWait early
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Wait for username field
            WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
            WebElement password = driver.findElement(By.id("password"));

            username.sendKeys("kisholi");
            password.sendKeys("123");

            WebElement loginBtn = driver.findElement(By.tagName("button"));
            loginBtn.click();

WebElement chatBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
    By.xpath("//div[@class='nav-item' and contains(text(),'💬 Team Chat')]")
));
chatBtn.click();


            // Message input
WebElement messageInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("chatInput")));
messageInput.sendKeys("Hello Admin! This is Stock Keeper ");

WebElement sendBtn = driver.findElement(By.xpath("//button[contains(text(),'Send')]"));
sendBtn.click();

// Chat messages container
WebElement chatBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("chatMessages")));
if (chatBox.getText().contains("Hello Admin!")) {
    System.out.println("Chat message sent and displayed successfully.");
} else {
    System.out.println("Chat message failed to display.");
}


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
