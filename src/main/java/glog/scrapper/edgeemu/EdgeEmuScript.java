package glog.scrapper.edgeemu;

import org.w3c.dom.NodeList;

import glog.util.URLGrabber;
import glog.util.XPathParser;

public class EdgeEmuScript {
	public void processLink(String main, String link) throws Exception {
		XPathParser xp = new XPathParser(link);

		NodeList nl = xp.parseList("//a[contains(@href,'browse')]/@href");
		for (int i = 0; i <= nl.getLength() - 1; i++) {
			System.out.println(nl.item(i).getNodeValue());
			XPathParser xp2 = new XPathParser(main + "/" + nl.item(i).getNodeValue());
			NodeList nl2 = xp2.parseList("//table/tbody/tr/td/a/@href");
			for (int i2 = 0; i2 <= nl2.getLength() - 1; i2++) {
				System.out.println(main + "/" + nl2.item(i2).getNodeValue());
				XPathParser xp3 = new XPathParser(main + "/" + nl2.item(i2).getNodeValue());
				String title = xp3.parseNode("//img[contains(@src,'/Named_Titles')]/@src").getNodeValue();
				String snap = xp3.parseNode("//img[contains(@src,'/Named_Snaps')]/@src").getNodeValue();
				URLGrabber ug = new URLGrabber(main + title.replaceAll("&amp;", "&"), "c:/temp/title/" + title.substring(title.lastIndexOf("/") + 1).replaceAll("&amp;", "&"));
				ug.saveURLBinary();
				URLGrabber ug2 = new URLGrabber(main + snap.replaceAll("&amp;", "&"), "c:/temp/snap/" + snap.substring(snap.lastIndexOf("/") + 1).replaceAll("&amp;", "&"));
				ug2.saveURLBinary();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		EdgeEmuScript ees = new EdgeEmuScript();
		ees.processLink("http://edgeemu.net", "http://edgeemu.net/browse-gp32.htm");
	}
}
