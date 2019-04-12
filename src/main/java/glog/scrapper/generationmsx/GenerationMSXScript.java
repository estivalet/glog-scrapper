package glog.scrapper.generationmsx;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.NodeList;

import com.google.gson.Gson;

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
		String name = url.substring(0, url.length() - 1);
		name = name.substring(name.lastIndexOf("/") + 1);
		FileUtils.write(new File("data/generationmsx/html/" + name + ".html"), xp.getPageContents(), "UTF-8");

		GenerationMSXGame game = new GenerationMSXGame();
		game.setUrl(url);

		String[] indexes = { "Original title", "Developed by", "Year", "System", "Sound", "Kind", "Input Devices Supported", "Genre", "Max Players", "Max Simultaneous", "Uses Kanji", "License", "Licence", "Language", "Conversion", "Note",
				"Tagoo database entry", "Mentioned in", "Title translations", "Credits", "RAM", "VRAM", "Ported by", "See also", "Groups" };
		String[] attrs = { "originalTitle", "developedBy", "year", "system", "sound", "kind", "inputDevicesSupported", "genre", "maxPlayers", "maxSimultaneous", "usesKanji", "license", "licence", "language", "conversion", "note",
				"tagooDatabaseEntry", "mentionedIn", "translatedTitle", "credits", "ram", "vram", "portedBy", "seeAlso", "group" };
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
			} else if (indexes[i].equals("See also")) {
				NodeList seeAlsos = xp.parseList("//dt[.='" + indexes[i] + "']/following::dd[1]//ul/li");
				for (int j = 1; j <= seeAlsos.getLength(); j++) {
					String seeAlso = xp.parse("//dt[.='" + indexes[i] + "']/following::dd[1]//ul/li[" + j + "]/a/text()");
					game.addSeealso(seeAlso);
				}
			} else if (indexes[i].equals("Groups")) {
				NodeList groups = xp.parseList("//dt[.='" + indexes[i] + "']/following::dd[1]//ul/li");
				for (int j = 1; j <= groups.getLength(); j++) {
					String group = xp.parse("//dt[.='" + indexes[i] + "']/following::dd[1]//ul/li[" + j + "]/a/text()");
					game.addGroup(group);
				}
			} else if (indexes[i].equals("Language")) {
				String langs = "";
				for (int l = 1; l <= xp.parseList("//dt[.='" + indexes[i] + "']/following::dd[1]/node()").getLength(); l++) {
					if (!"".equals(xp.parse("//dt[.='" + indexes[i] + "']/following::dd[1]/node()[" + l + "]"))) {
						langs += xp.parse("//dt[.='" + indexes[i] + "']/following::dd[1]/node()[" + l + "]") + ",";
					}
				}
				info = langs;
			} else if (indexes[i].equals("Genre")) {
				String genres = "";
				for (int l = 1; l <= xp.parseList("//dt[.='" + indexes[i] + "']/following::dd[1]/node()").getLength(); l++) {
					if (!"".equals(xp.parse("//dt[.='" + indexes[i] + "']/following::dd[1]/node()[" + l + "]"))) {
						genres += xp.parse("//dt[.='" + indexes[i] + "']/following::dd[1]/node()[" + l + "]") + ",";
					}
				}
				info = genres;
			} else {
				info = xp.parse("//dt[.='" + indexes[i] + "']/following::dd[1]").trim();
			}
			BeanUtils.setProperty(game, attrs[i], info);
		}

		return game;

	}

	private GenerationMSXPublication parsePubInformation(String url) throws Exception {
		XPathParser xp = new XPathParser(url);
		String name = url.substring(0, url.length() - 1);
		name = name.substring(name.lastIndexOf("/") + 1);

		GenerationMSXPublication pub = new GenerationMSXPublication();
		pub.setTitle(xp.parse("//h1/text()"));
		pub.setUrl(url);
		pub.setLink(xp.parse("//a[.='VIEW ONLINE']/@href"));

		String[] indexes = { "Publisher", "Publication", "Type", "Authors", "Isbn-10", "Language", "Format", "# of Pages", "Price", "Software mentioned", "Hardware mentioned", "Note" };
		String[] attrs = { "publisher", "publication", "type", "author", "isbn10", "language", "format", "pages", "price", "mentions", "hardware", "note" };
		for (int i = 0; i < indexes.length; i++) {
			String info = null;

			if (indexes[i].equals("Software mentioned")) {
				String mentions = "";
				for (int l = 1; l <= xp.parseList("//dt[.='" + indexes[i] + "']/following::dd[1]/ul/li").getLength(); l++) {
					if (!"".equals(xp.parse("//dt[.='" + indexes[i] + "']/following::dd[1]/ul/li[" + l + "]"))) {
						mentions += xp.parse("//dt[.='" + indexes[i] + "']/following::dd[1]/ul/li[" + l + "]").replaceAll("\\s+", " ").replace('\u00A0', ' ').trim() + ",";
					}
				}
				info = mentions;
			} else if (indexes[i].equals("Hardware mentioned")) {
				String mentions = "";
				for (int l = 1; l <= xp.parseList("//dt[.='" + indexes[i] + "']/following::dd[1]/ul/li").getLength(); l++) {
					if (!"".equals(xp.parse("//dt[.='" + indexes[i] + "']/following::dd[1]/ul/li[" + l + "]"))) {
						mentions += xp.parse("//dt[.='" + indexes[i] + "']/following::dd[1]/ul/li[" + l + "]").replaceAll("\\s+", " ").replace('\u00A0', ' ').trim() + ",";
					}
				}
				info = mentions;

			} else if (indexes[i].equals("Authors")) {

				String authors = "";
				for (int l = 1; l <= xp.parseList("//dt[.='" + indexes[i] + "']/following::dd[1]/node()").getLength(); l++) {
					if (!"".equals(xp.parse("//dt[.='" + indexes[i] + "']/following::dd[1]/node()[" + l + "]"))) {
						authors += xp.parse("//dt[.='" + indexes[i] + "']/following::dd[1]/node()[" + l + "]").replaceAll("\\s+", " ").replace('\u00A0', ' ').trim() + ",";
					}
				}
				info = authors;
			} else {
				info = xp.parse("//dt[.='" + indexes[i] + "']/following::dd[1]").trim();
			}
			BeanUtils.setProperty(pub, attrs[i], info);
		}

		NodeList nl3 = xp.parseList("//img[contains(@src,'/publication')]/parent::a");
		for (int c = 1; c <= nl3.getLength(); c++) {
			GenerationMSXGameMedia media = new GenerationMSXGameMedia();
			media.setUrl(xp.parse("(//img[contains(@src,'/publication')]/parent::a)[" + c + "]/@href"));
			pub.addMedia(media);

		}

		return pub;

	}

	private GenerationMSXGame parseReleases2(GenerationMSXGame game, String url) throws Exception {
		XPathParser xp = new XPathParser(url);
		String contents = xp.getPageContents();

		NodeList nl = xp.parseList("//a[contains(@href,'#rel_')]");
		for (int i = 0; i < nl.getLength(); i++) {
			String refname = nl.item(i).getAttributes().getNamedItem("href").getNodeValue().replaceAll("#", "");
			String relname = nl.item(i).getTextContent();

			int x = contents.indexOf("<a name=\"" + refname + "\"");
			int y = contents.indexOf("<hr>", x);
			if (y < 0) {
				y = contents.length();
			}

			String subcontents = "<html>" + contents.substring(x, y) + "</html>";
			XPathParser xp2 = new XPathParser(null, subcontents);
			NodeList nl2 = xp2.parseList("//dt[.='Published by']");
			GenerationMSXGameRelease release = new GenerationMSXGameRelease();
			release.setName(relname.trim());
			for (int k = 1; k < nl2.getLength() + 1; k++) {
				release.setTitle(xp2.parse("(//dt[.='Title'])[" + k + "]/following::dd[1]"));
				release.setPublisher(xp2.parse("(//dt[.='Published by'])[" + k + "]/following::dd[1]"));
				release.setMedia(xp2.parse("(//dt[.='Media type'])[" + k + "]/following::dd[1]/img/@title") + " " + xp2.parse("(//dt[.='Media type'])[" + k + "]/following::dd[1]").trim());
				release.setProductCode(xp2.parse("(//dt[.='Product code'])[" + k + "]/following::dd[1]"));
				release.setPirated(xp2.parse("(//dt[.='Is pirated'])[" + k + "]/following::dd[1]"));
				release.setManual(xp2.parse("(//dt[.='Manual'])[" + k + "]/following::dd[1]/a/@href"));

				if (!"".equals(xp2.parse("(//dt[.='Released in'])[" + k + "]/following::dd[1]/ul/li"))) {
					NodeList nl3 = xp2.parseList("(//dt[.='Released in'])[" + k + "]/following::dd[1]/ul/li");
					for (int l = 1; l < nl3.getLength(); l++) {
						release.addCountry(xp2.parse("(//dt[.='Released in'])[" + k + "]/following::dd[1]/ul/li[" + l + "]/text()").trim());
					}
				} else {
					release.addCountry(xp2.parse("(//dt[.='Released in'])[" + k + "]/following::dd[1]").trim());

				}

			}

			NodeList nl3 = xp2.parseList("//img[contains(@src,'/cover')]/parent::a");
			for (int c = 1; c <= nl3.getLength(); c++) {
				GenerationMSXGameMedia media = new GenerationMSXGameMedia();
				media.setUrl(xp2.parse("(//img[contains(@src,'/cover')]/parent::a)[" + c + "]/@href"));
				media.setType("image");
				media.setTitle(xp2.parse("(//img[contains(@src,'/cover')]/parent::a)[" + c + "]/@title"));
				release.addMedia(media);

			}

			// maybe it's a compilation.
			if (release.getMedias().size() == 0) {
				int z = contents.indexOf("<div class=\"container\">", x);
				release.setDescription(contents.substring(x, z));
			}

			game.addRelease(release);

		}

		return game;
	}

	private GenerationMSXGame parseMedia(GenerationMSXGame game, String url) throws Exception {
		XPathParser xp = new XPathParser(url);
		NodeList nl = xp.parseList("//img[contains(@src,'/software')]/parent::a");
		for (int i = 1; i <= nl.getLength(); i++) {
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
		game = parseReleases2(game, url);
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
		for (int page = 1; page <= 307; page++) {
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

	public void grabPublicationsLinksHttps() throws Exception {
		Set<String> links = new HashSet<String>();
		for (int page = 1; page <= 59; page++) {
			System.out.println("Grabbing links from page number " + page);
			XPathParser xp = new XPathParser("https://www.generation-msx.nl/publication/result?q=&searchtype=&p=" + page);
			for (int i = 1; i <= xp.parseList("//table/tbody/tr/td[1]/a/@href").getLength(); i++) {
				String href = xp.parse("(//table/tbody/tr/td[1]/a/@href)[" + i + "]");
				links.add(href);
			}
		}

		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("generationmsx-publications.txt", true)));
		for (String link : links) {
			out.println(link);
		}
		out.close();
	}

	public void grabCompaniesLinks() throws Exception {
		Set<String> links = new HashSet<String>();
		// 32 per page...
		String index = "abcdefghijklmnopqrstuvwxyz";
		for (int letter = 0; letter < index.length(); letter++) {
			XPathParser xp = new XPathParser("https://www.generation-msx.nl/company?page=1&index=" + index.substring(letter, letter + 1));
			String contents = xp.getPageContents();
			int x = contents.indexOf("results found with the letter");
			contents = contents.substring(0, x);
			int y = contents.lastIndexOf("<br/>");
			System.out.println("_-->" + contents.substring(y + 5, x));
			int pages = (int) Math.ceil(Integer.parseInt(contents.substring(y + 5, x).trim().replaceAll("\\s+", "")) / 32.0);
			System.out.println(pages);
			for (int page = 1; page <= pages; page++) {
				System.out.println("https://www.generation-msx.nl/company?page=" + page + "&index=" + index.substring(letter, letter + 1));
				xp = new XPathParser("https://www.generation-msx.nl/company?page=" + page + "&index=" + index.substring(letter, letter + 1));
				for (int i = 1; i <= xp.parseList("//a[contains(@href,'/company')]").getLength(); i++) {
					String href = xp.parse("(//a[contains(@href,'/company')]/@href)[" + i + "]");
					if (!href.contains("index=") && !"/company".equals(href) && !"/company/toplist".equals(href)) {
						System.out.println(href);
						links.add(href);
					}
				}
			}
		}
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("generationmsx-companies.txt", false)));
		for (String link : links) {
			out.println(link);
		}
		out.close();

	}

	private GenerationMSXCompany parseCompanyInformation(String url) throws Exception {
		XPathParser xp = new XPathParser(url);
		String name = url.substring(0, url.length() - 1);
		name = name.substring(name.lastIndexOf("/") + 1);

		GenerationMSXCompany company = new GenerationMSXCompany();
		company.setUrl(url);

		String[] indexes = { "Name", "Fullname", "Company type", "Country", "Founded in", "Sub Companies", "Software Notes", "Hardware Notes", "Address", "Website", "Labels", "Main Company", "Sub Companies" };
		String[] attrs = { "name", "fullName", "type", "country", "foundedIn", "Subcompanies", "softwareNotes", "hardwareNotes", "address", "website", "labels", "mainCompany", "subCompanies" };
		for (int i = 0; i < indexes.length; i++) {
			String info = null;
			if (indexes[i].equals("Labels")) {
				NodeList labels = xp.parseList("//dt[.='" + indexes[i] + "']/following::dd[1]//ul/li");
				String lbl = "";
				for (int j = 1; j <= labels.getLength(); j++) {
					String label = xp.parse("//dt[.='" + indexes[i] + "']/following::dd[1]//ul/li[" + j + "]");
					lbl += label + ",";
				}
				info = lbl;
			} else if (indexes[i].equals("Sub Companies")) {
				NodeList subCompanies = xp.parseList("//dt[.='" + indexes[i] + "']/following::dd[1]//ul/li");
				String sub = "";
				for (int j = 1; j <= subCompanies.getLength(); j++) {
					String subComp = xp.parse("//dt[.='" + indexes[i] + "']/following::dd[1]//ul/li[" + j + "]/a/text()");
					sub += subComp + ",";
				}
				info = sub;
			} else if (indexes[i].equals("Language")) {
				String langs = "";
				for (int l = 1; l <= xp.parseList("//dt[.='" + indexes[i] + "']/following::dd[1]/node()").getLength(); l++) {
					if (!"".equals(xp.parse("//dt[.='" + indexes[i] + "']/following::dd[1]/node()[" + l + "]"))) {
						langs += xp.parse("//dt[.='" + indexes[i] + "']/following::dd[1]/node()[" + l + "]") + ",";
					}
				}
				info = langs;

			} else {
				info = xp.parse("//dt[.='" + indexes[i] + "']/following::dd[1]").trim();
			}
			BeanUtils.setProperty(company, attrs[i], info.trim().replaceAll("\\s+", " "));
		}

		NodeList nl = xp.parseList("//img[contains(@src,'/company')]/parent::a");
		for (int i = 1; i <= nl.getLength(); i++) {
			GenerationMSXGameMedia media = new GenerationMSXGameMedia();
			media.setUrl(xp.parse("(//img[contains(@src,'/company')]/parent::a)[" + i + "]/@href"));
			media.setType("image");
			media.setTitle(xp.parse("(//img[contains(@src,'/company')]/parent::a)[" + i + "]/@title"));
			company.addMedia(media);
		}

		return company;

	}

	private void downloadPubImages() throws Exception {
		String script = "";
		for (String file : new File("data/generationmsx/publications").list()) {
			if ("images".equals(file)) {
				continue;
			}
			String json = FileUtils.readFileToString(new File("data/generationmsx/publications/" + file));
			Gson gson = new Gson();
			GenerationMSXPublication pub = gson.fromJson(json, GenerationMSXPublication.class);
			for (GenerationMSXGameMedia media : pub.getMedias()) {
				script += "C:/Users/luisoft/apps/curl-7.61.1-win64-mingw/bin/curl -k \"" + "https:" + media.getUrl() + "\" > \"" + "data/generationmsx/publications/images/" + media.getUrl().substring(media.getUrl().lastIndexOf("/") + 1)
						+ "\"\ntimeout 5\n";
			}
		}
		System.out.println(script);
	}

	public static void main(String[] args) throws Exception {
		// System.setProperty("http.proxyHost", "wg-vip.trt4.gov.br");
		// System.setProperty("http.proxyPort", "3128");
		// System.setProperty("https.proxyHost", "wg-vip.trt4.gov.br");
		// System.setProperty("https.proxyPort", "3129");
		// System.setProperty("http.proxyUser", "lestivalet");
		// System.setProperty("http.proxyPassword", "LFmcc%232209");
		// System.setProperty("https.proxyUser", "lestivalet");
		// System.setProperty("https.proxyPassword", "LFmcc%232209");

		GenerationMSXScript g = new GenerationMSXScript();
		g.downloadPubImages();

		// GenerationMSXScript g = new GenerationMSXScript();
		// g.grabCompaniesLinks();
		// g.grabPublicationsLinksHttps();
		// System.exit(0);

		// List<String> lines = FileUtils.readLines(new File("data/generationmsx-companies.txt"));
		// for (String line : lines) {
		// GenerationMSXScript s = new GenerationMSXScript();
		// String url = "https://www.generation-msx.nl" + line;
		// System.out.println(url);
		// GenerationMSXCompany company = s.parseCompanyInformation(url);
		// Gson gson = new GsonBuilder().setPrettyPrinting().create();
		// String json = gson.toJson(company);
		// System.out.println(json);
		// String name = url.substring(0, url.length() - 1);
		// name = name.substring(name.lastIndexOf("/") + 1);
		// FileUtils.write(new File("data/generationmsx/companies/" + name + ".json"), json, "UTF-8");
		// }
		// System.exit(0);

		// GenerationMSXScript g = new GenerationMSXScript();
		// g.grabLinksHttps();
		// System.exit(0);

		// GenerationMSXScript s = new GenerationMSXScript();
		// GenerationMSXGame game = s.getGameInfo("https://www.generation-msx.nl/software/topia/future-boy-conan/release/3566");
		// Gson gson = new GsonBuilder().setPrettyPrinting().create();
		// String json = gson.toJson(game);
		// System.out.println(json);
		// System.exit(0);

		// List<String> lines = FileUtils.readLines(new File("data/generationmsx-publications.txt"));
		// for (String line : lines) {
		// System.out.println(line);
		// GenerationMSXScript g = new GenerationMSXScript();
		// String url = "https://www.generation-msx.nl" + line;
		// GenerationMSXPublication pub = g.parsePubInformation(url);
		// Gson gson = new GsonBuilder().setPrettyPrinting().create();
		// String json = gson.toJson(pub);
		// System.out.println(json);
		//
		// String name = url.substring(0, url.length() - 1);
		// name = name.substring(name.lastIndexOf("/") + 1);
		//
		// FileUtils.write(new File("data/generationmsx/publications/" + name + ".json"), json, "UTF-8");
		// }

		// List<String> lines = FileUtils.readLines(new File("data/generationmsx.txt"));
		// for (String line : lines) {
		// System.out.println(line);
		// GenerationMSXScript g = new GenerationMSXScript();
		// String url = "https://www.generation-msx.nl" + line;
		// GenerationMSXGame game = g.getGameInfo(url);
		// Gson gson = new GsonBuilder().setPrettyPrinting().create();
		// String json = gson.toJson(game);
		// System.out.println(json);
		//
		// String name = url.substring(0, url.length() - 1);
		// name = name.substring(name.lastIndexOf("/") + 1);
		//
		// FileUtils.write(new File("data/generationmsx/json/" + name + ".json"), json, "UTF-8");
		// }

		// String script = "";
		// for (String file : new File("data/generationmsx/").list()) {
		// if (file.equals("images") || file.equals("processed")) {
		// continue;
		// }
		// String json = FileUtils.readFileToString(new File("data/generationmsx/" +
		// file));
		// Gson gson = new Gson();
		// GenerationMSXGame game = gson.fromJson(json, GenerationMSXGame.class);
		// String url = game.getUrl();
		// url = url.substring(0, url.length() - 1);
		// url = url.substring(0, url.lastIndexOf("/"));
		// url = url.substring(url.lastIndexOf("/") + 1);
		// char ch = url.substring(0, 1).charAt(0);
		// String folder = "";
		// if (ch >= 48 && ch <= 57) {
		// folder = "0-9";
		// } else {
		// folder = String.valueOf(ch).toLowerCase();
		// }
		// new File("data/generationmsx/images/" + folder + "/" + url).mkdirs();
		// for (GenerationMSXGameMedia media : game.getMedias()) {
		// if (!"".equals(media.getUrl())) {
		// script += "C:/Users/luisoft/apps/curl-7.61.1-win64-mingw/bin/curl -k \"" +
		// "https:" + media.getUrl()
		// + "\" > \"" + "data/generationmsx/images/" + folder + "/" + url + "/"
		// + media.getUrl().substring(media.getUrl().lastIndexOf("/") + 1) +
		// "\"\ntimeout 5\n";
		// }
		// }
		// }
		// IOUtil.write("c:/temp/download.bat", script);
	}
}
