package com.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 *@author zhongts
 *
 *
 *
 */
@RunWith(value=Parameterized.class)
public class ExcelData {
	private WebDriver driver;
	private String weight;
	private String height;
	private String BMIValue;
	
	//参数化
	@Parameters
	public static Collection<String[]> testData() throws IOException{
		InputStream is=new FileInputStream("d:/test.xlsx");
		XSSFWorkbook workbook=new XSSFWorkbook(is);
		//获得工作表：第一个
		XSSFSheet sheet =workbook.getSheetAt(0);
		//得到总行数
		int rowNum=sheet.getLastRowNum();
		List<String[]> records=new ArrayList<String[]>();
		
		for(int i=1;i<=rowNum;i++){
			//当前行
			XSSFRow row=sheet.getRow(i);
			int colNum=row.getLastCellNum();
			String[] data=new String[colNum];
			
			for(int j=0;j<colNum;j++){
				data[j]=row.getCell(j).getStringCellValue();
			}
			records.add(data);
		}
		return records;
	}
	
	//构造函数赋值
	public ExcelData(String height,String weight){
		this.height=height;
		this.weight=weight;
	}
	
//	//读取excel中的数据
//	public static Collection<String[]> getTestData(String path) {
//		List<String[]> list = new ArrayList<String[]>();
//		String row;
//
//		try {
//			BufferedReader br = new BufferedReader(new FileReader(path));
//			while ((row = br.readLine()) != null) {
//				String[] fileds = row.split(",");// 以逗号分隔数据
//				list.add(fileds);
//			}
//			br.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} 
//		return list;
//	}
	

	@Before
	public void setUp(){
		driver=new FirefoxDriver();
		driver.get("http://cn.onlinebmicalculator.com/");
	}

	@After
	public void tearDown(){
		driver.close();
	}

	@Test
	public void testBMI() {
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
		System.out.println("========================");
	}

}
