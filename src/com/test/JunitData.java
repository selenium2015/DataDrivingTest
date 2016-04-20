package com.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

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
public class JunitData {
	private WebDriver driver = new FirefoxDriver();
	private String height;
	private String weight;
	private String BMIValue;
	
	//构造函数进行参数传递赋值
	public JunitData(String height,String weight){
		this.weight=weight;
		this.height=height;
	}
	
	@Parameters
	public static Collection testData(){
		return Arrays.asList(new Object[][]{
				{"163","65"},
				{"163","70"},
				{"163","75"}
		}
      );
		
	}
	
	
	@Before
	public void setUp() throws Exception {
		driver.get("http://cn.onlinebmicalculator.com/");
	}

	@After
	public void tearDown() throws Exception {
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
		System.out.println("============");
	}

}
