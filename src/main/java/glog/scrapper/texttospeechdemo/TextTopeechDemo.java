package glog.scrapper.texttospeechdemo;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

import com.google.common.io.Files;

public class TextTopeechDemo {
	public static void main(String[] args) throws Exception {
		System.setProperty("webdriver.gecko.driver", "c:\\users\\luisoft\\Downloads\\geckodriver.exe");
		WebDriver driver = new FirefoxDriver();
		String[] systems = { "System 1" };

		for (String system : systems) {
			if ((new File("c:/temp/" + system + ".mp3")).exists()) {
				continue;
			}
			System.out.println(system);
			driver.get("https://text-to-speech-demo.mybluemix.net/");

			final Select selectBox = new Select(driver.findElement(By.name("voice")));
			selectBox.selectByValue("pt-BR_IsabelaVoice");

			driver.findElement(By.xpath("//textarea")).clear();
			// driver.findElement(By.xpath("//textarea")).sendKeys("Entering " + system + " system");
			driver.findElement(By.xpath("//textarea")).sendKeys("Entrando no sistema " + system);
			Thread.sleep(2000);
			driver.findElement(By.xpath("//button[.='Download']")).click();
			Thread.sleep(5000);

			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_ALT);
			robot.keyPress(KeyEvent.VK_S);
			robot.keyRelease(KeyEvent.VK_S);
			robot.keyRelease(KeyEvent.VK_ALT);

			Thread.sleep(1000);

			robot.keyPress(KeyEvent.VK_ENTER);
			Thread.sleep(1000);

			Files.move(new File("c:/users/luisoft/Downloads/transcript.ogg"), new File("c:/temp/ogg/" + system + ".ogg"));
		}
	}
}
