package glog.scrapper.generationmsx;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.beanutils.BeanUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.w3c.dom.NodeList;

import glog.util.URLGrabber;
import glog.util.XMLUtil;
import glog.util.XMLWrapper;
import glog.util.XPathParser;

public class GenerationMSXScript {
	private List<GenerationMSXGame> games;

	public GenerationMSXScript() {
		this.games = new ArrayList<GenerationMSXGame>();
	}

	public void addGame(GenerationMSXGame game) {
		this.games.add(game);
	}

	public List<GenerationMSXGame> getGames() {
		return games;
	}

	private void grabLinks() throws Exception {
		System.setProperty("webdriver.gecko.driver", "c:/users/luisoft/Desktop/geckodriver.exe");
		// 1. GRAB LINKS
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("generationmsx2.txt", true)));
		Set<String> links = new HashSet<String>();
		WebDriver driver = new FirefoxDriver();

		for (int i = 1; i < 297; i++) {
			System.out.println("Processing page..." + i);
			driver.get("http://www.generation-msx.nl/software/result?q=&searchtype=#/q=&searchtype=&p=" + i);

			WebElement myDynamicElement = (new WebDriverWait(driver, 50)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//nav[@class='row topPager']//li[@class='active']/a[contains(text(),'" + i + "')]")));

			List<WebElement> elements = driver.findElements(By.xpath("//table/tbody/tr/td[1]/a"));
			System.out.println(elements.size());
			for (WebElement e : elements) {
				System.out.println(e.getAttribute("href"));
				links.add(e.getAttribute("href"));
			}
			if (i + 1 < 297) {
				driver.findElement(By.xpath("//nav[@class='row topPager']//li/a[contains(text(),'" + (i + 1) + "')]")).click();
			}
		}
		driver.quit();
		Iterator<String> it = links.iterator();
		while (it.hasNext()) {
			out.println(it.next());
		}
		out.close();

	}

	private void saveLinks() throws Exception {
		// 2. SAVE ALL LINKS IN INDIVIDUAL FILES FOR LATER PROCESSING
		BufferedReader input = new BufferedReader(new FileReader(new File("generationmsx.txt")));
		String line = null;
		while ((line = input.readLine()) != null) {
			line = line.substring(0, line.length() - 1);
			System.out.println("Processing..." + line.substring(line.lastIndexOf("/") + 1));
			URLGrabber ug = new URLGrabber(line, line.substring(line.lastIndexOf("/") + 1));
			ug.saveURL();
		}
		input.close();

	}

	private GenerationMSXGame parseInformation(String fileName, String link) throws Exception {
		XPathParser xp = new XPathParser(new File(fileName));

		GenerationMSXGame game = new GenerationMSXGame();

		String[] indexes = { "Original title", "Developed by", "Year", "System", "Sound", "Kind", "Input Devices Supported", "Genre", "Max Players", "Max Simultaneous", "Uses Kanji", "License", "Licence", "Language", "Conversion", "Note",
				"Tagoo database entry", "Mentioned in" };
		String[] attrs = { "originalTitle", "developedBy", "year", "system", "sound", "kind", "inputDevicesSupported", "genre", "maxPlayers", "maxSimultaneous", "usesKanji", "license", "licence", "language", "conversion", "note",
				"tagooDatabaseEntry", "mentionedIn" };
		for (int i = 0; i < indexes.length; i++) {
			String info = xp.parse("//dt[.='" + indexes[i] + "']/following::dd[1]").trim();
			BeanUtils.setProperty(game, attrs[i], info);
		}

		return game;
	}

	private GenerationMSXGame parseReleases(GenerationMSXGame game, String fileName, String link) throws Exception {
		XPathParser xp = new XPathParser(new File(fileName));
		NodeList nl = xp.parseList("//dt[.='Published by']");
		for (int i = 1; i < nl.getLength() + 1; i++) {
			GenerationMSXGameRelease release = new GenerationMSXGameRelease();
			release.setPublisher(xp.parse("(//dt[.='Published by'])[" + i + "]/following::dd[1]"));
			release.setMedia(xp.parse("(//dt[.='Media type'])[" + i + "]/following::dd[1]/img/@title") + " " + xp.parse("(//dt[.='Media type'])[" + i + "]/following::dd[1]").trim());
			release.setCountry(xp.parse("(//dt[.='Released in'])[" + i + "]/following::dd[1]").trim());
			game.addRelease(release);
		}

		return game;
	}

	public String getGameInfo(String url) throws Exception {
		GenerationMSXGame game = parseInformation(url, "link");
		game = parseReleases(game, "c:/temp/releases.html", "link");
		this.addGame(game);

		JAXBContext jc = JAXBContext.newInstance(XMLWrapper.class, GenerationMSXGame.class);
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		return XMLUtil.marshal(marshaller, this.getGames(), "games");
	}

	public static void main(String[] args) throws Exception {
		GenerationMSXScript g = new GenerationMSXScript();
		g.grabLinks();
		// System.out.println(g.getGameInfo("c:/temp/info.html"));
	}
}
