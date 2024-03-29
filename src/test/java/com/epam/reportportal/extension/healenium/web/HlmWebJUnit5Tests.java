package com.epam.reportportal.extension.healenium.web;

import com.epam.healenium.SelfHealingDriver;
import com.epam.reportportal.junit5.ReportPortalExtension;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(ReportPortalExtension.class)
public class HlmWebJUnit5Tests {

    private static final Logger LOGGER = LoggerFactory.getLogger(HlmWebJUnit5Tests.class);

    private static final String url = "https://elenastepuro.github.io/test_env/index.html";

    static WebDriver driver;

    @BeforeAll
    static public void setUp() {
        WebDriverManager.firefoxdriver().setup();
        SafariOptions options = new SafariOptions();
        WebDriver delegate = new SafariDriver(options);
        driver = SelfHealingDriver.create(delegate);
    }

    @Test
    void logTest() {
        driver.get(url);
        driver.findElement(By.xpath("//*[@id='change:name']"));
        driver.findElement(By.id("Submit")).click();
        // healing
        driver.findElement(By.xpath("//*[@id='change:name']"));
    }

    @AfterAll
    static public void afterAll() {
        if (driver != null) {
            driver.quit();
        }
    }

}
