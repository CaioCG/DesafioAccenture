package br.com.accenture.qa.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public final class DriverFactory {
    private DriverFactory() {
    }

    public static WebDriver createChrome() {
        ChromeOptions options = new ChromeOptions();
        if (Boolean.parseBoolean(System.getProperty("headless", "true"))) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1440,1200", "--disable-notifications");

        ChromeDriver driver = new ChromeDriver(options);
        driver.executeCdpCommand("Network.enable", Map.of());
        driver.executeCdpCommand("Network.setBlockedURLs", Map.of("urls", List.of(
                "*googlesyndication.com/*",
                "*doubleclick.net/*",
                "*googleadservices.com/*",
                "*googletagservices.com/*",
                "*adservice.google.com/*")));
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        return driver;
    }
}
