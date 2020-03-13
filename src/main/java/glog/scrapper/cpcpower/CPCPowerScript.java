package glog.scrapper.cpcpower;

import java.io.File;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import glog.util.IOUtil;
import glog.util.URLGrabber;

public class CPCPowerScript {
	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", "c:/users/luisoft/Downloads/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.get("https://www.cpc-power.com/index.php?page=database");
		ArrayList<String> games = IOUtil.getContentsAsArray(new File("c:/temp/cpc.txt"));
		for (String game : games) {
			String tmp = game;
			if (tmp.indexOf("(") > 0) {
				tmp = tmp.substring(0, tmp.indexOf("(")).trim();
			}
			if (tmp.indexOf(",") > 0) {
				tmp = tmp.substring(0, tmp.indexOf(",")).trim();
			}

			driver.findElement(By.id("lemot")).clear();
			driver.findElement(By.id("lemot")).sendKeys(tmp);
			driver.findElement(By.xpath("//input[@value='Search']")).click();
			try {
				String href = driver.findElement(By.xpath("//div[@class='listingcart']/div/div/a/img")).getAttribute("src");
				System.out.println(href);
				URLGrabber ug = new URLGrabber(href, "c:/temp/" + game + ".png");
				ug.saveURLBinary();
			} catch (Exception e) {
				System.out.println("NOT FOUND:" + game);
			}
		}
	}
}
