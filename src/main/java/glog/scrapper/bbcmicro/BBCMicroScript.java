package glog.scrapper.bbcmicro;

import java.io.File;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import glog.util.IOUtil;
import glog.util.URLGrabber;

public class BBCMicroScript {
	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", "c:/users/luisoft/Downloads/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.get("http://www.bbcmicro.co.uk/");
		ArrayList<String> games = IOUtil.getContentsAsArray(new File("c:/temp/bbcmicro.txt"));
		for (String game : games) {
			String tmp = game;
			if (tmp.indexOf("(") > 0) {
				tmp = tmp.substring(0, tmp.indexOf("(")).trim();
			}

			driver.findElement(By.id("searchbox")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
			driver.findElement(By.id("searchbox")).sendKeys(tmp);
			driver.findElement(By.xpath("//button[.='Search' and @type=\"submit\"]")).click();
			try {
				String href = driver.findElement(By.xpath("(//img[1])[1]")).getAttribute("src");
				System.out.println(href);
				URLGrabber ug = new URLGrabber(href, "c:/temp/" + game + ".jpg");
				ug.saveURLBinary();
			} catch (Exception e) {
				System.out.println("NOT FOUND:" + game);
			}
		}
	}
}
