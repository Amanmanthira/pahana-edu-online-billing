import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class AdminDashboardFullTest {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\amanm_679qu5f\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Open login page
            driver.get("http://localhost:8080/mavenproject2-1.0-SNAPSHOT/login.html");

            // Login as admin
            WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
            WebElement password = driver.findElement(By.id("password"));
            username.sendKeys("admin");
            password.sendKeys("admin123");

            WebElement loginBtn = driver.findElement(By.tagName("button"));
            loginBtn.click();

            // Wait for dashboard URL or an element unique to dashboard
            wait.until(ExpectedConditions.urlContains("dashboard"));

            System.out.println("✅ Admin Login Successful");

            // Nav text, section id, expected heading (for validation)
            String[][] sections = {
                {"📊 Analytics", "analytics", "Admin Dashboard Analytics"},
                {"👥 Customers", "customers", "Customer Management"},
                {"👤 Users", "users", "Manage Users"},
                {"📦 Items", "items", "Item Management"},
                {"? Generate Bill", "bill", "Generate Bill"},  
                {"💳 Bill History", "history", "Bill History"},
                {"🏦 Bank Account", "bank", "Shop Account Overview"},
                {"💬 Team Chat", "teamchat", "Team Chat"},
                {"❓ Help", "help", "Help Section"}
            };

            for (String[] section : sections) {
                String navText = section[0];
                String id = section[1];
                String expectedHeading = section[2];

                // Click the nav item by its text
                WebElement navItem = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'nav-item') and contains(text(),'" + navText + "')]")));
                navItem.click();

                // Wait until the section div with id is visible
                WebElement sectionDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));

                // Check headings (h1 or h2) inside that section
                List<WebElement> headings = sectionDiv.findElements(By.xpath(".//h1 | .//h2"));
                boolean found = false;
                for (WebElement h : headings) {
                    if (h.getText().toLowerCase().contains(expectedHeading.toLowerCase().replaceAll("[^a-zA-Z ]", "").trim())) {
                        found = true;
                        break;
                    }
                }

                if (found) {
                    System.out.println("✅ Section '" + id + "' loaded with expected heading.");
                } else {
                    System.out.println("❌ Section '" + id + "' heading NOT found!");
                }

                Thread.sleep(1000);  // small pause to let page settle
            }

            // Click logout button and wait for login page
            WebElement logoutBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Logout')]")));
            logoutBtn.click();

            wait.until(ExpectedConditions.urlContains("login"));
            System.out.println("👋 Logged Out Successfully");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
