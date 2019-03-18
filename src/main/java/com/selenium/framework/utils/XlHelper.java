package com.selenium.framework.utils;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.*;

import com.selenium.framework.core.Constants;

import static com.selenium.framework.core.TestDriver.APP_LOGS;

import java.io.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * This class to read data from excel file
 * 
 * @author vgeesidi
 *
 */
public class XlHelper {

	public String path;
	public FileInputStream fis = null;
	public FileOutputStream fileOut = null;
	private XSSFWorkbook workbook = null;
	private XSSFSheet sheet = null;
	private XSSFRow row = null;
	private XSSFCell cell = null;

	/**
	 * Parameterized constructor
	 * 
	 * @param path
	 *            Path
	 */
	public XlHelper(String path) {
		this.path = path;
		try {
			fis = new FileInputStream(path);
			workbook = new XSSFWorkbook(fis);
			sheet = workbook.getSheetAt(0);
			fis.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Verify main suite format
	 * 
	 * @return true or false
	 */
	public boolean verifyMainSuiteFormat() {
		if (!isSheetExist(Constants.TEST_SUITE_SHEET)) {
			APP_LOGS.error(Constants.TEST_SUITE_SHEET + " Sheet Not Found .. Please check if there any typos");
			return false;
		}
		row = sheet.getRow(0);
		if (!row.getCell(0).getStringCellValue().trim().equalsIgnoreCase(Constants.Test_Suite_ID)
				|| !row.getCell(1).getStringCellValue().trim().equalsIgnoreCase("Description")
				|| !row.getCell(2).getStringCellValue().trim().equalsIgnoreCase(Constants.RUNMODE)) {
			APP_LOGS.error("Column names not defined properly .. Please check if there any typos");
			return false;
		}
		return true;
	}

	/**
	 * Verify test suite format
	 * 
	 * @return true or false
	 */
	public boolean verifyTestSuiteFormat() {
		if (!isSheetExist(Constants.TEST_CASES_SHEET)) {
			APP_LOGS.error(Constants.TEST_CASES_SHEET + " Sheet Not Found .. Please check if there any typos");
			return false;
		}
		if (!isSheetExist(Constants.TEST_STEPS_SHEET)) {
			APP_LOGS.error(Constants.TEST_STEPS_SHEET + " Sheet Not Found .. Please check if there any typos");
			return false;
		}
		row = sheet.getRow(0);
		List<String> cols = getColumns();
		if (!cols.contains(Constants.TCID) || !cols.contains("USID") || !cols.contains("Description")
				|| !cols.contains(Constants.RUNMODE)) {
			APP_LOGS.error("Column names not defined properly in 'Test Cases' Sheet.. Please check if there any typos");
			APP_LOGS.error("Expected Columns are TCID,USID,Description,Runmode,Dependency,Status");
			return false;
		}

		sheet = workbook.getSheetAt(1);
		row = sheet.getRow(0);
		cols = getColumns();

		if (!cols.contains(Constants.TCID) || !cols.contains("Description") || !cols.contains(Constants.KEYWORD)
				|| !cols.contains(Constants.OBJECT) || !cols.contains(Constants.DATA)
				|| !cols.contains("Proceed_on_Fail")) {
			APP_LOGS.error("Column names not defined properly in 'Test Steps' Sheet.. Please check if there any typos");
			APP_LOGS.error("Expected Columns are TCID,TSID,Description,Keyword,Object,Data,Proceed_on_Fail");
			return false;
		}
		sheet = workbook.getSheetAt(0);
		return true;
	}

	/**
	 * Get columns
	 * 
	 * @return Return columns
	 */
	public List<String> getColumns() {
		List<String> cols = new ArrayList<String>();
		for (int i = 0; i < row.getLastCellNum(); i++) {
			cols.add(row.getCell(i).getStringCellValue().trim());
		}
		return cols;
	}

	/**
	 * Get row count
	 * 
	 * @param sheetName
	 *            Sheet Name
	 * @return Row count
	 */
	public int getRowCount(String sheetName) {
		int index = workbook.getSheetIndex(sheetName);
		if (index == -1) {
			return 0;
		} else {
			sheet = workbook.getSheetAt(index);
			int number = sheet.getLastRowNum() + 1;
			return number;
		}
	}

	/**
	 * Get cell data
	 * 
	 * @param sheetName
	 *            Sheet name
	 * @param colName
	 *            Column name
	 * @param rowNum
	 *            Row number
	 * @return Cell dta
	 */
	public String getCellData(String sheetName, String colName, int rowNum) {
		try {
			if (rowNum <= 0) {
				return "";
			}

			int index = workbook.getSheetIndex(sheetName);
			int col_Num = -1;
			if (index == -1) {
				return "";
			}
			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(0);
			for (int i = 0; i < row.getLastCellNum(); i++) {
				if (row.getCell(i).getStringCellValue().trim().equals(colName.trim())) {
					col_Num = i;
				}
			}
			if (col_Num == -1) {
				return "";
			}
			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(rowNum - 1);
			if (row == null) {
				return "";
			}
			cell = row.getCell(col_Num);
			if (cell == null) {
				return "";
			}

			if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				return cell.getStringCellValue();
			} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC || cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
				String cellText = cell.getRawValue();
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					// format in form of M/D/YY
					double d = cell.getNumericCellValue();
					Calendar cal = Calendar.getInstance();
					cal.setTime(HSSFDateUtil.getJavaDate(d));
					cellText = (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
					cellText = cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + 1 + "/" + cellText;
				}
				return cellText;
			} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK)
				return "";
			else {
				return String.valueOf(cell.getBooleanCellValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "row " + rowNum + " or column " + colName + " does not exist in xls";
		}
	}

	/**
	 * Get cell data
	 * 
	 * @param sheetName
	 *            Sheet name
	 * @param colNum
	 *            Column number
	 * @param rowNum
	 *            Row number
	 * @return Cell data
	 */
	public String getCellData(String sheetName, int colNum, int rowNum) {
		try {
			if (rowNum <= 0) {
				return "";
			}
			int index = workbook.getSheetIndex(sheetName);
			if (index == -1) {
				return "";
			}
			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(rowNum - 1);
			if (row == null) {
				return "";
			}
			cell = row.getCell(colNum);
			if (cell == null) {
				return "";
			}
			if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				return cell.getStringCellValue();
			} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC || cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
				String cellText = cell.getRawValue();
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					// format in form of M/D/YY
					double d = cell.getNumericCellValue();
					Calendar cal = Calendar.getInstance();
					cal.setTime(HSSFDateUtil.getJavaDate(d));
					cellText = (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
					cellText = cal.get(Calendar.MONTH) + 1 + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cellText;
				}
				return cellText;
			} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
				return "";
			} else {
				return String.valueOf(cell.getBooleanCellValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "row " + rowNum + " or column " + colNum + " does not exist  in xls";
		}
	}

