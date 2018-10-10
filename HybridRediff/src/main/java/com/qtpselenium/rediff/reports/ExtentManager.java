package com.qtpselenium.rediff.reports;

import java.io.File;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {

	private static ExtentReports extent;
	public static String screenshotFolderPath;

	public static ExtentReports getInstance(String reportPath) {
		if (extent == null) {
			Date d = new Date();
			String folderName = d.toString().replaceAll(" ", "_").replaceAll(":", "_");
			String reportName = d.toString().replaceAll(" ", "_").replaceAll(":", "_") + ".html";
			new File(reportPath + folderName + "//Screenshots").mkdirs();
			screenshotFolderPath = reportPath + folderName + "//Screenshots//";
			reportPath = reportPath + folderName + "//";
			// System.out.println(reportPath+reportName);
			createInstance(reportPath + reportName);
		}
		return extent;
	}

	public static ExtentReports createInstance(String fileName) {
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(fileName);
		htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
		htmlReporter.config().setChartVisibilityOnOpen(true);
		htmlReporter.config().setTheme(Theme.DARK);
		htmlReporter.config().setDocumentTitle("Reports");
		htmlReporter.config().setEncoding("utf-8");
		htmlReporter.config().setReportName("Reports - Automation Testing");

		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);

		return extent;
	}

}
