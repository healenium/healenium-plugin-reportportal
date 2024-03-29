package com.epam.reportportal.extension.healenium.proxy;

import com.epam.reportportal.junit5.ReportPortalExtension;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.input.Tailer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

@ExtendWith(ReportPortalExtension.class)
public class HlmProxyJUnit5Tests {

	private static final Logger LOGGER = LoggerFactory.getLogger(HlmProxyJUnit5Tests.class);
	private static final String url = "https://elenastepuro.github.io/test_env/index.html";

	private static WebDriver driver;

	@BeforeAll
	static public void setUp() throws MalformedURLException {
		setupProxyLogs();
		WebDriverManager.firefoxdriver().setup();
		FirefoxOptions options = new FirefoxOptions();
		driver = new RemoteWebDriver(new URL("http://localhost:8085"), options);
	}

	@Test
	void logTest() {
		driver.get(url);
		driver.findElement(By.cssSelector("#change_id"));
		driver.findElement(By.id("Submit")).click();
		// healing
		driver.findElement(By.cssSelector("#change_id"));
	}

	@AfterAll
	static public void afterAll() {
		if (driver != null) {
			driver.quit();
		}
	}

	private static void setupProxyLogs() {
		File logFile = new File("healenium/logs/healenium-proxy.log");
		Tailer tailer = new Tailer(logFile, new LogTailer(), 1000, true);
		Thread thread = new Thread(tailer);
		thread.start();
	}

}
