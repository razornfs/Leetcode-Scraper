package com.scraper;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class LeetcodeScraper {

    private static final String loginPage = "http://leetcode.com/accounts/login/";
    private static String username;
    private static String password;
    private static final int timeout = 2;
    private static WebDriver webDriver;

    static {
        System.setProperty("webdriver.chrome.driver",
                           "src/main/resources/drivers/chrome/chromedriver.exe");
        webDriver = new ChromeDriver();
    }

    public static void main(String[] args) {
        getUserDetails(args);
        login();
        selectSolvedFromDropdown();
        selectAllRowsFromDropdown();

        // extract links from table
        WebElement table = webDriver.findElement(By.className("reactable-data"));
        List<WebElement> tableRows = table.findElements(By.tagName("tr"));
        for (WebElement tableRow : tableRows) {
            String href = tableRow.findElement(By.tagName("a")).getAttribute("href");
            String code = scrapeCode(href);
        }
    }

    private static void getUserDetails(String[] args) {
        if (args.length != 2) {
            System.out.println("Please provide username and password");
            System.exit(0);
        }
        username = args[0];
        password = args[1];
    }

    private static void login() {
        webDriver.get(loginPage);
        webDriver.findElement(By.id("id_login")).sendKeys(username);
        webDriver.findElement(By.id("id_password")).sendKeys(password);
        webDriver.findElement(By.id("id_remember")).click();
        webDriver.findElement(By.id("id_password")).submit();
    }

    private static void selectSolvedFromDropdown() {
        String dropdownFilterXpath =
                "//*[@id=\"question-app\"]/div/div[2]/form/div[1]/div/select";
        webDriver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
        Select dropdownFilter = new Select(webDriver.findElement(By.xpath(dropdownFilterXpath)));
        dropdownFilter.selectByValue("solved");
    }

    private static void selectAllRowsFromDropdown() {
        String dropdownRowsPerPageXpath =
                "//*[@id=\"question-app\"]/div/div[2]/div/table/tbody[2]/tr/td/span/select";
        webDriver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
        Select dropdownRowsPerPage = new Select(webDriver.findElement(By.xpath(dropdownRowsPerPageXpath)));
        dropdownRowsPerPage.selectByVisibleText("all");
    }

    private static String scrapeCode(String href) {
        //TODO
        webDriver.get(href + "?tab=Submission");
        return null;
    }
}
