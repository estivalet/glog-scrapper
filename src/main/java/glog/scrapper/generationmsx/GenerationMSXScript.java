package glog.scrapper.generationmsx;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.bson.Document;
import org.w3c.dom.NodeList;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import glog.util.IOUtil;
import glog.util.URLGrabber;
import glog.util.XPathParser;

public class GenerationMSXScript {
	private List<GenerationMSXGame> games;

	public GenerationMSXScript() {
		this.games = new ArrayList<GenerationMSXGame>();
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

	private GenerationMSXGame parseInformation(String url) throws Exception {
		XPathParser xp = new XPathParser(url);

		GenerationMSXGame game = new GenerationMSXGame();
		game.setUrl(url);

		String[] indexes = { "Original title", "Developed by", "Year", "System", "Sound", "Kind", "Input Devices Supported", "Genre", "Max Players", "Max Simultaneous", "Uses Kanji", "License", "Licence", "Language", "Conversion", "Note",
				"Tagoo database entry", "Mentioned in", "Title translations", "Credits", "RAM", "VRAM", "Ported by" };
		String[] attrs = { "originalTitle", "developedBy", "year", "system", "sound", "kind", "inputDevicesSupported", "genre", "maxPlayers", "maxSimultaneous", "usesKanji", "license", "licence", "language", "conversion", "note",
				"tagooDatabaseEntry", "mentionedIn", "translatedTitle", "credits", "ram", "vram", "portedBy" };
		for (int i = 0; i < indexes.length; i++) {
			String info = null;
			if (indexes[i].equals("System")) {
				info = xp.parse("//dt[.='" + indexes[i] + "']/following::dd[1]/img/@title").trim();
			} else if (indexes[i].equals("Mentioned in")) {
				NodeList mentions = xp.parseList("//dt[.='" + indexes[i] + "']/following::dd[1]//ul/li/a");
				for (int j = 1; j <= mentions.getLength(); j++) {
					String mentionUrl = xp.parse("//dt[.='" + indexes[i] + "']/following::dd[1]//ul/li[" + j + "]/a/@href");
					String mentionText = xp.parse("//dt[.='" + indexes[i] + "']/following::dd[1]//ul/li[" + j + "]/a/text()");
					String mentionDesc = xp.parse("//dt[.='" + indexes[i] + "']/following::dd[1]//ul/li[" + j + "]").trim();
					GenerationMSXGameMentions mention = new GenerationMSXGameMentions();
					mention.setUrl(mentionUrl);
					mention.setText(mentionText);
					mention.setDescription(mentionDesc);
					game.addMention(mention);
				}
			} else if (indexes[i].equals("Conversion")) {
				NodeList conversions = xp.parseList("//dt[.='" + indexes[i] + "']/following::dd[1]//ul/li");
				for (int j = 1; j <= conversions.getLength(); j++) {
					String conversion = xp.parse("//dt[.='" + indexes[i] + "']/following::dd[1]//ul/li[" + j + "]/text()");
					game.addConversion(conversion);
				}
			} else {
				info = xp.parse("//dt[.='" + indexes[i] + "']/following::dd[1]").trim();
			}
			BeanUtils.setProperty(game, attrs[i], info);
		}

		return game;

	}

	private GenerationMSXGame parseReleases(GenerationMSXGame game, String url) throws Exception {
		XPathParser xp = new XPathParser(url);
		NodeList nl = xp.parseList("//dt[.='Published by']");
		for (int i = 1; i <= nl.getLength() + 1; i++) {
			GenerationMSXGameRelease release = new GenerationMSXGameRelease();
			release.setTitle(xp.parse("(//dt[.='Title'])[" + i + "]/following::dd[1]"));
			release.setPublisher(xp.parse("(//dt[.='Published by'])[" + i + "]/following::dd[1]"));
			release.setMedia(xp.parse("(//dt[.='Media type'])[" + i + "]/following::dd[1]/img/@title") + " " + xp.parse("(//dt[.='Media type'])[" + i + "]/following::dd[1]").trim());
			release.setProductCode(xp.parse("(//dt[.='Product code'])[" + i + "]/following::dd[1]"));
			release.setPirated(xp.parse("(//dt[.='Is pirated'])[" + i + "]/following::dd[1]"));
			release.setCountry(xp.parse("(//dt[.='Released in'])[" + i + "]/following::dd[1]").trim());
			game.addRelease(release);
		}

		// TODO parse covers found in releases (must find a way to get covers by release)
		nl = xp.parseList("//img[contains(@src,'/cover')]/parent::a");
		for (int i = 1; i <= nl.getLength() + 1; i++) {
			GenerationMSXGameMedia media = new GenerationMSXGameMedia();
			media.setUrl(xp.parse("(//img[contains(@src,'/cover')]/parent::a)[" + i + "]/@href"));
			media.setType("image");
			media.setTitle(xp.parse("(//img[contains(@src,'/cover')]/parent::a)[" + i + "]/@title"));
			game.addMedia(media);
		}

		return game;
	}

	private GenerationMSXGame parseMedia(GenerationMSXGame game, String url) throws Exception {
		XPathParser xp = new XPathParser(url);
		NodeList nl = xp.parseList("//img[contains(@src,'/software')]/parent::a");
		for (int i = 1; i <= nl.getLength() + 1; i++) {
			GenerationMSXGameMedia media = new GenerationMSXGameMedia();
			media.setUrl(xp.parse("(//img[contains(@src,'/software')]/parent::a)[" + i + "]/@href"));
			media.setType("image");
			media.setTitle(xp.parse("(//img[contains(@src,'/software')]/parent::a)[" + i + "]/@title"));
			game.addMedia(media);
		}

		return game;

	}

	public GenerationMSXGame getGameInfo(String url) throws Exception {
		GenerationMSXGame game = parseInformation(url.replace("/release", ""));
		game = parseReleases(game, url);
		game = parseMedia(game, url.replace("/release", "/media"));

		return game;
	}

	/**
	 * need to add certificate to work with https:
	 * 
	 * ..\..\bin\keytool.exe -import -trustcacerts -alias genmsx -file genmsx.cer -keystore cacerts -storepass changeit
	 * 
	 * @throws Exception
	 */
	public void grabLinksHttps() throws Exception {
		Set<String> links = new HashSet<String>();
		for (int page = 1; page <= 305; page++) {
			System.out.println("Grabbing links from page number " + page);
			XPathParser xp = new XPathParser("https://www.generation-msx.nl/software/result?q=&searchtype=&p=" + page);
			for (int i = 1; i <= xp.parseList("//table/tbody/tr/td[1]/a/@href").getLength(); i++) {
				String href = xp.parse("(//table/tbody/tr/td[1]/a/@href)[" + i + "]");
				links.add(href);
			}
		}

		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("generationmsx.txt", true)));
		for (String link : links) {
			out.println(link);
		}
		out.close();
	}

	public static void main(String[] args) throws Exception {
		// List<String> lines = FileUtils.readLines(new File("data/generationmsx.txt"));
		// for (String line : lines) {
		// System.out.println(line);
		// GenerationMSXScript g = new GenerationMSXScript();
		// // g.grabLinksHttps();
		// String url = "https://www.generation-msx.nl" + line;
		// GenerationMSXGame game = g.getGameInfo(url);
		// Gson gson = new GsonBuilder().setPrettyPrinting().create();
		// String json = gson.toJson(game);
		// System.out.println(json);
		//
		// String name = url.substring(0, url.length() - 1);
		// name = name.substring(name.lastIndexOf("/") + 1);
		//
		// FileUtils.write(new File("data/generationmsx/" + name + ".json"), json, "UTF-8");
		// }

		// Store in mongodb.
		int port_no = 27017;
		String host_name = "localhost", db_name = "msxdb", db_coll_name = "generationmsx";
		String client_url = "mongodb://" + host_name + ":" + port_no + "/" + db_name;
		MongoClientURI uri = new MongoClientURI(client_url);
		MongoClient mongo_client = new MongoClient(uri);
		MongoDatabase db = mongo_client.getDatabase(db_name);
		MongoCollection<Document> coll = db.getCollection(db_coll_name);
		String[] files = new File("data/generationmsx").list();
		for (String file : files) {
			System.out.println(file);
			String json = IOUtil.readFully(new FileInputStream("data/generationmsx/" + file));
			Document doc = Document.parse(json);
			coll.insertOne(doc);
		}
		System.exit(0);

		String script = "";
		for (String file : new File("data/generationmsx/").list()) {
			if (file.equals("images") || file.equals("processed")) {
				continue;
			}
			String json = FileUtils.readFileToString(new File("data/generationmsx/" + file));
			Gson gson = new Gson();
			GenerationMSXGame game = gson.fromJson(json, GenerationMSXGame.class);
			String url = game.getUrl();
			url = url.substring(0, url.length() - 1);
			url = url.substring(0, url.lastIndexOf("/"));
			url = url.substring(url.lastIndexOf("/") + 1);
			char ch = url.substring(0, 1).charAt(0);
			String folder = "";
			if (ch >= 48 && ch <= 57) {
				folder = "0-9";
			} else {
				folder = String.valueOf(ch).toLowerCase();
			}
			new File("data/generationmsx/images/" + folder + "/" + url).mkdirs();
			for (GenerationMSXGameMedia media : game.getMedias()) {
				if (!"".equals(media.getUrl())) {
					script += "C:/Users/luisoft/apps/curl-7.61.1-win64-mingw/bin/curl -k \"" + "https:" + media.getUrl() + "\" > \"" + "data/generationmsx/images/" + folder + "/" + url + "/"
							+ media.getUrl().substring(media.getUrl().lastIndexOf("/") + 1) + "\"\ntimeout 5\n";
				}
			}
		}
		IOUtil.write("c:/temp/download.bat", script);
	}
}
