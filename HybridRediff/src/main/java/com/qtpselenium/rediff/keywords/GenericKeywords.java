package com.qtpselenium.rediff.keywords;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.qtpselenium.rediff.reports.ExtentManager;

public class GenericKeywords {
	public Properties envProp;
	public Properties prop;
	public String objectKey;

	public String dataKey;
	public String ProceedOnFail;
	public Hashtable<String, String> data;
	public WebDriver driver;
	public ExtentTest test;
	public SoftAssert softAssert = new SoftAssert();

	/****************** Setter Functions *******************/
	public void setEnvProp(Properties envProp) {
		this.envProp = envProp;
	}

	public void setProp(Properties prop) {
		this.prop = prop;
	}

	public void setObjectKey(String objectKey) {
		this.objectKey = objectKey;
	}

	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}

	public void setData(Hashtable<String, String> data) {
		this.data = data;
	}

	public void setExtentTest(ExtentTest test) {
		this.test = test;
	}

	public String getProceedOnFail() {
		return ProceedOnFail;
	}

	public void setProceedOnFail(String proceedOnFail) {
		ProceedOnFail = proceedOnFail;
	}

	/******************************************************/

	public void openBrowser() {
		String browser = data.get(dataKey);
		test.log(Status.INFO, "Opening Browser " + browser);

		// To Run on GRID

		if (prop.getProperty("gridRun").equals("Y")) {
			// run on grid

			DesiredCapabilities cap = null;
			if (browser.equals("Mozilla")) {
				cap = DesiredCapabilities.firefox();
				cap.setJavascriptEnabled(true);
				cap.setPlatform(Platform.WINDOWS);
			} else if (browser.equals("Chrome")) {
				cap = DesiredCapabilities.chrome();
				cap.setJavascriptEnabled(true);
				cap.setPlatform(Platform.WINDOWS);
			}

			try {
				driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), cap);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else { // Not Run on GRID

			if (browser.equals("Mozilla")) {
				System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,
						"D:\\RAM\\Files\\BrowserLogs\\Firefox.log");
				FirefoxOptions options = new FirefoxOptions();
				options.addPreference("dom.browsernotifications.enabled", false);
				driver = new FirefoxDriver(options);
			} else if (browser.equals("Chrome")) {
				System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY,
						"D:\\RAM\\Files\\BrowserLogs\\Chrome.log");

				ChromeOptions options = new ChromeOptions();
				options.addArguments("disable-infobars");
				options.addArguments("--diable-notifications");
				driver = new ChromeDriver(options);
			} else if (browser.equals("IE")) {
				System.setProperty(InternetExplorerDriverService.IE_DRIVER_LOGFILE_PROPERTY,
						"D:\\RAM\\Files\\BrowserLogs\\IE.log");
				driver = new InternetExplorerDriver();
			} else if (browser.equals("Edge")) {
				System.setProperty(EdgeDriverService.EDGE_DRIVER_LOG_PROPERTY, "D:\\RAM\\Files\\BrowserLogs\\Edge.log");
				driver = new EdgeDriver();
			}

			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			driver.manage().window().maximize();
		}
	}

	public void navigate() {
		test.log(Status.INFO, "Navigating to Website " + envProp.getProperty(objectKey));
		driver.get(envProp.getProperty(objectKey));
	}

	public void click() {
		test.log(Status.INFO, "Clicking " + prop.getProperty(objectKey));
		getObject(objectKey).click();
	}

	public void click(String objectKey) {
		setObjectKey(objectKey);
		click();
	}

	public void type() {
		test.log(Status.INFO, "Typing in " + prop.getProperty(objectKey));
		getObject(objectKey).sendKeys(data.get(dataKey));

	}

	public void type(String objectKey, String dataKey) {
		setObjectKey(objectKey);
		setDataKey(dataKey);
		type();
	}

	public void clear() {
		test.log(Status.INFO, "Clearing " + objectKey);
		getObject(objectKey).clear();

	}

	public void acceptAlert() {
		test.log(Status.INFO, "Switching to Alert");
		try {
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.alertIsPresent());
			driver.switchTo().alert().accept();
			driver.switchTo().defaultContent();
			test.log(Status.INFO, "Alert Accepted Successfully");
		} catch (Exception e) {
			if (objectKey.equals("Y"))
				test.log(Status.INFO, "Alert Not Present");
		}

	}

	public void select() {
		test.log(Status.INFO, "Selecing From " + prop.getProperty(objectKey) + " . Data - " + data.get(dataKey));
		List<WebElement> options = new Select(getObject(objectKey)).getOptions();
		for (int i = 0; i < options.size(); i++) {
			if (options.get(i).getText().equals(data.get(dataKey)))
				break;
			if (i == options.size() - 1)
				reportFailure("Option Not Found in Dropdown " + data.get(dataKey));
		}

		new Select(getObject(objectKey)).selectByVisibleText(data.get(dataKey));
	}

	public void validateTitle() {
		test.log(Status.INFO, "Validating Title " + prop.getProperty(objectKey));
		String expectedTitle = prop.getProperty(objectKey);
		String actualTitle = driver.getTitle();
		if (!expectedTitle.equals(actualTitle))
			// Report Failure
			reportFailure("Title didn' match. Got Title as " + actualTitle);
	}

	public void validateElementPresent() {
		if (!isElementPresent(objectKey)) {
			// report failure
			reportFailure("Element not found " + objectKey);
		}
	}

	public void validateElementInList() {
		test.log(Status.INFO, "Validating Element in List");
		waitForPageToLoad();
		if (isElementInList())
			reportFailure("Could Not Deleted Option");
	}

	public void waitForPageToLoad() {
		// Page load sttaus
		// Ajax/JQuery status
		JavascriptExecutor js = (JavascriptExecutor) driver;
		// Check for javascript status
		int i = 0;
		while (i != 10) {
			String status = (String) js.executeScript("return document.readyState;");
			// System.out.println(status);
			if (status.equals("complete"))
				break;
			else
				wait(1);
			i++;
		}

		// Check for JQuery status
		i = 0;
		while (i != 10) {
			Long d = (Long) js.executeScript("return jQuery.active;");
			System.out.println(d);
			if (d.longValue() == 0)
				break;
			else
				wait(1);
			i++;
		}

	}

	public void wait(int time) {
		try {
			Thread.sleep(time * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void waitTillSelectionToBe(String obejctKey, String expected) {

		int i = 0;
		String actual = "";
		while (i != 10) {
			WebElement e = getObject(obejctKey);
			Select s = new Select(e);
			actual = s.getFirstSelectedOption().getText();
			// System.out.println(actual);
			if (actual.equals(expected))
				return;
			else
				wait(1);
		}

		reportFailure("Values don't match. Got value as " + obejctKey);

	}

	public void quit() {
		if (driver != null)
			driver.quit();
	}

	/************************* Utility Functions *************************/
	public WebElement getObject(String objectKey) {
		WebElement e = null;
		try {
			if (objectKey.endsWith("_xpath"))
				e = driver.findElement(By.xpath(prop.getProperty(objectKey)));
			else if (objectKey.endsWith("_css"))
				e = driver.findElement(By.cssSelector(prop.getProperty(objectKey)));
			else if (objectKey.endsWith("_id"))
				e = driver.findElement(By.id(prop.getProperty(objectKey)));
			else if (objectKey.endsWith("_name"))
				e = driver.findElement(By.name(prop.getProperty(objectKey)));
			WebDriverWait wait = new WebDriverWait(driver, 20);
			wait.until(ExpectedConditions.visibilityOf(e));
			wait.until(ExpectedConditions.elementToBeClickable(e));
		} catch (Exception ex) {
			// Failure - Report that failure
			reportFailure("Object not found " + objectKey);
		}

		return e;

	}

	public boolean isElementPresent(String objectKey) {
		List<WebElement> list = null;
		if (objectKey.endsWith("_xpath"))
			list = driver.findElements(By.xpath(prop.getProperty(objectKey)));
		else if (objectKey.endsWith("_css"))
			list = driver.findElements(By.cssSelector(prop.getProperty(objectKey)));
		else if (objectKey.endsWith("_id"))
			list = driver.findElements(By.id(prop.getProperty(objectKey)));
		else if (objectKey.endsWith("_name"))
			list = driver.findElements(By.name(prop.getProperty(objectKey)));

		if (list.size() == 0)
			return false;
		else
			return true;
	}

	public boolean isElementInList() {
		List<WebElement> options = new Select(getObject(objectKey)).getOptions();
		for (int i = 0; i < options.size(); i++) {
			if (options.get(i).getText().equals(data.get(dataKey)))
				;
			return true;
		}

		return false;
	}

	/******************* Reporting Functions ************************/

	public void reportFailure(String failureMsg) {
		test.log(Status.FAIL, failureMsg);
		takeScreenshot();
		if (ProceedOnFail.equals("Y"))
			softAssert.fail(failureMsg);
		else {
			softAssert.fail(failureMsg);
			softAssert.assertAll();
		}

		// Assert.fail();
	}

	public void takeScreenshot() {
		Date d = new Date();
		String screenShotFile = d.toString().replaceAll(":", "_").replaceAll(" ", "_") + ".png";
		File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(srcFile, new File(ExtentManager.screenshotFolderPath + screenShotFile));
			test.log(Status.FAIL, "ScreenShot --> "
					+ test.addScreenCaptureFromPath(ExtentManager.screenshotFolderPath + screenShotFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void softAssert() {
		softAssert.assertAll();
	}

}
