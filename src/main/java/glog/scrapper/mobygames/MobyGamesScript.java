package glog.scrapper.mobygames;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.beanutils.BeanUtils;
import org.w3c.dom.NodeList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import glog.util.IOUtil;
import glog.util.XMLUtil;
import glog.util.XMLWrapper;
import glog.util.XPathParser;

/**
 * Get game information from mobygames.com website.
 * 
 * @author lestivalet
 *
 */
public class MobyGamesScript {
	private static final String MOBY_URL = "http://www.mobygames.com/";
	private static final String GAMES_URL = MOBY_URL + "browse/games/";
	private static final String XPATH_GAMES_COUNT = "//td[@class='mobHeaderItems']";
	private static final String XPATH_GAME_LINK = "//a[contains(@href,'/game/%param1%')]/@href";
	private static final String OUTPUT_FILE = "mobygames.txt";
	private static final int PAGE_SIZE = 25;
	private String system;
	private List<MobyGame> games;

	/**
	 * @param system
	 *            System to scrap the games.
	 */
	public MobyGamesScript(String system) {
		this.system = system;
		this.games = new ArrayList<MobyGame>();
	}

	/**
	 * @return number of games available for the selected system.
	 * @throws Exception
	 */
	public int getGamesCount() throws Exception {
		XPathParser xp = new XPathParser(GAMES_URL + this.system + "/list-games");
		String total = xp.parse(XPATH_GAMES_COUNT);

		return Integer.parseInt(total.substring(total.indexOf("of") + 3, total.length() - 1));
	}

