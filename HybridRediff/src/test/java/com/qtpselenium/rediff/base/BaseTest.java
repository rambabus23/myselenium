package com.qtpselenium.rediff.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.qtpselenium.rediff.driver.DriverScript;
import com.qtpselenium.rediff.mail.ZipAndSendMail;
import com.qtpselenium.rediff.reports.ExtentManager;
import com.qtpselenium.rediff.utils.DataUtil;
import com.qtpselenium.rediff.utils.Xls_Reader;

public class BaseTest {
	public Properties envProp;
	public Properties prop;
	public Xls_Reader xls;
	public String testName;
	public DriverScript ds;
	public ExtentReports rep;
	public ExtentTest test;

	@BeforeTest
	public void init() {
		// System.out.println("Before Running Test");
		// init the test name
		testName = this.getClass().getSimpleName();
		//System.out.println("***" + testName);
		System.out.println("***" + this.getClass().getPackage().getName());
		String arr[] = this.getClass().getPackage().getName().split("\\.");
		String suiteName = arr[arr.length - 1];
		//System.out.println("Suite Name --> "+suiteName);
		// init properties
		prop = new Properties();
		envProp = new Properties();
		try {
			FileInputStream fs = new FileInputStream(
					System.getProperty("user.dir") + "\\src\\test\\resources\\env.properties");
			prop.load(fs);
			String env = prop.getProperty("env");
			fs = new FileInputStream(System.getProperty("user.dir") + "\\src\\test\\resources\\" + env + ".properties");
			envProp.load(fs);
			// System.out.println(envProp.getProperty("url"));

		} catch (IOException e) {
			e.printStackTrace();
		}

		// init xls obejct
		xls = new Xls_Reader(envProp.getProperty(suiteName + "_xls"));

		// init DS
		ds = new DriverScript();
		ds.setEnvProp(envProp);
		ds.setProp(prop);

	}

	@BeforeMethod
	public void initTest() {
		rep = ExtentManager.getInstance(prop.getProperty("reportPath"));
		test = rep.createTest(testName);
		ds.setExtentTest(test);
	}

	@AfterMethod
	public void quit() {
		if (ds != null) {
			ds.quit();
		}

		if (rep != null)
			rep.flush();
		
	}
	
	@DataProvider
	public Object[][] getData(Method method) {
		// System.out.println("In DataProvider");
		// xls = new Xls_Reader("D:\\RAM\\Files\\Data\\Hybrid Sept2018\\SuiteA.xlsx");
		testName = method.getName();
		System.out.println("Inside DataProvider - "+testName);
		
		return DataUtil.getTestData(testName, xls);
	}

}
