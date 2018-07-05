package businesscomponents;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelUtility {

	static String path = "C://Users//465839//workspace//My_Account//Datatables//MyAccount.xls";
	static String sheetName = "DTW";

	public static String accountNum;	

	public static String newSiteID(String acctNo) 
	{
		String[] arr = acctNo.split("-");
		return arr[0].trim();
	}

	public static String newAccount(String acctNo)
	{
		String[] arr = acctNo.split("-");
		return arr[1].trim();
	}

	public static String getSet()
	{
	
		String data = null;
		for (int r = 1; r < allRows(path, sheetName); r++)
		{
			if (getData(path, sheetName, r, 0).trim().contains("Not")) 
			{
				data = getData(path, sheetName, r, 1);
				data = data.replace("'", " ").trim();
				setData(path, sheetName, "Used", r, 0);
				break;
			}
		}
		return data;
	}

	public static int allRows(String path, String sheetName) {
		int rows = 0;
		try {
			FileInputStream fis = new FileInputStream(new File(path));
			Workbook wb = WorkbookFactory.create(fis);
			Sheet sh = wb.getSheet(sheetName);
			rows = sh.getPhysicalNumberOfRows(); // Acctual
			// System.out.println(sh.getLastRowNum());

		} catch (Exception e) {
			System.err.println("Correct Rows are not returned.");
		}
		return rows;
	}

	public static String getData(String path, String sheetName, int rowNumber,
			int cellNumber) {
		String data = null;
		try {
			FileInputStream fis = new FileInputStream(new File(path));
			data = WorkbookFactory.create(fis).getSheet(sheetName)
					.getRow(rowNumber).getCell(cellNumber).getStringCellValue();
			if (data.isEmpty()) {
				data = "Data not Found.";
			}
		} catch (Exception e) {
			System.err.println("Error while getting Data.");
		}

		return data;
	}

	public static void setData(String path, String sheetName, String dataToSet,
			int rowNumber, int cellNumber) {
		try {
			FileInputStream fis = new FileInputStream(new File(path));
			Workbook wb = WorkbookFactory.create(fis);
			Sheet sh = wb.getSheet(sheetName);
			Row r = sh.getRow(rowNumber); // createRow
			Cell c = r.createCell(cellNumber);// createCell
			if (StringUtils.isNotBlank(dataToSet)) {
				c.setCellValue(dataToSet);
			}
			FileOutputStream fos = new FileOutputStream(new File(path));
			wb.write(fos);
			fos.flush();
			fos.close();
			System.out.println("Data written in Excel Sheet." + rowNumber++);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
