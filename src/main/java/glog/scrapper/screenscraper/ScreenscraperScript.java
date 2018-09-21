package glog.scrapper.screenscraper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.NodeList;

import glog.util.URLGrabber;
import glog.util.XPathParser;

public class ScreenscraperScript {

	public static void main(String[] args) throws Exception {
		XPathParser xp0 = new XPathParser("http://www.screenscraper.fr/systemeinfos.php?plateforme=26&alpha=0&numpage=0");
		NodeList systems = xp0.parseList("//td[contains(@onclick,'systeme')]");
		List<String> sys = new ArrayList<String>();
		System.out.println("Total systems found:" + systems.getLength());
		for (int i = 0; i < systems.getLength(); i++) {
			String s = systems.item(i).getAttributes().getNamedItem("onclick").getTextContent();
			int a = s.indexOf("plateforme=");
			int b = s.indexOf("&amp;", a);
			sys.add(s.substring(a + 11, b));
		}
		// System.exit(0);

		for (String system : sys) {
			new File("c:/temp/" + system + "/").mkdirs();
			XPathParser xp = new XPathParser("http://www.screenscraper.fr/systemeinfos.php?plateforme=" + system + "&alpha=0&numpage=0");
			// System.out.println(xp.getPageContents());
			// NodeList shotLinks = xp.parseList("//font[.='Médias']/following::img[contains(@src,'plateformid')]");
			NodeList shotLinks = xp.parseList("//img[contains(@src,'image.php?plateformid=" + system + "')]");
			for (int i = 0; i < shotLinks.getLength(); i++) {
				String l = shotLinks.item(i).getAttributes().getNamedItem("src").getTextContent();
				if (l.contains("plateformid")) {
					String link = "http://www.screenscraper.fr/" + l.replaceAll("amp;", "");

					String[] data = link.split("&");
					int width = 0, height = 0;
					String name = "";
					for (int j = 0; j < data.length; j++) {
						System.out.println(data[j]);
						if (data[j].startsWith("maxwidth")) {
							String[] data2 = data[j].split("=");
							width = Integer.parseInt(data2[1]) * 3;
							System.out.println(width);
						}
						if (data[j].startsWith("maxheight")) {
							String[] data2 = data[j].split("=");
							height = Integer.parseInt(data2[1]) * 3;
							System.out.println(height);
						}
						if (data[j].startsWith("media")) {
							String[] data2 = data[j].split("=");
							name = data2[1];
						}
					}
					link += "&maxwidth=" + width + "&maxheight=" + height;
					System.out.println(link);
					URLGrabber ug = new URLGrabber(link, "c:/temp/" + system + "/" + name + ".png");
					ug.saveURLBinary();
					// System.exit(0);
				}
			}
		}

	}

}
