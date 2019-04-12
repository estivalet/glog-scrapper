package glog.scrapper.oldcomputer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import glog.util.XMLUtil;
import glog.util.XMLWrapper;
import glog.util.XPathParser;

public class OldComputerScript {
	private static final String OLD_COMPUTER_URL = "http://www.old-computers.com/museum/";
	private List<OldComputer> computers;

	public OldComputerScript() {
		this.computers = new ArrayList<OldComputer>();
	}

	private OldComputer summaryPage(int id) throws Exception {
		OldComputer oc = new OldComputer();

		XPathParser xp = new XPathParser("http://www.old-computers.com/museum/computer.asp?c=" + id + "&st=1");
		String description = xp.parse("//p[@class='petitnoir']");
		oc.setDescription(description);
		oc.setImage(OLD_COMPUTER_URL + xp.parse("//img[contains(@src,'photos/')]/@src"));

		String[] keys = { "NAME", "MANUFACTURER", "TYPE", "ORIGIN", "YEAR", "PRODUCTION", "LANGUAGE", "KEYBOARD", "CPU", "SPEED", "CO-PROCESSOR", "RAM", "VRAM", "ROM", "TEXT", "GRAPHIC", "COLORS", "SOUND", "SIZE", "PORTS", "POWER", "PRICE",
				"BUILT", "CONTROLLERS", "MEDIA", "NUMBER", "PERIPHERALS", "SWITCHES", "BATTERIES", "BUTTONS", "GUN" };
		String[] attrs = { "name", "manufacturer", "type", "origin", "year", "production", "language", "keyboard", "cpu", "speed", "coprocessor", "ram", "vram", "rom", "text", "graphics", "colors", "sound", "size", "ports", "power",
				"price", "builtInGames", "controllers", "media", "numGames", "peripherals", "switches", "batteries", "buttons", "gun" };
		for (int i = 0; i < keys.length; i++) {
			String value = xp.parse("//b[contains(text(),'" + keys[i] + "')]/following::td[1]");
			BeanUtils.setProperty(oc, attrs[i], value);
		}

		return oc;

	}

	private OldComputer photosPage(OldComputer oc, int id) throws Exception {
		XPathParser xp = new XPathParser("http://www.old-computers.com/museum/photos.asp?t=1&c=" + id + "&st=1");
		Node test = xp.parseNode("//font[contains(text(),'992 computers')]");
		if (test != null) {
			return oc;
		}
		NodeList images = xp.parseList("//img[contains(@src,'icones_photos')]");
		for (int i = 0; i < images.getLength(); i++) {
			// String src = OLD_COMPUTER_URL +
			// images.item(i).getAttributes().getNamedItem("src").getNodeValue().toString().replace("icones_",
			// "");
			String src = OLD_COMPUTER_URL + images.item(i).getParentNode().getAttributes().getNamedItem("href").getNodeValue().toString().replace("icones_", "");
			String description = images.item(i).getParentNode().getAttributes().getNamedItem("title").getTextContent();
			if (description.indexOf("/title") > 0) {
				description = description.substring(0, description.indexOf("/title"));
			}
			OldComputerShot oca = new OldComputerShot();
			oca.setLink(src);
			oca.setDescription(description);
			oc.addScreenshot(oca);
		}
		return oc;
	}

	private OldComputer advertPage(OldComputer oc, int id) throws Exception {
		XPathParser xp = new XPathParser("http://www.old-computers.com/museum/photos.asp?t=2&c=" + id + "&st=1");
		Node test = xp.parseNode("//font[contains(text(),'992 computers')]");
		if (test != null) {
			return oc;
		}
		NodeList images = xp.parseList("//img[contains(@src,'icones_advert')]");
		for (int i = 0; i < images.getLength(); i++) {
			String src = OLD_COMPUTER_URL + images.item(i).getAttributes().getNamedItem("src").getNodeValue().toString().replace("icones_", "");
			String description = images.item(i).getParentNode().getAttributes().getNamedItem("title").getNodeValue();
			if (description.indexOf("/title") > 0) {
				description = description.substring(0, description.indexOf("/title"));
			}
			OldComputerAdvert oca = new OldComputerAdvert();
			oca.setLink(src);
			oca.setDescription(description);
			oc.addAdvert(oca);
		}
		return oc;
	}

	private int countTables(NodeList nl) {
		int count = 0;
		boolean foundTable = false;
		for (int j = 0; j < nl.getLength(); j++) {
			if ("table".equals(nl.item(j).getNodeName())) {
				foundTable = true;
				count++;
			}

			if (foundTable) {
				if ("p".equals(nl.item(j).getNodeName())) {
					if (nl.item(j).getChildNodes().item(0).getNodeName().equals("img")) {
						return count;
					}
				}
				if ("br".equals(nl.item(j).getNodeName())) {
					if (nl.item(j + 1).getNodeName().equals("br")) {
						return count;
					}
				}
			}
		}
		return count;
	}

