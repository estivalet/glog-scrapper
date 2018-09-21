package glog.scrapper.fromtexttospeech;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Fromtexttospeech {
	public static void main(String[] args) throws Exception {
		System.setProperty("webdriver.gecko.driver", "c:\\users\\luisoft\\Downloads\\geckodriver.exe");
		WebDriver driver = new FirefoxDriver();
		String[] systems = { "System 1" };

		for (String system : systems) {
			if ((new File("c:/temp/" + system + ".mp3")).exists()) {
				continue;
			}
			System.out.println(system);
			driver.get("http://www.fromtexttospeech.com");
			driver.findElement(By.id("input_text")).sendKeys("Entering " + system + " system");
			driver.findElement(By.id("create_audio_file")).click();
			WebDriverWait wait = new WebDriverWait(driver, 10);
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[.='Download audio file']")));
			element.click();
			Thread.sleep(5000);
			// String url = driver.findElement(By.xpath("//a[.='Download audio file']")).getAttribute("href");

			// URLGrabber ug = new URLGrabber(url, "c:/temp/" + system + ".mp3");
			// ug.saveURLBinary();
			// }

			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_S);
			// CTRL+Z is now pressed (receiving application should see a "key down" event.)
			robot.keyRelease(KeyEvent.VK_S);
			robot.keyRelease(KeyEvent.VK_CONTROL);

			Thread.sleep(2000);

			StringSelection stringSelection = new StringSelection(system);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(stringSelection, stringSelection);

			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);

			Thread.sleep(1000);

			robot.keyPress(KeyEvent.VK_ENTER);
			Thread.sleep(1000);
		}
		driver.close();
	}
}
