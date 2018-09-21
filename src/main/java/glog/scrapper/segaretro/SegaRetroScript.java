package glog.scrapper.segaretro;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.NodeList;

import glog.util.XMLUtil;
import glog.util.XMLWrapper;
import glog.util.XPathParser;

public class SegaRetroScript {

	public SegaRetroScript() {
	}

	public SegaRetroGame processLink(String link) throws Exception {
		SegaRetroGame srg = new SegaRetroGame();

		XPathParser xp = new XPathParser(link);

		srg.setName(xp.parse("//span[@itemprop='name']/text()"));

		String[] keys = { "Publisher", "Developer", "System", "ROM", "Genre", "Number" };
		String[] attrs = { "publisher", "developer", "systems", "romSize", "genre", "players" };
		for (int i = 0; i < keys.length; i++) {
			String value = xp.parse("//b[contains(text(),'" + keys[i] + "')]/following::span[1]");
			BeanUtils.setProperty(srg, attrs[i], value);
		}

		NodeList nl = xp.parseList("//small[.='Release']/ancestor::table[1]/tbody/tr");
		for (int i = 1; i <= nl.getLength() - 2; i++) {
			SegaRetroGameRelease srgr = new SegaRetroGameRelease();
			srgr.setCountry(xp.parse("//small[.='Release']/ancestor::table[1]/tbody/tr[" + i + "]/td[1]").trim());
			srgr.setDate(xp.parse("//small[.='Release']/ancestor::table[1]/tbody/tr[" + i + "]/td[2]/span"));
			srgr.setPrice(xp.parse("//small[.='Release']/ancestor::table[1]/tbody/tr[" + i + "]/td[3]/span"));
			srgr.setCode(xp.parse("//small[.='Release']/ancestor::table[1]/tbody/tr[" + i + "]/td[4]/span"));
			srg.addRelease(srgr);
		}

		srg.addShot("http://segaretro.org" + xp.parseNode("//span[@itemprop='name']/preceding::img[1]/@src").getNodeValue());

		nl = xp.parseList("//img[contains(@src,'thumb') and @width>14 and (not(contains(@src,'retrocdn')))]");
		for (int i = 0; i < nl.getLength(); i++) {
			String tmp = "http://segaretro.org" + nl.item(i).getParentNode().getAttributes().getNamedItem("href").getNodeValue();
			xp = new XPathParser(tmp);
			if (xp.parseNode("//a[.='Original file']") != null) {
				srg.addShot("http://segaretro.org" + xp.parseNode("//a[.='Original file']/@href").getNodeValue().toString());
			} else {
				srg.addShot("http://segaretro.org" + xp.parseNode("//div[@class='fullImageLink']/a/img/@src").getNodeValue().toString());
			}
		}

		return srg;

	}

	public static void main(String[] args) throws Exception {
		// XPathParser xp = new
		// XPathParser("http://segaretro.org/Category:SG-1000_games");
		// NodeList nl = xp.parseList("//h2[contains(.,'Pages in
		// category')]/following::a");
		// for (int i = 0; i < nl.getLength(); i++) {
		// System.out.println(nl.item(i).getAttributes().getNamedItem("href").getNodeValue());
		// }
		// System.exit(0);

		SegaRetroScript srs = new SegaRetroScript();
		List<String> lines = FileUtils.readLines(new File("/luisoft/development/apps/glog-service/segaretro_sg1000.txt"));
		for (String line : lines) {
			List<SegaRetroGame> games = new ArrayList<SegaRetroGame>();
			SegaRetroGame srg = srs.processLink("http://segaretro.org" + line);
			games.add(srg);

			JAXBContext jc = JAXBContext.newInstance(XMLWrapper.class, SegaRetroGame.class);
			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			FileUtils.write(new File("segaretro/sg1000/" + line.substring(1).replaceAll("[^\\w.-]", "_") + ".xml"), XMLUtil.marshal(marshaller, games, "games"));

		}

		// games.add(srs.processLink("http://segaretro.org/A_Year_at_Pooh_Corner"));

		// JAXBContext jc = JAXBContext.newInstance(XMLWrapper.class,
		// SegaRetroGame.class);
		// Marshaller marshaller = jc.createMarshaller();
		// marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		//
		// System.out.println(XMLUtil.marshal(marshaller, games, "games"));

	}
}
