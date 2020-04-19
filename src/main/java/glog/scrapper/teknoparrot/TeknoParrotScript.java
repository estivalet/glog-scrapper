package glog.scrapper.teknoparrot;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.NodeList;

import glog.util.XPathParser;

public class TeknoParrotScript {

	private static String sanitize(String name) {
		return name.trim().replace(":", " -").replace("&eacute;", "é").replace("&amp;", "and");
	}

	public static void main(String[] args) throws Exception {
		Map<String, String> games = new HashMap<String, String>();

		XPathParser xp = new XPathParser("https://teknogods.github.io/compatibility.html");
		NodeList nl = xp.parseList("//a[@class='compat-item']/@href");
		for (int i = 0; i < nl.getLength(); i++) {
			xp = new XPathParser("https://teknogods.github.io/" + nl.item(i).getNodeValue());
			String name = sanitize(xp.parse("//div[@class='tekno-pic-box pic-teknoparrot']/p"));
			String hardware = xp.parse("//span[contains(text(),'Hardware:')]/parent::p/a/text()").trim();
			if ("".equals(hardware.trim())) {
				hardware = xp.parse("//span[contains(text(),'Hardware:')]/parent::p/text()[1]").trim();
			}
			if ("".equals(hardware)) {
				System.out.println("NOT FOUND for " + name);
				System.exit(0);
			}
			System.out.println(name + " = " + hardware);

			String hm = games.get(hardware);
			if (hm == null) {
				games.put(hardware, name);
			} else {
				String gm = games.get(hardware);
				gm += ";" + name;
				games.put(hardware, gm);
			}

		}
		for (Map.Entry<String, String> entry : games.entrySet()) {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("c:/temp/" + entry.getKey() + ".txt", true)));
			String[] values = entry.getValue().split(";");
			for (String v : values) {
				out.println(v + ";" + v + ";" + entry.getKey() + ";;;;;;;;;0;;;;;");
			}
			out.close();

			System.out.println(entry.getKey() + " = " + entry.getValue());
		}

	}

}
