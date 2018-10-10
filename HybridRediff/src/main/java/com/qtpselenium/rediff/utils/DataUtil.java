package com.qtpselenium.rediff.utils;

import java.util.Hashtable;

public class DataUtil {

	public static Object[][] getTestData(String testName, Xls_Reader xls) {
		int testStartRowNum = 1;
		while (!xls.getCellData(Constants.DATA_SHEET, 0, testStartRowNum).equalsIgnoreCase(testName)) {
			testStartRowNum++;
		}
		System.out.println("Test Name found at Row is " + testStartRowNum);
		// Find Total Rows
		int colStartRowNumber = testStartRowNum + 1;
		int totalCols = 0;
		while (!xls.getCellData(Constants.DATA_SHEET, totalCols, colStartRowNumber).equals("")) {
			totalCols++;
		}
		System.out.println("Total columns are " + totalCols);
		// Find total Rows
		int dataStartRowNum = testStartRowNum + 2;
		int totalRows = 0;
		while (!xls.getCellData(Constants.DATA_SHEET, 0, dataStartRowNum).equals("")) {
			dataStartRowNum++;
			totalRows++;
		}
		System.out.println("Total Rows are " + totalRows);
		dataStartRowNum = testStartRowNum + 2;
		int finalRows = dataStartRowNum + totalRows;
		Object[][] myDaya = new Object[totalRows][1];
		Hashtable<String, String> table = null;
		int i = 0;
		for (int rNum = dataStartRowNum; rNum < finalRows; rNum++) {
			table = new Hashtable<String, String>();
			for (int cNum = 0; cNum < totalCols; cNum++) {
				String data = xls.getCellData(Constants.DATA_SHEET, cNum, rNum);
				String key = xls.getCellData(Constants.DATA_SHEET, cNum, colStartRowNumber);
				// System.out.println(key+"--"+data);
				table.put(key, data);
			}
			// System.out.println(table);
			myDaya[i][0] = table;
			i++;
			// System.out.println("-------");
		}
		return myDaya;
	}

	public static boolean isSkip(String testName, Xls_Reader xls) {
		int rows = xls.getRowCount(Constants.TESTCASES_SHEET);
		for (int rNum = 2; rNum <= rows; rNum++) {
			String tcid = xls.getCellData(Constants.TESTCASES_SHEET, Constants.TCID_COL, rNum);
			if (tcid.equalsIgnoreCase(testName)) {
				String runmode = xls.getCellData(Constants.TESTCASES_SHEET, Constants.RUNMODE_COL, rNum);
				if (runmode.equals(Constants.RUNMODE_NO))
					return true;
				else
					return false;
			}
		}
		return true;
	}
}
