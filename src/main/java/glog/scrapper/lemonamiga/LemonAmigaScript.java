package glog.scrapper.lemonamiga;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.w3c.dom.NodeList;

import glog.util.URLGrabber;
import glog.util.XMLUtil;
import glog.util.XMLWrapper;
import glog.util.XPathParser;

public class LemonAmigaScript {

	private static final String LEMON_URL = "http://www.lemonamiga.com/";
	private static final String GAMES_URL = LEMON_URL + "games/";
	private static final String LIST_GAMES_URL = GAMES_URL + "list.php?&lineoffset=";
	private static final String XPATH_GAME_DETAILS = "//img[@alt='Details']/parent::a/@href";
	private static final String XPATH_TOTAL_GAMES = "//td/b/text()";
	private static final int PAGE_SIZE = 27;
	private static final String OUTPUT_FILE = "lemonamiga.txt";
	private List<LemonAmigaGame> games = new ArrayList<LemonAmigaGame>();

	private void saveGamesLinksToFile() throws Exception {
		Vector<String> links = new Vector<String>();
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FILE, true)));
		XPathParser xp = new XPathParser(LIST_GAMES_URL);
		int total = Integer.parseInt(xp.parse(XPATH_TOTAL_GAMES));
		int pages = (int) Math.ceil((double) total / PAGE_SIZE);
		for (int p = 0; p < pages; p++) {
			System.out.println(LIST_GAMES_URL + p * PAGE_SIZE);
			xp = new XPathParser(LIST_GAMES_URL + p * PAGE_SIZE);
			NodeList nl = xp.parseList(XPATH_GAME_DETAILS);
			for (int i = 0; i < nl.getLength(); i++) {
				System.out.println(nl.item(i).getNodeValue());
				links.add(nl.item(i).getNodeValue());
			}
		}
		for (String link : links) {
			out.println(GAMES_URL + link);
		}
		out.close();
	}

	protected org.w3c.dom.Document getDOM(String link) throws Exception {
		URLGrabber ug = new URLGrabber(link);
		String contents = ug.getContents();
		TagNode tagNode = new HtmlCleaner().clean(contents);

		return new DomSerializer(new CleanerProperties()).createDOM(tagNode);
	}

	/**
	 * @param link
	 * @throws Exception
	 */
	private LemonAmigaGame parseGame(String link) throws Exception {
		LemonAmigaGame game = new LemonAmigaGame();
		game.setUrl(link);

		org.w3c.dom.Document doc = getDOM(link);
		XPath xpath = XPathFactory.newInstance().newXPath();

		String name = (String) xpath.evaluate("//strong[@class='textGameHeader']/font", doc, XPathConstants.STRING);
		System.out.println(name);
		game.setName(name);

		String[] infos = { "Developer:", "Design:", "Manager:", "Producer:", "Coder:", "Graphics:", "Musician:", "Hardware:", "Disks:", "Genre:", "License:", "Language:", "Players:", "Relationship:" };
		String[] attrs = { "developer", "design", "manager", "producer", "coder", "graphics", "musician", "hardware", "disks", "genre", "license", "language", "players", "relationship" };
		for (int i = 0; i < infos.length; i++) {
			String credit = (String) xpath.evaluate("//td[.='" + infos[i] + "']/following::td[@class='gameCredits'][1]", doc, XPathConstants.STRING);
			BeanUtils.setProperty(game, attrs[i], credit.trim());
		}

		NodeList magazineReviews = (NodeList) xpath.evaluate("//a[contains(@href,'amr')]/@href", doc, XPathConstants.NODESET);
		for (int i = 0; i < magazineReviews.getLength(); i++) {
			LemonAmigaMagazineReview lamr = new LemonAmigaMagazineReview();
			String magazineLink = magazineReviews.item(i).getTextContent();
			String magazine = (String) xpath.evaluate("//a[@href='" + magazineLink + "']/span[1]", doc, XPathConstants.STRING);
			lamr.setMagazine(magazine);
			lamr.setLink(magazineLink);
			game.addReview(lamr);
		}

		// Look for boxes
		NodeList pics = (NodeList) xpath.evaluate("//img[contains(@src,'/games/boxes')]/ancestor::a[1]/@href", doc, XPathConstants.NODESET);
		for (int i = 0; i < pics.getLength(); i++) {
			parseBox(game, "http://www.lemonamiga.com/games/" + pics.item(i).getTextContent().replace("&amp;", "&"));
		}

		parseScreens(game, link);
		parseAdverts(game, link);

		return game;

	}

	/**
	 * @param link
	 * @throws Exception
	 */
	private LemonAmigaGame parseBox(LemonAmigaGame game, String link) throws Exception {
		org.w3c.dom.Document doc = getDOM(link);
		XPath xpath = XPathFactory.newInstance().newXPath();

		NodeList pics = (NodeList) xpath.evaluate("//img[contains(@src,'/boxes/full')]/@src", doc, XPathConstants.NODESET);
		for (int i = 0; i < pics.getLength(); i++) {
			game.addBox("http://www.lemonamiga.com" + pics.item(i).getTextContent());
		}

		return game;
	}

	/**
	 * @param link
	 * @throws Exception
	 */
	private LemonAmigaGame parseScreens(LemonAmigaGame game, String link) throws Exception {
		link = link.replace("/details.php", "/screens.php");
		org.w3c.dom.Document doc = getDOM(link);
		XPath xpath = XPathFactory.newInstance().newXPath();

		NodeList pics = (NodeList) xpath.evaluate("//img/@src", doc, XPathConstants.NODESET);
		for (int i = 0; i < pics.getLength(); i++) {
			game.addShot(pics.item(i).getTextContent().replace("/small/", "/full/"));
		}

		return game;

	}

	/**
	 * @param link
	 * @throws Exception
	 */
	private void parseAdverts(LemonAmigaGame game, String link) throws Exception {
		link = link.replace("/details.php", "/advert.php");
		org.w3c.dom.Document doc = getDOM(link);
		XPath xpath = XPathFactory.newInstance().newXPath();

		NodeList pics = (NodeList) xpath.evaluate("//img[contains(@src,'/adverts/full')]/@src", doc, XPathConstants.NODESET);
		for (int i = 0; i < pics.getLength(); i++) {
			game.addAdvert(pics.item(i).getTextContent());
		}

	}

	public String getGameInfo(String url) throws Exception {
		LemonAmigaGame game = this.parseGame(url);
		this.games.add(game);

		JAXBContext jc = JAXBContext.newInstance(XMLWrapper.class, LemonAmigaGame.class);
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		return XMLUtil.marshal(marshaller, this.games, "games");
	}

	public static void main(String[] args) throws Exception {
		// LemonAmigaScript las = new LemonAmigaScript();
		// las.saveGamesLinksToFile();
		// System.out.println(las.getGameInfo("http://www.lemonamiga.com/games/details.php?id=935"));

		List<String> lines = FileUtils.readLines(new File("/luisoft/development/apps/glog-service/lemonamiga.txt"));
		for (String line : lines) {
			LemonAmigaScript las = new LemonAmigaScript();
			String fileName = "lemonamiga/" + line.substring(line.indexOf("=") + 1) + ".xml";
			System.out.println(fileName);
			if (!new File(fileName).exists()) {
				FileUtils.write(new File(fileName), las.getGameInfo(line));
			}
		}

	}
}
