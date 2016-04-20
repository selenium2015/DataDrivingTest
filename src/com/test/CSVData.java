package com.test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.*;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * @author zhongts
 * 
 * 
 * 
 */
@RunWith(value=Parameterized.class)
public class CSVData {

	private WebDriver driver = new FirefoxDriver();
	private String height;
	private String weight;
	private String BMIValue;
	
	@Before
	public void setUp() throws Exception {
		driver.get("http://cn.onlinebmicalculator.com/");
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

	// 构造函数赋值
	public CSVData(String height, String weight) {
		this.height = height;
		this.weight = weight;
	}

	// 参数化文件
	@Parameters
	public static Collection<String[]> testData() {
		return getTestData("d:/test.csv");
	}

	// 读取CSV数据
	public static Collection<String[]> getTestData(String path) {
		List<String[]> list = new ArrayList<String[]>();
		String row;
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			while ((row = br.readLine()) != null) {
				String[] fileds = row.split(",");// 以逗号分隔数据
				list.add(fileds);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
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
		System.out.println("============");
	}

}