	private OldComputer hardwareInfo(OldComputer oc, int id) throws Exception {
		XPathParser xp = new XPathParser("http://www.old-computers.com/museum/hardware.asp?t=1&c=" + id + "&st=1");
		NodeList tmp = xp.parseList("html/body/table[2]/tbody/tr[1]/td[3]/p");
		for (int i = 1; i < tmp.getLength() + 1; i++) {
			Node n = xp.parseNode("html/body/table[2]/tbody/tr[1]/td[3]/p[" + i + "]");
			Node img = xp.parseNode("html/body/table[2]/tbody/tr[1]/td[3]/p[" + i + "]/img");
			// Has hardware category
			if (img != null) {
				OldComputerHardware och = new OldComputerHardware();
				och.setType(xp.parse("html/body/table[2]/tbody/tr[1]/td[3]/p[" + i + "]/b"));
				NodeList nodes = xp.parseList("html/body/table[2]/tbody/tr[1]/td[3]/p[" + i + "]/following::node()");
				int total = countTables(nodes);

				for (int j = 1; j <= total; j++) {
					OldComputerHardwareItem ochi = new OldComputerHardwareItem();
					String desc = xp.parse("html/body/table[2]/tbody/tr[1]/td[3]/p[" + i + "]/following::table[" + j + "]/descendant::td[3]/p/b");
					desc += "|||" + xp.parse("html/body/table[2]/tbody/tr[1]/td[3]/p[" + i + "]/following::table[" + j + "]/descendant::td[3]/p/text()");
					desc += "|||" + xp.parse("html/body/table[2]/tbody/tr[1]/td[3]/p[" + i + "]/following::table[" + j + "]/descendant::td[1]/a/@title");
					desc = desc.substring(0, desc.indexOf("/title"));
					ochi.setDescription(desc);
					ochi.setPhoto(OLD_COMPUTER_URL + xp.parse("html/body/table[2]/tbody/tr[1]/td[3]/p[" + i + "]/following::table[" + j + "]/descendant::td[1]/a/img/@src").replace("icones_", ""));
					och.addItem(ochi);
				}
				oc.addHardware(och);

			} else {
				oc.setHardwareDescription(n.getTextContent());
			}
		}
		return oc;
	}

	private OldComputer emulatorInfo(OldComputer oc, int id) throws Exception {
		XPathParser xp = new XPathParser("http://www.old-computers.com/museum/emulator.asp?t=1&c=" + id + "&st=1");
		NodeList tmp = xp.parseList("html/body/table[2]/tbody/tr[1]/td[3]/table");
		for (int i = 3; i < tmp.getLength() + 1; i++) {
			OldComputerEmulator oce = new OldComputerEmulator();
			Node n = xp.parseNode("html/body/table[2]/tbody/tr[1]/td[3]/table[" + i + "]/tbody/tr/td[2]/a");
			oce.setName(n.getTextContent());
			oce.setLink(n.getAttributes().getNamedItem("href").getNodeValue());
			Node n2 = xp.parseNode("html/body/table[2]/tbody/tr[1]/td[3]/table[" + i + "]/tbody/tr/td[1]/img/@alt");
			oce.setPlatform(n2.getTextContent());
			Node n3 = xp.parseNode("html/body/table[2]/tbody/tr[1]/td[3]/table[" + i + "]/tbody/tr/td[4]/p");
			if (n3 != null) {
				oce.setDescription(n3.getTextContent());
			}
			oc.addEmulator(oce);
		}
		return oc;
	}

	private OldComputer linksPage(OldComputer oc, int id) throws Exception {
		XPathParser xp = new XPathParser("http://www.old-computers.com/museum/links.asp?c=" + id + "&st=1");
		NodeList links = xp.parseList("//a[@class='petitnoir3']/@href");
		for (int i = 0; i < links.getLength(); i++) {
			oc.addLink(links.item(i).getTextContent());
		}
		return oc;
	}

	private String getComputerInfo(int id) throws Exception {
		OldComputer oc = this.summaryPage(id);
		oc = this.photosPage(oc, id);
		oc = this.advertPage(oc, id);
		oc = this.hardwareInfo(oc, id);
		oc = this.emulatorInfo(oc, id);
		oc = this.linksPage(oc, id);

		this.computers.add(oc);

		JAXBContext jc = JAXBContext.newInstance(XMLWrapper.class, OldComputer.class);
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		return XMLUtil.marshal(marshaller, computers, "computers");
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

		OldComputerScript ocs = new OldComputerScript();
		String data = ocs.getComputerInfo(1181);
		System.out.println(data);
		FileUtils.write(new File("test.xml"), data);
		System.exit(0);
		//
		// if (1 == 1)
		// return;

		// List<String> lines = FileUtils.readLines(new File("/luisoft/development/apps/glog-service/oldcomputer_pong.txt"));
		// for (String line : lines) {
		// OldComputerScript ocs = new OldComputerScript();
		// line = line.substring(line.indexOf("?c=") + 3, line.indexOf("&amp"));
		// System.out.println("Processing id " + line);
		// FileUtils.write(new File(line + ".xml"), ocs.getComputerInfo(Integer.parseInt(line)));
		// }

		// File file = new File("/home/luisoft/Desktop/pongs.html");
		// String string = FileUtils.readFileToString(file);
		// TagNode tagNode = new HtmlCleaner().clean(string);
		// Document doc = new DomSerializer(new
		// CleanerProperties()).createDOM(tagNode);
		// XPath xp = XPathFactory.newInstance().newXPath();
		// NodeList nl = (NodeList)
		// xp.evaluate("//a[contains(@href,'computer.asp?c=')]/@href", doc,
		// XPathConstants.NODESET);
		// for (int i = 0; i < nl.getLength(); i++) {
		// System.out.println(nl.item(i).getNodeValue());
		// }

	}

}