package com.jenkinsBuild.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.jenkinsBuild.base.BaseTest;

public class LoginPage extends BaseTest {
	
	public LoginPage() {
		PageFactory.initElements(driver, this);
	}
	@FindBy(id="(//a[contains(text(),'Sign in')])[2]")
	WebElement SignInLink;
	
	public String verifyLoginPageTitle() {
		return driver.getTitle();
	}
}
