package com.HybridFramework.test;

import java.lang.reflect.Method;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.HybridFramework.base.BaseTest;
import com.HybridFramework.pages.LoginPage;

public class LoginPageTest extends BaseTest {
	
	public LoginPage loginPage;
	
	public LoginPageTest() {
		super();
	}
	@Parameters("Browser")
	@BeforeMethod
	public void setUp(String browser) {
		initialization(browser);
	}
	@Test
	public void verifyTitle(Method method) {
		extentTest=extent.startTest(method.getName());
		String loginPageTitle=loginPage.verifyLoginPageTitle();
		Assert.assertEquals(loginPageTitle, prop.getProperty("title"),"login page title not match");
	}
}
