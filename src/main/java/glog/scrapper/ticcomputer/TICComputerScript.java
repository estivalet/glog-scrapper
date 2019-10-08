package glog.scrapper.ticcomputer;

import org.w3c.dom.NodeList;

import glog.util.URLGrabber;
import glog.util.XPathParser;

public class TICComputerScript {

	public static void main(String[] args) throws Exception {
		TICComputerScript g = new TICComputerScript();
		XPathParser xp = new XPathParser("https://tic.computer/play?cat=0&sort=0");
		NodeList links = xp.parseList("//a[contains(@href,'cart')]/@href");
		for (int i = 0; i < links.getLength(); i++) {
			System.out.println(links.item(i).getNodeValue());
			xp = new XPathParser("https://tic.computer" + links.item(i).getNodeValue());
			String downloadLink = xp.parse("//a[contains(.,'download cartridge')]/@href");
			String name = xp.parse("//h1").replace("Games &gt; ", "").replaceAll("&quot;", "'").replace(":", " -");
			System.out.println(downloadLink);
			System.out.println("-->" + name);
			URLGrabber ug = new URLGrabber("https://tic.computer" + downloadLink, "c:/temp/" + name + ".tic");
			ug.saveURLBinary();
			// System.exit(0);
		}
	}
}