	/**
	 * Verify sheet exists or not
	 * 
	 * @param sheetName
	 *            Sheet name
	 * @return true or false
	 */
	public boolean isSheetExist(String sheetName) {
		int index = workbook.getSheetIndex(sheetName);
		if (index == -1) {
			index = workbook.getSheetIndex(sheetName.toUpperCase());
			if (index == -1) {
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}

	/**
	 * Get column count
	 * 
	 * @param sheetName
	 *            Sheet name
	 * @return Column count
	 */
	public int getColumnCount(String sheetName) {
		if (!isSheetExist(sheetName)) {
			return -1;
		}
		sheet = workbook.getSheet(sheetName);
		row = sheet.getRow(0);
		if (row == null) {
			return -1;
		}
		return row.getLastCellNum();
	}

	/**
	 * Get row number
	 * 
	 * @param sheetName
	 *            Sheet name
	 * @param colName
	 *            Column name
	 * @param cellValue
	 *            Cell value
	 * @return Row number
	 */
	public int getCellRowNum(String sheetName, String colName, String cellValue) {

		for (int i = 2; i <= getRowCount(sheetName); i++) {
			if (getCellData(sheetName, colName, i).equalsIgnoreCase(cellValue)) {
				return i;
			}
		}
		return -1;
	}
}
