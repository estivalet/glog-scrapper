package glog.scrapper.worldofspectrum;

import java.io.File;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import glog.util.IOUtil;
import glog.util.URLGrabber;

public class WorldOfSpectrumScript {

	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", "c:/users/luisoft/Downloads/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.get("https://www.worldofspectrum.org/infoseek.cgi");
		ArrayList<String> games = IOUtil.getContentsAsArray(new File("c:/temp/zxspectrum.txt"));
		for (String game : games) {
			String tmp = game;
			if (tmp.indexOf("(") > 0) {
				tmp = tmp.substring(0, tmp.indexOf("(")).trim();
			}

			driver.findElement(By.name("regexp")).sendKeys(tmp);
			driver.findElement(By.xpath("//input[@value='Search!']")).click();
			try {
				String href = driver.findElement(By.xpath("//font[.='(In-game screen)']/preceding::a[1]")).getAttribute("href");
				System.out.println(href);
				URLGrabber ug = new URLGrabber(href, "c:/temp/" + game + ".gif");
				ug.saveURLBinary();
			} catch (Exception e) {
				System.out.println("NOT FOUND:" + game);
			}
		}
	}

}
