package com.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

public class TestNGData {
	WebDriver driver = new FirefoxDriver();
	String BMIValue;
	String bodyCondition;

	@Test(dataProvider = "testData2")
	public void testBMI(String height, String weight) {
		WebElement heightIn = driver.findElement(By.name("f_height"));
		WebElement weightIn = driver.findElement(By.name("f_weight"));
		WebElement calculateButton = driver.findElement(By.name("f_submit"));

		if ((heightIn.getText() != null)) {
			heightIn.clear();
		}
		heightIn.sendKeys(height);
		if ((weightIn.getText() != null)) {
			weightIn.clear();
		}
		weightIn.sendKeys(weight);
		calculateButton.click();

		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		BMIValue = driver.findElement(
				By.cssSelector("#bt > div.bmi_result > p > span")).getText();

		double bmiValue = Double.valueOf(BMIValue);
		if (bmiValue >= 28.0) {
			System.out.println("BMI值为：" + BMIValue + "身体状态为：肥胖");
		} else if (bmiValue >= 24.0) {
			System.out.println("BMI值为：" + BMIValue + "身体状态为：过重");
		} else if (bmiValue >= 18.5) {
			System.out.println("BMI值为：" + BMIValue + "身体状态为：正常");
		} else {
			System.out.println("BMI值为：" + BMIValue + "身体状态为：偏瘦");
		}
		System.out.println("============");
	}

	/**
	 * @DataProvider的测试类中进行参数化
	 * @return
	 */
	@DataProvider
	public Object[][] testData() {
		return new Object[][] { { "163", "60" }, { "163", "65" },
				{ "163", "70" }, { "163", "75" } };
	}

	@DataProvider(name = "testData2")
	public Object[][] testData2() throws IOException {
		return getData();
	}

	@BeforeClass
	public void beforeClass() {
		driver.get("http://cn.onlinebmicalculator.com/");
	}

	@AfterClass
	public void afterClass() {
		driver.quit();
	}

	public static Object[][] getData() throws IOException {
		InputStream is = new FileInputStream("d:/test.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook(is);
		// 获得工作表：第一个
		XSSFSheet sheet = workbook.getSheetAt(0);
		// 得到总行数
		int rowNum = sheet.getLastRowNum();
		List<Object[]> records = new ArrayList<Object[]>();

		for (int i = 1; i <= rowNum; i++) {
			// 当前行
			XSSFRow row = sheet.getRow(i);
			int colNum = row.getLastCellNum();
			String[] data = new String[colNum];

			for (int j = 0; j < colNum; j++) {
				data[j] = row.getCell(j).getStringCellValue();
			}
			records.add(data);
		}
		// 转换成Object[][]
		Object[][] results = new Object[records.size()][];
		for (int i = 0; i < records.size(); i++) {
			results[i] = records.get(i);
			System.out.println(results[i]);
		}
		return results;
	}

}
