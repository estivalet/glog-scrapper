package glog.scrapper.theoldcomputer;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TheOldComputerScript {

	private void grabLinks() throws Exception {
		System.setProperty("webdriver.gecko.driver", "c:/users/luisoft/Downloads/geckodriver.exe");
		// 1. GRAB LINKS
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("generationmsx.txt", true)));
		WebDriver driver = new FirefoxDriver();
		driver.get("http://www.theoldcomputer.com/roms/index.php?folder=Texas-Instruments/TI-99-4A/Cartridges");
		List<WebElement> elements = driver.findElements(By.xpath("//a[contains(@href,'getfile')]"));
		List<String> links = new ArrayList<String>();
		for (int i = 0; i < elements.size(); i++) {
			links.add(elements.get(i).getAttribute("href"));
		}
		elements.get(0).click();
		Thread.sleep(3000);
		driver.findElement(By.id("username")).sendKeys("howlerbr");
		driver.findElement(By.id("password")).sendKeys("Luisoft#2209");
		driver.findElement(By.name("login")).click();
		Thread.sleep(3000);
		// driver.findElement(By.xpath("//a[contains(@href,'download')]")).click();
		// Thread.sleep(3000);
		// Robot robot = new Robot();
		// Simulate a key press
		// robot.keyPress(KeyEvent.VK_DOWN);
		// robot.keyRelease(KeyEvent.VK_DOWN);
		// robot.keyPress(KeyEvent.VK_ENTER);
		// robot.keyRelease(KeyEvent.VK_ENTER);
		// driver.quit();

		// driver.get("http://www.theoldcomputer.com/roms/index.php?folder=Texas-Instruments/TI-99-4A/Cartridges");
		// elements = driver.findElements(By.xpath("//a[contains(@href,'getfile')]"));
		Robot robot = null;
		for (int i = 63; i < links.size(); i++) {
			// elements.get(i).click();
			driver.get(links.get(i));
			Thread.sleep(3000);
			driver.findElement(By.xpath("//a[contains(@href,'download')]")).click();
			Thread.sleep(3000);
			robot = new Robot();
			// Simulate a key press
			robot.keyPress(KeyEvent.VK_DOWN);
			robot.keyRelease(KeyEvent.VK_DOWN);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
			// driver.get("http://www.theoldcomputer.com/roms/index.php?folder=Texas-Instruments/TI-99-4A/Cartridges");
			// elements = driver.findElements(By.xpath("//a[contains(@href,'getfile')]"));
		}

	}

	public static void main(String[] args) throws Exception {
		TheOldComputerScript g = new TheOldComputerScript();
		g.grabLinks();
	}
}
