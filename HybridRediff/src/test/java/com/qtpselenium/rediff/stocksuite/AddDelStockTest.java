package com.qtpselenium.rediff.stocksuite;

import java.util.Hashtable;

import org.testng.SkipException;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.qtpselenium.rediff.base.BaseTest;
import com.qtpselenium.rediff.utils.Constants;
import com.qtpselenium.rediff.utils.DataUtil;

public class AddDelStockTest extends BaseTest {

	@Test(dataProvider = "getData")
	public void addStockTest(Hashtable<String, String> data) throws Exception {
		test.log(Status.INFO, "Starting " + testName);
		if (DataUtil.isSkip(testName, xls) || data.get(Constants.RUNMODE_COL).equals(Constants.RUNMODE_NO)) {
			test.log(Status.SKIP, "Skipping Test as Runmode Set to NO");
			throw new SkipException("Skipping Test as Runmode Set to NO");
		}
		System.out.println("Running " + testName);
		ds.executeKeywords(testName, xls, data);

	}

	@Test(priority = 2, dependsOnMethods = { "addStockTest" }, dataProvider = "getData")
	public void deleteStockTest(Hashtable<String, String> data) throws Exception {
		test.log(Status.INFO, "Starting " + testName);
		if (DataUtil.isSkip(testName, xls) || data.get(Constants.RUNMODE_COL).equals(Constants.RUNMODE_NO)) {
			test.log(Status.SKIP, "Skipping Test as Runmode Set to NO");
			throw new SkipException("Skipping Test as Runmode Set to NO");
		}
		System.out.println("Running " + testName);
		ds.executeKeywords(testName, xls, data);

	}

}
