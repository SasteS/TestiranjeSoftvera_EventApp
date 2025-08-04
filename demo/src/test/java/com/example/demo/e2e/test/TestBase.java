package com.example.demo.e2e.test;
//
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.testng.annotations.AfterSuite;
//import org.testng.annotations.BeforeSuite;
//
//import java.util.concurrent.TimeUnit;
//
//public class TestBase {
//    public static WebDriver driver;
//
//    @BeforeSuite
//    public void initializeWebDriver() {
//        System.setProperty("webdriver.chrome.driver", "chromedriver.exe"); // demo/chromedriver.exe
//        driver = new ChromeDriver();
//
//        driver.manage().window().maximize();
//
//        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
//    }
//
//    @AfterSuite
//    public void quitDriver() {
//        driver.quit();
//    }
//}
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;

import java.util.concurrent.TimeUnit;

public class TestBase {
    public static WebDriver driver;

    @BeforeSuite
    public void initializeWebDriver() {
        // This will automatically download and set the right ChromeDriver
        WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
    }

    @AfterSuite
    public void quitDriver() {
        driver.quit();
    }
}
