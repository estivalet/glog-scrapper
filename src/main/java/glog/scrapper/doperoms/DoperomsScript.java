package glog.scrapper.doperoms;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Vector;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.w3c.dom.NodeList;

import glog.util.XPathParser;

public class DoperomsScript {
	private static final String DOPEROMS_URL = "http://www.doperoms.com/roms/Apple_Ii/";
	private static final String GAMES_URL = DOPEROMS_URL + "games/";
	private static final String LIST_GAMES_URL = GAMES_URL + "list.php?&lineoffset=";
	private static final String XPATH_GAME_DETAILS = "//img[@alt='Details']/parent::a/@href";
	private static final String XPATH_TOTAL_GAMES = "//td/b/text()";
	private static final int PAGE_SIZE = 50;
	private static final String OUTPUT_FILE = "doperoms.txt";

	private void saveGamesLinksToFile() throws Exception {
		Vector<String> links = new Vector<String>();
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FILE, true)));
		XPathParser xp = new XPathParser(DOPEROMS_URL);
		int total = 3773;
		int pages = (int) Math.ceil((double) total / PAGE_SIZE);
		for (int p = 0; p < pages; p++) {
			System.out.println(DOPEROMS_URL + (p * PAGE_SIZE) + ".html");
			xp = new XPathParser(DOPEROMS_URL + (p * PAGE_SIZE));
			NodeList nl = xp.parseList("//a[contains(@name,'.zip')]/@href");
			for (int i = 0; i < nl.getLength(); i++) {
				System.out.println(nl.item(i).getNodeValue().replaceAll(" ", "%20"));
				links.add(nl.item(i).getNodeValue());
			}
		}
		for (String link : links) {
			out.println(GAMES_URL + link);
		}
		out.close();
	}

	public static void main(String[] args) throws Exception {
		DoperomsScript ds = new DoperomsScript();
		// ds.saveGamesLinksToFile();

		System.setProperty("webdriver.gecko.driver", "c:/users/luisoft/Downloads/geckodriver.exe");
		WebDriver driver = new FirefoxDriver();
		driver.get("http://www.doperoms.com/roms/apple_ii/Alien%2520Rain%2520%25281981%2529%2528Broderbund%2529%255Bnib%255D.zip.html/643770/Alien%20Rain%20(1981)(Broderbund)[nib].zip.html");
		WebElement myDynamicElement = (new WebDriverWait(driver, 50)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[contains(@alt,'Download')]")));
		myDynamicElement.click();
		myDynamicElement = (new WebDriverWait(driver, 50)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(@href,'/files/roms/apple_ii')]")));
		myDynamicElement.click();
	}

}
