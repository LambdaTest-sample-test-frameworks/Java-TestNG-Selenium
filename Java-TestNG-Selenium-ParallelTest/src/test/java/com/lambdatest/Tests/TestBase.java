package com.lambdatest.Tests;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.UnexpectedException;

public class TestBase {

	public String buildTag = System.getenv("LT_BUILD");
	public String username = System.getenv("LT_USERNAME");
	public String accesskey = System.getenv("LT_APIKEY");
	public  String gridURL = System.getenv("LT_GRID_URL");

	private ThreadLocal<WebDriver> webDriver = new ThreadLocal<WebDriver>();

	@DataProvider(name = "browsersDetails", parallel = true)
	public static Object[][] ltBrowserDataProvider(Method testMethod) {
		return new Object[][] { new Object[] { "MicrosoftEdge", "16.0", "WIN10" },
				new Object[] { "firefox", "63.0", "WIN10" }, new Object[] { "internet explorer", "11.0", "WIN7" },
				new Object[] { "chrome", "60.0", "OS X 10.11" }, new Object[] { "chrome", "70.0", "macOS 10.13" },
				new Object[] { "firefox", "64.0", "WIN7" }, };
	}

	public WebDriver getWebDriver() {
		return webDriver.get();
	}

	protected void setUp(String browser, String version, String os, String methodName)
			throws MalformedURLException, UnexpectedException {
		DesiredCapabilities capabilities = new DesiredCapabilities();

		// set desired capabilities to launch appropriate browser on Lambdatest
		capabilities.setCapability(CapabilityType.BROWSER_NAME, browser);
		capabilities.setCapability(CapabilityType.VERSION, version);
		capabilities.setCapability(CapabilityType.PLATFORM, os);
		capabilities.setCapability("name", methodName);

		if (buildTag == null) {
			capabilities.setCapability("build", "TestNG-Java-Parallel");
		}

		if (username == null)
			// Set you LAMBDATEST Username here if not Provided from Jenkins
			username = "nikhily";

		if (accesskey == null)
			// Set you LAMBDATEST AppKey here if not Provided from Jenkins
			accesskey = "3Z6PQHL1sUEW2nuvvPZxg7zom6y8ZqAvfPJBX5Ne2rWN3uwfGi";

		if (gridURL == null)
			gridURL = "https://" + username + ":" + accesskey + "@stage-hub.lambdatest.com/wd/hub";

		// Launch remote browser and set it as the current thread
		webDriver.set(new RemoteWebDriver(new URL(gridURL), capabilities));

	}

	@AfterMethod(alwaysRun = true)
	public void tearDown(ITestResult result) throws Exception {
		// ((JavascriptExecutor) webDriver.get()).executeScript("Lambdatest:job-result="
		// + (result.isSuccess() ? "passed" : "failed"));
		webDriver.get().quit();
	}
}
