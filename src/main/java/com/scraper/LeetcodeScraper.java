package com.scraper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class LeetcodeScraper {

    private final String loginPage = "http://leetcode.com/accounts/login/";
    private final int timeout = 2;

    private String username;
    private String password;
    private WebDriver webDriver;

    void start() {
        setUpWebDriver();
        getUserDetails();
        login();
        selectSolvedFromDropdown();
        selectAllRowsFromDropdown();
        parseSolvedExercises();
    }

    private void setUpWebDriver() {
        System.setProperty("webdriver.chrome.driver",
                           "src/main/resources/drivers/chrome/chromedriver.exe");
        webDriver = new ChromeDriver();
    }

    private void getUserDetails() {
        System.out.println("Username: ");
        username = new Scanner(System.in).nextLine();
        System.out.println("Password: ");
        password = new Scanner(System.in).nextLine();
        //Arrays.toString(System.console().readPassword());
    }

    private void login() {
        webDriver.get(loginPage);
        webDriver.findElement(By.id("id_login")).sendKeys(username);
        webDriver.findElement(By.id("id_password")).sendKeys(password);
        webDriver.findElement(By.id("id_remember")).click();
        webDriver.findElement(By.id("id_password")).submit();
    }

    private void selectSolvedFromDropdown() {
        String dropdownFilterXpath =
                "//*[@id=\"question-app\"]/div/div[2]/form/div[1]/div/select";
        webDriver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
        Select dropdownFilter = new Select(webDriver.findElement(By.xpath(dropdownFilterXpath)));
        dropdownFilter.selectByValue("solved");
    }

    private void selectAllRowsFromDropdown() {
        String dropdownRowsPerPageXpath =
                "//*[@id=\"question-app\"]/div/div[2]/div/table/tbody[2]/tr/td/span/select";
        webDriver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
        Select dropdownRowsPerPage = new Select(webDriver.findElement(By.xpath(dropdownRowsPerPageXpath)));
        dropdownRowsPerPage.selectByVisibleText("all");
    }

    private void parseSolvedExercises() {
        WebElement table = webDriver.findElement(By.className("reactable-data"));
        List<WebElement> tableRows = table.findElements(By.tagName("tr"));
        for (WebElement row : tableRows) {
            String exerciseHref = row.findElement(By.tagName("a")).getAttribute("href");
            List<Code> code = getAcceptedSolutionsFromExercise(exerciseHref);
        }
    }

    private List<Code> getAcceptedSolutionsFromExercise(String exerciseHref) {
        List<Code> ret = new ArrayList<>();

        webDriver.get(exerciseHref + "?tab=Submission");
        WebElement table =
                webDriver.findElement(By.xpath("/html/body/div/div[4]/div/div/div[2]/table"));
        List<WebElement> tableRows = table.findElements(By.tagName("tr"));

        for (WebElement row : tableRows) {
            String statusClass = row.findElement(By.tagName("a")).getAttribute("class");
            if ("text-success status-accepted".equals(statusClass)) {
                String submissionHref = row.findElement(By.tagName("a")).getAttribute("href");

                List<WebElement> tdList = row.findElements(By.tagName("td"));
                String language = tdList.get(tdList.size() - 1).getText();

                ret.add(new Code(scrapeCode(submissionHref), language));
            }
        }
        return ret;
    }

    private String scrapeCode(String submissionHref) {
        return null;
    }
}
