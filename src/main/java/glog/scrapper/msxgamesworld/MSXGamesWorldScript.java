package glog.scrapper.msxgamesworld;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.NodeList;

import glog.util.XMLUtil;
import glog.util.XMLWrapper;
import glog.util.XPathParser;

public class MSXGamesWorldScript {
	private static final String MSXGAMESWORLD_URL = "http://msxgamesworld.com/";
	private static final String GAMES_URL = MSXGAMESWORLD_URL + "/results.php?page=ppp&type=game&sort=nom&dir=asc";
	private static final String XPATH_GAMES_COUNT = "//div[@class='alert alert-success alert-light']/strong[1]/text()";
	private static final String XPATH_GAME_LINK = "//a[contains(@href,'gamecard.php')]/@href";
	private static final String OUTPUT_FILE = "msxgamesworld.txt";
	private static final int PAGE_SIZE = 25;
	private List<MSXGamesWorldGame> games = new ArrayList<MSXGamesWorldGame>();

	private void saveGamesLinksToFile() throws Exception {
		Vector<String> links = new Vector<String>();
		int totalGames = this.getGamesCount();
		System.out.println(totalGames);
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FILE, true)));
		// there is an invalid char in page 24, need to skip it.
		for (int p = 1; p <= totalGames / PAGE_SIZE; p++) {
			System.out.println(p);
			XPathParser xp = new XPathParser(GAMES_URL.replace("ppp", String.valueOf(p)));
			NodeList nl = xp.parseList(XPATH_GAME_LINK);
			for (int i = 0; i < nl.getLength(); i++) {
				System.out.println("adding " + nl.item(i).getNodeValue());
				links.add(nl.item(i).getNodeValue());
			}
		}
		for (String link : links) {
			out.println(MSXGAMESWORLD_URL + link);
		}
		out.close();
	}

	public int getGamesCount() throws Exception {
		XPathParser xp = new XPathParser(GAMES_URL.replace("ppp", "1"));
		String total = xp.parse(XPATH_GAMES_COUNT);
		total = total.replace(" results", "");

		return Integer.parseInt(total);
	}

	private String getGameMainSummary(String url) throws Exception {
		MSXGamesWorldGame game = new MSXGamesWorldGame();
		game.setUrl(url);
		XPathParser xp = new XPathParser(url);
		String name = xp.parse("//div[@class='ms-title']/following::h1/text()");
		game.setName(name);

		String[] infos = { "Developer", "Year of release", "Type of software", "Format", "MSX Generation", "Sound", "Languages", "Type of control", "Num. players", "Publisher", "Country", "Region", "Product ID", "Type of license",
				"Requires", "Price" };
		String[] attrs = { "developer", "year", "type", "format", "generation", "sound", "languages", "control", "players", "publisher", "country", "region", "productId", "typeOfLicense", "requires", "price" };
		for (int i = 0; i < infos.length; i++) {
			String info = xp.parse("//dt[.='" + infos[i] + "']/following::dd[1]");
			BeanUtils.setProperty(game, attrs[i], info.trim());
		}
		int releases = xp.parseList("//h3[@class='text-right']").getLength();
		for (int i = 1; i <= releases; i++) {
			MSXGamesWorldGameRelease r = new MSXGamesWorldGameRelease();
			String[] infos1 = { "Product code", "Publisher", "Year", "Languages", "Region/Country", "Format", "Price", "Dep. Legal" };
			String[] attrs1 = { "productCode", "publisher", "year", "languages", "region", "format", "price", "depLegal" };
			for (int j = 0; j < infos1.length; j++) {
				// String releaseNum = xp.parse("//h3[.='Releases']/following::dt[.='" + infos1[j] + "'][" + i + "]/preceding-sibling::h3[1]/span/strong");
				// System.out.println("//h3[.='Releases']/following::dt[.='" + infos1[j] + "'][" + i + "]/preceding-sibling::h3[1]/span/strong");
				// System.out.println("RELEASE NUM-->" + releaseNum);
				// String info = xp.parse("//h3[.='Releases']/following::dt[.='" + infos1[j] + "'][" + i + "]/following::dd[1]");
				// BeanUtils.setProperty(r, attrs1[j], info.trim());

				String releaseNum = xp.parse("(//h3[@class='text-right'])[" + i + "]/following::dt[.='" + infos1[j] + "'][1]/preceding::h3[@class='text-right'][1]");
				if (releaseNum != "") {
					if (Integer.parseInt(releaseNum) == i) {
						String info = xp.parse("(//h3[@class='text-right'])[" + i + "]/following::dt[.='" + infos1[j] + "'][1]/following::dd[1]");
						BeanUtils.setProperty(r, attrs1[j], info.trim());

					}
				}
			}
			game.addRelease(r);
		}

		int remakes = xp.parseList("//h3[.='Remakes']/following::dd").getLength();
		for (int i = 1; i <= remakes; i++) {
			String link = xp.parse("(//h3[.='Remakes']/following::dd)[" + i + "]/a/@href");
			if (link != "") {
				game.addRemake(link);
			}
		}

		int caratulas = xp.parseList("//img[contains(@src,'caratulas')]").getLength();
		for (int i = 1; i <= caratulas; i++) {
			game.addCaratula(xp.parse("(//img[contains(@src,'caratulas')])[" + i + "]/@src"));
		}

		int capturas = xp.parseList("//img[contains(@src,'capturas')]").getLength();
		for (int i = 1; i <= capturas; i++) {
			game.addCaptura(xp.parse("(//img[contains(@src,'capturas')])[" + i + "]/@src"));
		}

		games.add(game);

		JAXBContext jc = JAXBContext.newInstance(XMLWrapper.class, MSXGamesWorldGame.class);
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		return XMLUtil.marshal(marshaller, games, "games");

	}

	public static void main(String[] args) throws Exception {
		// mgws.saveGamesLinksToFile();

		List<String> lines = FileUtils.readLines(new File("msxgamesworld.txt"));
		for (String line : lines) {
			MSXGamesWorldScript mgws = new MSXGamesWorldScript();
			String fileName = "msxgamesworld/" + line.substring(line.lastIndexOf("=") + 1) + ".xml";
			if (!new File(fileName).exists()) {
				System.out.println("Scrapping... " + fileName);
				FileUtils.write(new File(fileName), mgws.getGameMainSummary(line), "UTF-8");
			}
		}

		// mgws.getGameMainSummary("http://msxgamesworld.com/gamecard.php?id=416");
	}

}
