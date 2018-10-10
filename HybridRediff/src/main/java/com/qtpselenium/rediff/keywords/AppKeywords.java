package com.qtpselenium.rediff.keywords;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.Status;

public class AppKeywords extends GenericKeywords {

	public void login() {
		test.log(Status.INFO, "Logging In");
		
		getObject("moneyLink_css").click();
		getObject("signIn_css").click();
		String username = "";
		String password = "";
		if (data.get("Username") == null) {
			username = envProp.getProperty("defaultUsername");
			password = envProp.getProperty("defaultPassword");
		} else {
			username = data.get("Username");
			password = data.get("Password");
		}
		getObject("userEmail_xpath").sendKeys(username);
		getObject("continue_css").click();
		getObject("userPassword_id").sendKeys(password);
		getObject("logInSubmit_css").click();
		acceptAlert();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("myPortFolio_xpath")));

	}

	public void validateLogin() {
		test.log(Status.INFO, "Validating Login Test");
		boolean result = isElementPresent("myPortFolio_xpath");
		String expectedResult = data.get("ExpectedResult");
		String actualResult = "";
		if (result) {
			actualResult = "LoginSuccess";
			test.log(Status.PASS, "Login Successful");
		} else {
			actualResult = "LoginFailure";
			test.log(Status.FAIL, "Login Failed");
		}
		if (!expectedResult.equals(actualResult))
			reportFailure("Got Result as " + actualResult);
	}

	public void defaultLogin() {
		String username = envProp.getProperty("adminusername");
		String passsword = envProp.getProperty("adminpassword");
		System.out.println(username + "---" + passsword);
	}

	public void verifyPortFolio() {
		test.log(Status.INFO, "Verifying PortFolio Name is " + data.get(dataKey));
		waitForPageToLoad();
		waitTillSelectionToBe(objectKey, data.get(dataKey));
		waitForPageToLoad();

	}

	public void addStock() {
		test.log(Status.INFO, "Adding Stock");
		click("addSrock_xpath");
		type("stockName_xpath", "Stock Name");
		click("firstSelectedOption_xpath");
		click("stockPurchaseDate_xpath");
		System.out.println(data.get("Date Of Purchase"));
		selectDate(data.get("Date Of Purchase"));

		type("addQuantity_id", "Quantity");
		type("addPurchasePrice_id", "Purchase Price");
		click("addStuckButton_id");
		waitForPageToLoad();
		test.log(Status.INFO, "Stock Added and Successfully Submitted!");
		test.log(Status.INFO, "Validating Company in Table");

		int rowNum = getRowWithCellData(data.get("Stock Name"));
		System.out.println("Row Number of given company name from the table is -- " + rowNum);
		if (rowNum == -1)
			reportFailure("Could not find the stock - "+data.get("Stock Name"));
	}

	public void deleteStock() {
		test.log(Status.INFO, "Deleting Stock");
		waitForPageToLoad();
		int rowNum = getRowWithCellData(data.get("Stock Name"));
		if (rowNum == -1)
			reportFailure("Stock not found in list " + data.get("Stock Name"));
		driver.findElement(By.xpath("//table[@id='stock']/tbody/tr[" + rowNum + "]/td[1]")).click();
		driver.findElements(By.xpath("//input[@name='Delete']")).get(rowNum - 1).click();

		driver.switchTo().alert().accept();
		waitForPageToLoad();
		driver.switchTo().defaultContent();
		rowNum = getRowWithCellData(data.get("Stock Name"));
		System.out.println(rowNum);
		//reportFailure("Stock found after deletion " + data.get("Stock Name"));
		softAssert.assertEquals(rowNum, -1);

	}

	public void selectDate(String d) {
		Date current = new Date();
		System.out.println("Current date -- " + current);
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date selected = sd.parse(d);
			String day = new SimpleDateFormat("d").format(selected);
			String month = new SimpleDateFormat("MMMM").format(selected);
			String year = new SimpleDateFormat("yyyy").format(selected);
			System.out.println(day + " " + month + " " + year);
			String desiredMonthYear = month + " " + year;
			System.out.println("Month Year foramt -- " + desiredMonthYear);
			while (true) {
				String displayedMonthYear = driver.findElement(By.cssSelector(".dpTitleText")).getText();
				// System.out.println("Dispalyed Month Year -- "+displayedMonthYear);
				if (desiredMonthYear.equals(displayedMonthYear)) {
					// click date;
					driver.findElement(By.xpath("//td[text()='" + day + "']")).click();
					break;
				} else {
					if (selected.compareTo(current) > 0) {
						driver.findElement(
								By.cssSelector("#datepicker>table>tbody>tr.dpTitleTR>td:nth-child(4)>button")).click();
					} else if (selected.compareTo(current) < 0) {
						driver.findElement(
								By.cssSelector("#datepicker>table>tbody>tr.dpTitleTR>td:nth-child(2)>button")).click();
					}
				}
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getRowWithCellData(String data) {
		List<WebElement> rows = driver.findElements(By.cssSelector("#stock>tbody>tr"));
		for (int rNum = 0; rNum < rows.size(); rNum++) {
			WebElement row = rows.get(rNum);
			List<WebElement> cells = row.findElements(By.tagName("td"));
			for (int cNum = 0; cNum < cells.size(); cNum++) {
				WebElement cell = cells.get(cNum);
				if (!cell.getText().trim().equals("") && data.contains(cell.getText()))
					return ++rNum;
			}
		}
		return -1;
	}

}
