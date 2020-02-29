package glog.scrapper.atarimania;

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

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import glog.util.IOUtil;
import glog.util.URLGrabber;

public class AtariManiaScript {

	public void saveLinks() throws Exception {
		// 1. GRAB LINKS
		System.setProperty("webdriver.gecko.driver", "c:\\users\\luisoft\\Downloads\\geckodriver.exe");
		WebDriver driver = new FirefoxDriver();
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("atarimania.txt", true)));
		Set<String> links = new HashSet<String>();

		for (int i = 1; i < 2; i++) {
			driver.get("http://www.atarimania.com/list_games_atari-5200-p_search-total-page-step_37.-99-1-100_5_G.html");
			List<WebElement> elements = driver.findElements(By.xpath("//a[contains(@href,'game-atari-5200')]"));
			for (WebElement e : elements) {
				links.add(e.getAttribute("href"));
			}
		}
		Iterator<String> it = links.iterator();
		while (it.hasNext()) {
			out.println(it.next());
		}
		out.close();
		driver.quit();

		// 2. SAVE ALL LINKS IN INDIVIDUAL FILES FOR LATER PROCESSING
		BufferedReader input = new BufferedReader(new FileReader(new File("atarimania.txt")));
		String line = null;
		while ((line = input.readLine()) != null) {
			System.out.println("Processing..." + line.substring(line.lastIndexOf("/") + 1));
			URLGrabber ug = new URLGrabber(line, line.substring(line.lastIndexOf("/") + 1));
			ug.saveURL();
		}
		input.close();

	}

	public static void downloadMedia(String contents, String pattern, String destination) {
		int next = 1;
		while (next > 0) {
			next = contents.indexOf(pattern, next);
			int j = contents.indexOf("\"", next);
			if (next > 0) {
				String url = contents.substring(next, j);
				next = j;

				System.out.println(url);

				URLGrabber ug = new URLGrabber("http://www.atarimania.com/" + url, destination + "/" + url.substring(url.lastIndexOf("/") + 1));
				ug.saveURLBinary();
			}
		}

	}

	public static void getFirstAtariScreenshot() throws Exception {
		System.setProperty("webdriver.chrome.driver", "c:/users/luisoft/Downloads/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.get("http://www.atarimania.com/list_games_atari_search_97.98.122.111.111._S_G.html");
		ArrayList<String> games = IOUtil.getContentsAsArray(new File("c:/temp/st.txt"));
		for (String game : games) {
			String tmp = game;
			if (tmp.indexOf("(") > 0) {
				tmp = tmp.substring(0, tmp.indexOf("(")).trim();
			}

			driver.findElement(By.id("C2")).clear();
			driver.findElement(By.id("C2")).sendKeys(tmp);
			driver.findElement(By.xpath("//span[@class='btnvalignmiddle']")).click();
			try {
				String results = driver.findElement(By.xpath("//span[@class='TxtBlancGras']")).getText();
				// if ("1".equals(results)) {
				String href = driver.findElement(By.xpath("(//img[@class='Img'])[1]")).getAttribute("src");
				System.out.println(href);
				URLGrabber ug = new URLGrabber(href, "c:/temp/" + game + ".gif");
				ug.saveURLBinary();
				// }
			} catch (Exception e) {
				System.out.println("NOT FOUND:" + game);
			}
		}

	}

	public static void main(String[] args) throws Exception {
		AtariManiaScript ams = new AtariManiaScript();
//		ams.saveLinks();

		AtariManiaScript.getFirstAtariScreenshot();

	}

}
