package com.qtpselenium.rediff.portfoliosuite;

import java.util.Hashtable;

import org.testng.SkipException;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.qtpselenium.rediff.base.BaseTest;
import com.qtpselenium.rediff.utils.Constants;
import com.qtpselenium.rediff.utils.DataUtil;

public class PortFolioTest extends BaseTest {

	@Test(priority = 1, dataProvider = "getData")
	public void createPortFolioTest(Hashtable<String, String> data) throws Exception {
		test.log(Status.INFO, "Starting Login Test");
		if (DataUtil.isSkip(testName, xls) || data.get(Constants.RUNMODE_COL).equals(Constants.RUNMODE_NO)) {
			test.log(Status.SKIP, "Skipping Test as Runmode Set to NO");
			throw new SkipException("Skipping Test as Runmode Set to NO");
		}

		ds.executeKeywords(testName, xls, data);
	}

	@Test(priority = 2, dependsOnMethods = { "createPortFolioTest" }, dataProvider = "getData")
	public void deletePortFolioTest(Hashtable<String, String> data) throws Exception {
		test.log(Status.INFO, "Starting Delete PortFolioTest");
		if (DataUtil.isSkip(testName, xls) || data.get(Constants.RUNMODE_COL).equals(Constants.RUNMODE_NO)) {
			test.log(Status.SKIP, "Skipping Test as Runmode Set to NO");
			throw new SkipException("Skipping Test as Runmode Set to NO");
		}

		ds.executeKeywords(testName, xls, data);
	}

}
