package glog.scrapper.msxgamesworld;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.bson.Document;
import org.w3c.dom.NodeList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import glog.util.IOUtil;
import glog.util.URLGrabber;
import glog.util.XPathParser;

/**
 * New version of the site so I need to have a new script, still keeping old info
 * 
 * @author luisoft
 *
 */
public class MSXGamesWorldScriptV2 {
	private static final String MSXGAMESWORLD_URL = "http://www.msxgamesworld.com/results.php?type=game";
	private static final String GAMES_URL = MSXGAMESWORLD_URL + "&page=ppp&type=game&sort=nom&dir=asc";
	private static final String XPATH_GAME_LINK = "//a[contains(@href,'gamecard.php')]/@href";
	private static final String OUTPUT_FILE = "msxgamesworld.txt";

	private void saveGamesLinksToFile() throws Exception {

		// Find number of pages in the site.
		XPathParser xp = new XPathParser(MSXGAMESWORLD_URL);
		String contents = xp.getPageContents();
		int i = contents.indexOf("items</strong>  in");
		int j = contents.indexOf("pages", i);
		int pages = Integer.parseInt(contents.substring(i + 19, j - 1));

		// Loop through each page and save the link to a file for later processing.
		Vector<String> links = new Vector<String>();
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FILE, true)));
		for (int p = 1; p <= pages; p++) {
			// page 25 has a bug... need to manually grab the links
			if (p == 25) {
				continue;
			}
			System.out.println(p);
			xp = new XPathParser(GAMES_URL.replace("ppp", String.valueOf(p)));
			NodeList nl = xp.parseList(XPATH_GAME_LINK);
			for (i = 0; i < nl.getLength(); i++) {
				System.out.println("adding " + nl.item(i).getNodeValue());
				links.add(nl.item(i).getNodeValue());
			}
		}
		for (String link : links) {
			out.println(MSXGAMESWORLD_URL + link);
		}
		out.close();
	}

	private String getGameMainSummary(String url) throws Exception {
		String json = "";
		XPathParser xp = new XPathParser(url);
		String name = xp.parse("//h1[@class='project-title text-primary']/text()");
		System.out.println(name.trim());
		json = "{\"name\" : \"" + name + "\",";
		json += "\"url\" : \"" + url + "\",";

		NodeList features = xp.parseList("//strong[@class='feature-title text-uppercase text-primary']/text()");
		for (int i = 0; i < features.getLength(); i++) {
			String feature = features.item(i).getNodeValue();
			System.out.println(feature);
			feature = feature.replace("Num.", "Number").replace("Dep.", "Depto");
			json += "\"" + feature + "\":";

			NodeList featureDesc = xp.parseList("//strong[@class='feature-title text-uppercase text-primary' and text()='" + feature + "']/following::div[1]/*");
			if (featureDesc.getLength() > 1) {
				json += "[";
				boolean ok = false;
				for (int j = 0; j < featureDesc.getLength(); j++) {
					if (featureDesc.item(j).getFirstChild() != null) {
						System.out.println("=" + featureDesc.item(j).getFirstChild().getNodeValue());
						json += "\"" + featureDesc.item(j).getFirstChild().getNodeValue() + "\",";
						ok = true;
					}
				}
				if (!ok) {
					json += "\"\",";
				}
				json = json.substring(0, json.length() - 1);
				json += "],";
			} else {
				String value = xp.parse("//strong[@class='feature-title text-uppercase text-primary' and text()='" + feature + "']/following::div[1]/text()");
				if ("".equals(value.trim())) {
					value = xp.parse("//strong[@class='feature-title text-uppercase text-primary' and text()='" + feature + "']/following::div[1]/a/text()");
				}
				System.out.println("=" + value);
				json += "\"" + value + "\",";
			}
		}

		json = json.substring(0, json.length() - 1);

		int caratulas = xp.parseList("//img[contains(@src,'caratulas')]").getLength();
		if (caratulas > 0) {
			json += ",\"caratulas\":[";
			for (int i = 1; i <= caratulas; i++) {
				String caratula = xp.parse("(//img[contains(@src,'caratulas')])[" + i + "]/@src");
				System.out.println(caratula);
				json += "\"" + caratula + "\",";
			}
			json = json.substring(0, json.length() - 1);
			System.out.println(json += "]");
		}

		int capturas = xp.parseList("//img[contains(@src,'capturas')]").getLength();
		if (capturas > 0) {
			json += ",\"capturas\":[";
			for (int i = 1; i <= capturas; i++) {
				String captura = xp.parse("(//img[contains(@src,'capturas')])[" + i + "]/@src");
				System.out.println(captura);
				json += "\"" + captura + "\",";
			}
			json = json.substring(0, json.length() - 1);
			System.out.println(json += "]");
		}

		NodeList publications = xp.parseList("//div[contains(@id,'pub-')]/descendant::i/ancestor::div[1]");
		if (publications.getLength() > 0) {
			json += ",\"publications\":[";
			for (int i = 0; i < publications.getLength(); i++) {
				String publication = xp.parse("//div[contains(@id,'pub-')][" + (i + 1) + "]/descendant::i/ancestor::div[1]").trim();
				json += "\"" + publication + "\",";
			}
			json = json.substring(0, json.length() - 1);
			System.out.println(json += "]");
		}

		NodeList releases = xp.parseList("//h4[.='Releases']/following::span[@class='badge badge-secondary mt-3']");
		if (releases.getLength() > 0) {
			json += ",\"releases\":[";
			for (int i = 0; i < releases.getLength(); i++) {
				json += "{";
				System.out.println("Release " + i);
				System.out.println("-------------");
				NodeList infos = xp.parseList("//h4[.='Releases']/following::span[@class='badge badge-secondary mt-3'][" + (i + 1) + "]/following::div[@class='row'][1]/descendant::dt");
				for (int j = 0; j < infos.getLength(); j++) {
					String info = infos.item(j).getFirstChild().getNodeValue();
					json += "\"" + info + "\":";
					System.out.println("--->" + info + "==" + infos.item(j).getNextSibling().getChildNodes().getLength());
					if (infos.item(j).getNextSibling().getChildNodes().getLength() > 1) {
						json += "[";
					}
					for (int k = 0; k < infos.item(j).getNextSibling().getChildNodes().getLength(); k++) {
						boolean ok = false;
						if (infos.item(j).getNextSibling().getChildNodes().item(k).getFirstChild() != null) {
							String data = infos.item(j).getNextSibling().getChildNodes().item(k).getFirstChild().getNodeValue();
							json += "\"" + data.replace("\\", "") + "\",";
							ok = true;
						} else {
							if (infos.item(j).getNextSibling().getChildNodes().item(k).getNodeValue() != null && !infos.item(j).getNextSibling().getChildNodes().item(k).getNodeValue().trim().equals("")) {
								String data = infos.item(j).getNextSibling().getChildNodes().item(k).getNodeValue();
								json += "\"" + data.replace("\\", "") + "\",";
								ok = true;
							}
						}
						if (!ok) {
							json += "\"\",";
						}
					}

					if (infos.item(j).getNextSibling().getChildNodes().getLength() > 1) {
						json = json.substring(0, json.length() - 1);
						json += "],";
					}

				}
				json = json.substring(0, json.length() - 1);

				json += "},";
			}
			json = json.substring(0, json.length() - 1);
			json += "]";
		}

		NodeList packs = xp.parseList("//h4[.='Pack, collection, bundle']/following::ul[1]/descendant::li");
		if (packs.getLength() > 0) {
			json += ",\"packs\":[";
			for (int i = 0; i < packs.getLength(); i++) {
				json += "\"" + packs.item(i).getTextContent().trim() + "\",";
			}
			json = json.substring(0, json.length() - 1);
			json += "]";
		}

		NodeList series = xp.parseList("//h4[.='Series or sagas']/following::ul[1]/descendant::li");
		if (series.getLength() > 0) {
			json += ",\"series\":[";
			for (int i = 0; i < series.getLength(); i++) {
				json += "\"" + series.item(i).getTextContent() + "\",";
			}
			json = json.substring(0, json.length() - 1);
			json += "]";
		}

		NodeList misc = xp.parseList("//h4[.='Miscellaneous']/following::ul[1]/descendant::li/a");
		if (misc.getLength() > 0) {
			json += ",\"misc\":[";
			for (int i = 0; i < misc.getLength(); i++) {
				json += "\"" + misc.item(i).getAttributes().getNamedItem("href").getTextContent().replace("\"", "'") + "\",";
			}
			json = json.substring(0, json.length() - 1);
			json += "]";
		}

		NodeList links = xp.parseList("//h4[.='External links']/following::ul[1]/descendant::li/a");
		if (links.getLength() > 0) {
			json += ",\"links\":[";
			for (int i = 0; i < links.getLength(); i++) {
				json += "\"" + links.item(i).getAttributes().getNamedItem("href").getTextContent().replace("\"", "'") + "\",";
			}
			json = json.substring(0, json.length() - 1);
			json += "]";
		}

		json += "}";
		System.out.println(json);
		Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
		JsonElement element = gson.fromJson(json, JsonElement.class);
		System.out.println();

		return gson.toJson(element);

	}

	private void downloadImages() throws Exception {
		String[] filess = new File("data/msxgamesworldv2").list();
		String downloads = "";
		for (String file : filess) {
			System.out.println(file);
			if (file.equals("images") || file.equals("processed")) {
				continue;
			}
			String json = IOUtil.readFully(new FileInputStream("data/msxgamesworldv2/" + file));
			JsonElement jelement = new JsonParser().parse(json);
			JsonObject jobject = jelement.getAsJsonObject();
			if (jobject.getAsJsonArray("caratulas") == null) {
				continue;
			}
			for (int i = 0; i < jobject.getAsJsonArray("caratulas").size(); i++) {
				String captura = jobject.getAsJsonArray("caratulas").get(i).toString().replaceAll("\"", "");
				String f = captura.substring(captura.lastIndexOf("/") + 1);
				captura = captura.replaceAll(" ", "%20").replaceAll("&amp;", "&").replaceAll("&iacute;", "í");

				char ch = f.substring(0, 1).charAt(0);
				String folder = "";
				if (ch >= 48 && ch <= 57) {
					folder = "0-9";
				} else {
					folder = String.valueOf(ch).toLowerCase();
				}
				System.out.println(f + " -> " + folder);
				System.out.println("-------->" + "http://www.msxgamesworld.com/" + captura);
				URLGrabber ug = new URLGrabber("http://www.msxgamesworld.com/" + captura, "C:/Users/luisoft/git/glog-scrapper/data/msxgamesworldv2/images/caratulas/" + folder + "/" + f);
				File ft = new File("C:/Users/luisoft/git/glog-scrapper/data/msxgamesworldv2/images/caratulas/" + folder + "/" + f);
				if (ft.exists() && (ft.length() == 0 || ft.length() == 221)) {
					System.out.println("delleting " + "C:/Users/luisoft/git/glog-scrapper/data/msxgamesworldv2/images/caratulas/" + folder + "/" + f);
					System.out.println("DELETED->" + ft.delete());
					downloads += "C:/Users/luisoft/apps/curl-7.61.1-win64-mingw/bin/curl -k \"" + "http://www.msxgamesworld.com/" + captura + "\" > \"" + f + "\"\n";
					downloads += "timeout 15\n";
				} else if (!ft.exists()) {
					downloads += "C:/Users/luisoft/apps/curl-7.61.1-win64-mingw/bin/curl -k \"" + "http://www.msxgamesworld.com/" + captura + "\" > \"" + f + "\"\n";
					downloads += "timeout 15\n";

				}
				// if (ug.saveURLBinary()) {
				// System.out.println("saved " + "C:/Users/luisoft/git/glog-scrapper/msxgamesworldv2/images/capturas/" + folder + "/" + f);
				// System.out.println("waiting");
				// Thread.sleep(15000);
				// }
			}
		}
		System.out.println("_-----------------------");
		// System.out.println(downloads);
		IOUtil.write("c:/temp/download.bat", downloads);

	}

	public static void main(String[] args) throws Exception {
		// URLGrabber ug = new URLGrabber("http://www.msxgamesworld.com/images/capturas/Fernando%20Martín%20Basket%20Master%20(Dinamic,%201986)%20002.png", "C:/Users/luisoft/git/glog-scrapper/msxgamesworldv2/test.png");
		// ug.saveURLBinary();
		// System.exit(0);

		// HttpDownloadUtility.downloadFile("http://www.msxgamesworld.com/images/capturas/Fernando%20Martín%20Basket%20Master%20(Dinamic,%201986)%20002.png", "C:/Users/luisoft/git/glog-scrapper/msxgamesworldv2/");
		// System.exit(0);

		// separate files into folders.
		// String[] filesss = new File("data/msxgamesworldv2/images/capturas").list();
		// for (String f : filesss) {
		// if (new File("data/msxgamesworldv2/images/capturas/" + f).isDirectory()) {
		// continue;
		// }
		// System.out.println(f);
		// char ch = f.substring(0, 1).charAt(0);
		// String folder = "";
		// if (ch >= 48 && ch <= 57) {
		// folder = "0-9";
		// } else {
		// folder = String.valueOf(ch).toLowerCase();
		// }
		// new File("data/msxgamesworldv2/images/capturas/" + folder).mkdirs();
		// Files.move(new File("data/msxgamesworldv2/images/capturas/" + f).toPath(), new File("data/msxgamesworldv2/images/capturas/" + folder + "/" + f).toPath(), StandardCopyOption.REPLACE_EXISTING);
		// }
		// System.exit(0);

		// test only
		// MSXGamesWorldScriptV2 g2 = new MSXGamesWorldScriptV2();
		// System.out.println(g2.getGameMainSummary("http://www.msxgamesworld.com/gamecard.php?id=1171"));
		// System.exit(0);

		// Download images.
		// MSXGamesWorldScriptV2 g = new MSXGamesWorldScriptV2();
		// g.downloadImages();
		// System.exit(0);

		// Store in mongodb.
		int port_no = 27017;
		String host_name = "localhost", db_name = "msxdb", db_coll_name = "msxgamesworld";
		String client_url = "mongodb://" + host_name + ":" + port_no + "/" + db_name;
		MongoClientURI uri = new MongoClientURI(client_url);
		MongoClient mongo_client = new MongoClient(uri);
		MongoDatabase db = mongo_client.getDatabase(db_name);
		MongoCollection<Document> coll = db.getCollection(db_coll_name);
		String[] files = new File("data/msxgamesworldv2/processed").list();
		for (String file : files) {
			System.out.println(file);
			String json = IOUtil.readFully(new FileInputStream("data/msxgamesworldv2/processed/" + file));
			Document doc = Document.parse(json);
			coll.insertOne(doc);
		}

		System.exit(0);

		MSXGamesWorldScriptV2 mgws = new MSXGamesWorldScriptV2();
		// mgws.saveGamesLinksToFile();
		// System.exit(0);

		List<String> lines = FileUtils.readLines(new File("msxgamesworld.txt"));
		for (String line : lines) {
			mgws = new MSXGamesWorldScriptV2();
			String fileName = "msxgamesworldv2/" + line.substring(line.lastIndexOf("=") + 1) + ".json";
			if (!new File(fileName).exists()) {
				System.out.println("Scrapping... " + fileName);
				FileUtils.write(new File(fileName), mgws.getGameMainSummary(line), "UTF-8");
				// System.exit(0);
			}
		}
	}

}
