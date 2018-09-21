package glog.scrapper.lemon64;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.w3c.dom.NodeList;

import glog.util.URLGrabber;

public class Lemon64Script {

	protected org.w3c.dom.Document getDOM(String link) throws Exception {
		URLGrabber ug = new URLGrabber(link);
		String contents = ug.getContents();
		TagNode tagNode = new HtmlCleaner().clean(contents);

		return new DomSerializer(new CleanerProperties()).createDOM(tagNode);
	}

	private void parseGame(String link) throws Exception {
		org.w3c.dom.Document doc = getDOM(link);
		XPath xpath = XPathFactory.newInstance().newXPath();

		String name = (String) xpath.evaluate("//td[@class='normalheadblank']/div[1]/text()", doc, XPathConstants.STRING);
		System.out.println("NAME-->" + name);

		String year = (String) xpath.evaluate("//td[@class='normalheadblank']/div[2]", doc, XPathConstants.STRING);
		System.out.println(year);

		String[] credits = { "Coder:", "Musician:", "Genre:", "Players:", "Review:", "Advert:", "Music:", "Related", "Documentation:", "Review:", "Conversions:", "Links:" };
		for (int i = 0; i < credits.length; i++) {
			String credit = (String) xpath.evaluate("//b[contains(text(),'" + credits[i] + "')]/following::td[1]", doc, XPathConstants.STRING);
			System.out.println(credit.trim());
		}

		NodeList pics = (NodeList) xpath.evaluate("//img[@class='pic']/@src", doc, XPathConstants.NODESET);
		for (int i = 0; i < pics.getLength(); i++) {
			// saveImage(pics.item(i).getTextContent());
		}

	}

	public static void main(String[] args) throws Exception {
		Lemon64Script la = new Lemon64Script();
		// Connection conn = CreateHSQLDatabase.getConnection();
		// la.insertPlatform(conn, "Commodore 64", 1);
		// la.createPlatformFolders(GLOG_FOLDER + "C/Commodore - 64/");
		la.parseGame("http://www.lemon64.com/games/details.php?ID=2928");
	}

}
