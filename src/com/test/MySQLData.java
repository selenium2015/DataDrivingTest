package com.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
public class MySQLData {
	private WebDriver driver;
	private String weight;
	private String height;
	private String BMIValue;
	
	//参数化，从MySQL数据库取值
	@Parameters
	public static Collection<String[]> testData() throws SQLException{
		List<String[]> records = new ArrayList<String[]>();
		//连接数据库
		new com.mysql.jdbc.Driver();
		Connection con=DriverManager.getConnection("jdbc:mysql://192.168.3.41/traffic","jira","jira");
		Statement st=con.createStatement();
		ResultSet rs=st.executeQuery("select amt,id from payment_reconciliate_data where id<5");
		//取得结果集结构
		ResultSetMetaData rsMetaData=rs.getMetaData();
		int cols=rsMetaData.getColumnCount();//取得行数
		while(rs.next()){
			String[] fileds=new String[cols];
			int col=0;
			for(int colIdx=1;colIdx<=cols;colIdx++){
				fileds[col]=rs.getString(colIdx);
				col++;
			}
			records.add(fileds);
		}
		rs.close();
		st.close();
		con.close();	
		
		return records;
		
	}
	
	//构造函数进行传递参数
	public MySQLData(String weight,String height){
		this.weight=weight;
		this.height=height;
	}	
	
	@Before
	public void setUp() throws Exception {
		driver=new FirefoxDriver();
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
		System.out.println("========================");
	}

}
