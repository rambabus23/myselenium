package com.qtpselenium.rediff.driver;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Properties;


import com.aventstack.extentreports.ExtentTest;
import com.qtpselenium.rediff.keywords.AppKeywords;
import com.qtpselenium.rediff.utils.Constants;
import com.qtpselenium.rediff.utils.Xls_Reader;

public class DriverScript {
	public Properties envProp;
	public Properties prop;
	public ExtentTest test;
	AppKeywords app;

	public void executeKeywords(String testName, Xls_Reader xls, Hashtable<String, String> testData) throws Exception {
		int rows = xls.getRowCount(Constants.KEYWORDS_SHEET);
		app = new AppKeywords();
		app.setEnvProp(envProp);
		app.setProp(prop);
		app.setData(testData);
		app.setExtentTest(test);
		for (int rNum = 2; rNum <= rows; rNum++) {
			String TCID = xls.getCellData(Constants.KEYWORDS_SHEET, Constants.TCID_COL, rNum);
			if (TCID.equalsIgnoreCase(testName)) {
				String keyword = xls.getCellData(Constants.KEYWORDS_SHEET, Constants.KEYWORD_COL, rNum);
				String objectKey = xls.getCellData(Constants.KEYWORDS_SHEET, Constants.OBJECT_COL, rNum);
				String dataKey = xls.getCellData(Constants.KEYWORDS_SHEET, Constants.DATA_COL, rNum);
				String proceedOnFail = xls.getCellData(Constants.KEYWORDS_SHEET, Constants.PROCEED_COL, rNum);
				String data = testData.get(dataKey);

				// System.out.println(TCID+"---"+keyword+"---"+prop.getProperty(objectKey)+"---"+data);
				// test.log(Status.INFO,
				// TCID+"---"+keyword+"---"+prop.getProperty(objectKey)+"---"+data);
				app.setDataKey(dataKey);
				app.setObjectKey(objectKey);
				app.setProceedOnFail(proceedOnFail);
				// Reflection API
				Method method;
				method = app.getClass().getMethod(keyword);
				method.invoke(app);

			}
		}
		app.softAssert();

	}

	public Properties getEnvProp() {
		return envProp;
	}

	public void setEnvProp(Properties envProp) {
		this.envProp = envProp;
	}

	public Properties getProp() {
		return prop;
	}

	public void setProp(Properties prop) {
		this.prop = prop;
	}

	public void setExtentTest(ExtentTest test) {
		this.test = test;
	}

	public void quit() {
		if (app != null)
			app.quit();
	}

}
