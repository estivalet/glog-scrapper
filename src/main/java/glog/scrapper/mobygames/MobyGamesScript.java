package glog.scrapper.mobygames;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.beanutils.BeanUtils;
import org.w3c.dom.NodeList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import glog.util.IOUtil;
import glog.util.URLGrabber;
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
	private static final String MOBY_URL = "https://www.mobygames.com/";
	private static final String GAMES_URL = MOBY_URL + "browse/games/";
	private static final String XPATH_GAMES_COUNT = "//td[@class='mobHeaderItems']";
	private static final String XPATH_GAME_LINK = "//a[contains(@href,'/game/%param1%')]/@href";
	private static final String OUTPUT_FILE = "mobygames.txt";
	private static final int PAGE_SIZE = 25;
	private String system;
	private List<MobyGame> games;

	/**
	 * @param system System to scrap the games.
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

		if ("".equals(total)) {
			return 1;
		}

		return Integer.parseInt(total.substring(total.indexOf("of") + 3, total.length() - 1));
	}

	public void full() throws Exception {
		XPathParser xp = new XPathParser("https://www.mobygames.com/browse/games/full,1/");
		NodeList nl = xp.parseList("//h4[.='Platform']/following::a[contains(@href,'games')]/@href");
		for (int i = 0; i < nl.getLength(); i++) {
			System.out.println(nl.item(i).getNodeValue());
		}

	}

	private void saveGamesLinksToFile() throws Exception {
		saveGamesLinksToFile(OUTPUT_FILE);
	}

	/**
	 * Save all game links to a text file for later processing.
	 * 
	 * @throws Exception
	 */
	private void saveGamesLinksToFile(String fileName) throws Exception {
		if (new File(fileName).exists()) {
			System.out.println("Skipping " + fileName + " already exists");
			return;
		}
		Vector<String> links = new Vector<String>();
		int totalGames = this.getGamesCount();
		System.out.println(totalGames);
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
		for (int p = 0; p <= totalGames; p += PAGE_SIZE) {
			XPathParser xp = new XPathParser(GAMES_URL + this.system + "/offset," + p + "/so,0a/list-games/");
			NodeList nl = xp.parseList(XPATH_GAME_LINK.replace("%param1%", this.system));
			for (int i = 0; i < nl.getLength(); i++) {
				System.out.println("adding " + nl.item(i).getNodeValue());
				links.add(nl.item(i).getNodeValue());
			}
		}
		for (String link : links) {
			out.println(link);
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
		game.setShortName(url.substring(url.lastIndexOf("/") + 1));
		XPathParser xp = new XPathParser(url);
		String name = xp.parse("//h1[@class='niceHeaderTitle']/a");
		game.setName(name);

		String contents = xp.getPageContents();

		int i = contents.lastIndexOf("Description</h2>");
		int j = contents.indexOf("<div", i + 16);
		String description = contents.substring(i + 16, j);
		game.setDescription(description);

		String[] infos = { "Published by", "Developed by", "Released", "Also For", "Genre", "Perspective", "Theme",
				"Non-Sport", "Sport", "Misc", "Country", "Release Date", "Visual", "Gameplay", "setting" };
		String[] attrs = { "publishedBy", "developedBy", "released", "alsoFor", "genre", "perspective", "theme",
				"nonSport", "sport", "misc", "country", "releaseDate", "visual", "gamePlay", "setting" };
		for (i = 0; i < infos.length; i++) {
			String info = xp.parse("//div[.='" + infos[i] + "']/following::div[1]");
			// The unicode character \u0160 is not a non-breaking space.
			info = info.replaceAll("\u00A0", " ");
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
				xpath = new XPathParser(l);
				MobyGameScreenshot mgs = new MobyGameScreenshot();
				mgs.setLink(xpath.parse("//img[contains(@src,'/images/shots/')]/@src"));
				mgs.setCaption(xpath.parse("//img[contains(@src,'/images/shots/')]/following-sibling::h3"));
				game.addScreenshot(mgs);

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
			xpath = new XPathParser(l);
			MobyGameCoverArt mgca = new MobyGameCoverArt();
			mgca.setLink(xpath.parse("//img[contains(@src,'/images/covers/')]/@src"));
			mgca.setScanOf(xpath.parse("//td[.='Scan Of']/following-sibling::td").replaceAll(":", "").trim());
			mgca.setPackaging(xpath.parse("//td[.='Packaging']/following-sibling::td").replaceAll(":", "").trim());
			if (xpath.parse("//td[.='Country']/following-sibling::td").equals("")) {
				mgca.setCountry(xpath.parse("//td[.='Countries']/following-sibling::td").replaceAll(":", "").trim());
			} else {
				mgca.setCountry(xpath.parse("//td[.='Country']/following-sibling::td").replaceAll(":", "").trim());
			}
			mgca.setPlatforms(xpath.parse("//td[.='Platforms']/following-sibling::td").replaceAll(":", "").trim());
			game.addCover(mgca);
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
	 * @param url game URL
	 * @return String in XML format.
	 * @throws Exception
	 */
	public String getGameInfo(String url) throws Exception {
		MobyGame game = this.getGameMainSummary(url);
		game = this.getGameReleaseInfo(game);
		game = this.getScreenshot(game);
		this.addGame(this.getCoverArt(game));

		// JAXBContext jc = JAXBContext.newInstance(XMLWrapper.class, MobyGame.class);
		// Marshaller marshaller = jc.createMarshaller();
		// marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		//
		// return XMLUtil.marshal(marshaller, this.getGames(), "games");

		Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
		String json = gson.toJson(game, MobyGame.class);
		return json;

	}

	/**
	 * Convert the XML to JSON to store in MONGODB database.
	 * 
	 * @param file XML file to convert.
	 * @throws Exception
	 */
	private void convertXMLtoJSON(String file) throws Exception {
		String xml = IOUtil.getContents(new File(file));
		JAXBContext jc = JAXBContext.newInstance(XMLWrapper.class, MobyGame.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		MobyGame g = XMLUtil.unmarshalString(unmarshaller, MobyGame.class, xml).get(0);
		g.setSystem(this.system);

		System.out.println(g.getName());

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

		// 01. Get all game links for the system then save the links to a text file
		String[] systems = { "atari-st" };
		for (int s = 0; s < systems.length; s++) {

			String system = systems[s];
			MobyGamesScript mgs = new MobyGamesScript(system);
			mgs.saveGamesLinksToFile(system + ".txt");

			// 02. For each game link in the text file go to the link and save meta data of
			// the game in a JSON file
			String urls = IOUtil.readFully(new FileInputStream(system + ".txt"));
			for (String url : urls.split(System.getProperty("line.separator"))) {
				if (!new File("mobygames/" + system + "/json/" + url.substring(url.lastIndexOf("/") + 1) + ".json")
						.exists()) {
					System.out.println(url);
					IOUtil.write("mobygames/" + system + "/json", url.substring(url.lastIndexOf("/") + 1) + ".json",
							mgs.getGameInfo(url));
				}
			}

			// 03. Download images
			Gson gson = new Gson();
			String[] files = new File("mobygames\\" + system + "\\json").list();
			for (String file : files) {
				System.out.println("Getting images for " + file);
				String json = IOUtil.readFully(new FileInputStream("mobygames\\" + system + "\\json\\" + file));
				MobyGame game = gson.fromJson(json, MobyGame.class);
				for (MobyGameScreenshot shot : game.getShot()) {
					String url = "https://www.mobygames.com/"
							+ shot.getLink().replaceAll("https://www.mobygames.com", "");
					String destFolder = system + "/" + game.getFolderName();
					destFolder = destFolder.replaceAll(":", "").replaceAll("[?]", "-").replaceAll("[...]", "_")
							.replaceAll("[*]", "_");
					String destFileName = shot.getLink().substring(shot.getLink().lastIndexOf("/") + 1);
					destFileName = destFileName.substring(destFileName.indexOf("screenshot-") + 11);
					new File(destFolder).mkdirs();
					if (!new File(destFolder + "/" + destFileName).exists()) {
						URLGrabber ug = new URLGrabber(url, destFolder + "/" + destFileName);
						ug.saveURLBinary();
						Thread.sleep(300);
					}
				}
				for (MobyGameCoverArt cover : game.getCover()) {
					String url = "https://www.mobygames.com/"
							+ cover.getLink().replaceAll("https://www.mobygames.com", "");
					String destFolder = system + "/" + game.getFolderName();
					destFolder = destFolder.replaceAll(":", "").replaceAll("[?]", "-").replaceAll("[...]", "_")
							.replaceAll("[*]", "_");
					String destFileName = cover.getLink().substring(cover.getLink().lastIndexOf("/") + 1);
					destFileName = destFileName
							.substring(destFileName.indexOf("-" + system + "-") + system.length() + 2);
					new File(destFolder).mkdirs();
					if (!new File(destFolder + "/" + destFileName).exists()) {
						URLGrabber ug = new URLGrabber(url, destFolder + "/" + destFileName);
						ug.saveURLBinary();
						Thread.sleep(300);
					}
				}
			}
		}

//		String urls = IOUtil.readFully(new FileInputStream("data/mobygames/all.txt"));
//		for (String url : urls.split(System.getProperty("line.separator"))) {
//			System.out.println(url);
//			MobyGamesScript mgs = new MobyGamesScript(url);
//			mgs.saveGamesLinksToFile(url + ".txt");
//		}

		// String[] files = new File("data/mobygames/MSX - MSX 1/").list();
		// for (String file : files) {
		// mgs.convertXMLtoJSON("data/mobygames/MSX - MSX 1/" + file);
		// }

//		MobyGamesScript mgs = new MobyGamesScript("msx");
//		IOUtil.write("c:/temp/temp.json", mgs.getGameInfo("https://www.mobygames.com/game/msx/abu-simbel-profanation"));
	}

}