	/**
	 * Save all game links to a text file for later processing.
	 * 
	 * @throws Exception
	 */
	private void saveGamesLinksToFile() throws Exception {
		Vector<String> links = new Vector<String>();
		int totalGames = this.getGamesCount();
		System.out.println(totalGames);
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FILE, true)));
		for (int p = 0; p <= totalGames; p += PAGE_SIZE) {
			XPathParser xp = new XPathParser(GAMES_URL + this.system + "/offset," + p + "/so,0a/list-games/");
			NodeList nl = xp.parseList(XPATH_GAME_LINK.replace("%param1%", this.system));
			for (int i = 0; i < nl.getLength(); i++) {
				System.out.println("adding " + nl.item(i).getNodeValue());
				links.add(nl.item(i).getNodeValue());
			}
		}
		for (String link : links) {
			out.println(MOBY_URL + link);
		}
		out.close();
	}

	/**
	 * Get game main summary tab.
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	private MobyGame getGameMainSummary(String url) throws Exception {
		MobyGame game = new MobyGame();
		game.setUrl(url);
		XPathParser xp = new XPathParser(url);
		String name = xp.parse("//h1[@class='niceHeaderTitle']/a");
		game.setName(name);

		String contents = xp.getPageContents();

		int i = contents.lastIndexOf("Description</h2>");
		int j = contents.indexOf("<div", i + 16);
		String description = contents.substring(i + 16, j);
		game.setDescription(description);

		String[] infos = { "Published by", "Developed by", "Released", "Also For", "Genre", "Perspective", "Theme",
				"Non-Sport", "Sport", "Misc", "Country", "Release Date" };
		String[] attrs = { "publishedBy", "developedBy", "released", "alsoFor", "genre", "perspective", "theme",
				"nonSport", "sport", "misc", "country", "releaseDate" };
		for (i = 0; i < infos.length; i++) {
			String info = xp.parse("//div[.='" + infos[i] + "']/following::div[1]");
			BeanUtils.setProperty(game, attrs[i], info);
		}

		return game;
	}

	/**
	 * Parse Release tab. (/release-info)
	 * 
	 * @param game
	 * @return
	 * @throws Exception
	 */
	private MobyGame getGameReleaseInfo(MobyGame game) throws Exception {
		XPathParser xp = new XPathParser(game.getUrl() + "/release-info");

		// Releases
		NodeList systems = xp.parseList("//h2");
		for (int s = 0; s < systems.getLength(); s++) {
			String systemName = systems.item(s).getTextContent();

			MobyGameRelease release = new MobyGameRelease();
			release.setSystem(systemName);
			// System.out.println(systemName);

			// Publishers
			NodeList publishers = xp.parseList("//h2[.='" + systemName + "']/following::div[.='Published by']");
			for (int p = 1; p <= publishers.getLength(); p++) {

				String publisherName = xp.parse(
						"(//h2[.='" + systemName + "']/following::div[.='Published by'])[" + p + "]/following::a[1]");

				String currentSystem = xp.parse(
						"(//h2[.='" + systemName + "']/following::div[.='Published by'])[" + p + "]/preceding::h2[1]");
				if (!currentSystem.equals(systemName)) {
					continue;
				}

				String currentPub = xp.parse("//h2[.='" + systemName + "']/following::a[.=\"" + publisherName
						+ "\"]/following::div[.='Developed by']/preceding::div[.='Published by'][1]/following::a[1]");
				String developerName = "";
				if (currentPub.equals(publisherName)) {
					developerName = xp.parse("//h2[.='" + systemName + "']/following::a[.=\"" + publisherName
							+ "\"]/following::div[.='Developed by']/following::a[1]");
				}

				currentPub = xp.parse("//h2[.='" + systemName + "']/following::a[.=\"" + publisherName
						+ "\"]/following::div[.='Country']/preceding::div[.='Published by'][1]/following::a[1]");
				String country = "";
				if (currentPub.equals(publisherName)) {
					country = xp.parse("//h2[.='" + systemName + "']/following::a[.=\"" + publisherName
							+ "\"]/following::div[.='Country']/following::span[1]");
				}

				if (country.equals("")) {
					currentPub = xp.parse("(//h2[.='" + systemName + "']/following::a[.=\"" + publisherName
							+ "\"]/following::div[.='Countries'])[1]/preceding::div[.='Published by'][1]/following::a[1]");
					if (currentPub.equals(publisherName)) {
						NodeList countriesList = xp
								.parseList("(//h2[.='" + systemName + "']/following::a[.=\"" + publisherName
										+ "\"]/following::div[.='Countries'])[1]/following-sibling::div[1]/span");
						for (int c = 0; c < countriesList.getLength(); c++) {
							country += countriesList.item(c).getTextContent();
						}
					}
				}

				currentPub = xp.parse("//h2[.='" + systemName + "']/following::a[.=\"" + publisherName
						+ "\"]/following::div[.='Release Date']/preceding::div[.='Published by'][1]/following::a[1]");
				String releaseDate = "";
				if (currentPub.equals(publisherName)) {
					releaseDate = xp.parse("//h2[.='" + systemName + "']/following::a[.=\"" + publisherName
							+ "\"]/following::div[.='Release Date']/following-sibling::div[1]");
				}

				currentPub = xp.parse("//h2[.='" + systemName + "']/following::a[.=\"" + publisherName
						+ "\"]/following::div[.='EAN-13']/preceding::div[.='Published by'][1]/following::a[1]");
				String ean13 = "";
				if (currentPub.equals(publisherName)) {
					ean13 = xp.parse("//h2[.='" + systemName + "']/following::a[.=\"" + publisherName
							+ "\"]/following::div[.='EAN-13']/following-sibling::div[1]");
				}

				currentPub = xp.parse("//h2[.='" + systemName + "']/following::a[.=\"" + publisherName
						+ "\"]/following::div[.='Comments']/preceding::div[.='Published by'][1]/following::a[1]");
				String comments = "";
				if (currentPub.equals(publisherName)) {
					comments = xp.parse("//h2[.='" + systemName + "']/following::a[.=\"" + publisherName
							+ "\"]/following::div[.='Comments']/following-sibling::div[1]");
				}

				currentPub = xp.parse("//h2[.='" + systemName + "']/following::a[.=\"" + publisherName
						+ "\"]/following::div[.='Ported by']/preceding::div[.='Published by'][1]/following::a[1]");
				String portedBy = "";
				if (currentPub.equals(publisherName)) {
					portedBy = xp.parse("//h2[.='" + systemName + "']/following::a[.=\"" + publisherName
							+ "\"]/following::div[.='Ported by']/following::a[1]");
				}

				MobyGameReleaseInfo releaseInfo = new MobyGameReleaseInfo();
				releaseInfo.setPublisher(publisherName);
				releaseInfo.setDeveloper(developerName);
				releaseInfo.setCountry(country);
				releaseInfo.setReleaseDate(releaseDate);
				releaseInfo.setPorted(portedBy);
				releaseInfo.setEan13(ean13);
				releaseInfo.setComments(comments);

				release.addReleaseInfo(releaseInfo);

			}

			game.addRelease(release);

		}
		return game;
	}

	/**
	 * Get URL for the available screenshots.
	 * 
	 * @param game
	 * @return
	 * @throws Exception
	 */
	private MobyGame getScreenshot(MobyGame game) throws Exception {
		try {
			XPathParser xpath = new XPathParser(game.getUrl() + "/screenshots");
			NodeList shotLinks = xpath.parseList("//a[contains(@href,'/screenshots/')]/@href");
			for (int i = 0; i < shotLinks.getLength(); i++) {
				String l = shotLinks.item(i).getTextContent();
				xpath = new XPathParser(MOBY_URL + l);
				game.addScreenshot(xpath.parse("//img[contains(@src,'/images/shots/')]/@src"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return game;
	}

	/**
	 * Get URL for available cover arts.
	 * 
	 * @param game
	 * @return
	 * @throws Exception
	 */
	private MobyGame getCoverArt(MobyGame game) throws Exception {
		XPathParser xpath = new XPathParser(game.getUrl() + "/cover-art");
		NodeList shotLinks = xpath.parseList("//a[contains(@href,'/cover-art/')]/@href");
		for (int i = 0; i < shotLinks.getLength(); i++) {
			String l = shotLinks.item(i).getTextContent();
			xpath = new XPathParser(MOBY_URL + l);
			game.addCover(xpath.parse("//img[contains(@src,'/images/covers/')]/@src"));
		}
		return game;
	}

	/**
	 * Add a new game to the list.
	 * 
	 * @param game
	 */
	public void addGame(MobyGame game) {
		this.games.add(game);
	}

	/**
	 * @return the game list.
	 */
	public List<MobyGame> getGames() {
		return games;
	}

	/**
	 * Get game information: main summary, releases, screenshots and cover art
	 * 
	 * @param url
	 *            game URL
	 * @return String in XML format.
	 * @throws Exception
	 */
	public String getGameInfo(String url) throws Exception {
		MobyGame game = this.getGameMainSummary(url);
		game = this.getGameReleaseInfo(game);
		game = this.getScreenshot(game);
		this.addGame(this.getCoverArt(game));

		JAXBContext jc = JAXBContext.newInstance(XMLWrapper.class, MobyGame.class);
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		return XMLUtil.marshal(marshaller, this.getGames(), "games");
	}

	/**
	 * Convert the XML to JSON to store in MONGODB database.
	 * 
	 * @param file
	 *            XML file to convert.
	 * @throws Exception
	 */
	private void convertXMLtoJSON(String file) throws Exception {
		String xml = IOUtil.getContents(new File(file));
		JAXBContext jc = JAXBContext.newInstance(XMLWrapper.class, MobyGame.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		MobyGame g = XMLUtil.unmarshalString(unmarshaller, MobyGame.class, xml).get(0);
		g.setSystem(this.system);

		System.out.println(g.getName());
		System.out.println(g.getPublishedBy());

		String outfile = file.substring(file.lastIndexOf("/") + 1, file.lastIndexOf(".")) + ".json";
		try (Writer writer = new FileWriter("data/output/" + outfile)) {
			Gson gson = new GsonBuilder().create();
			gson.toJson(g, writer);
		}

	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		MobyGamesScript mgs = new MobyGamesScript("msx");
		mgs.convertXMLtoJSON("data/mobygames/MSX - MSX 1/abu-simbel-profanation.xml");
	}

}
