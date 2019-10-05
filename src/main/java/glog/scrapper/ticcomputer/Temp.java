package glog.scrapper.ticcomputer;

import java.io.File;
import java.net.URLDecoder;

import org.w3c.dom.NodeList;

import glog.util.URLGrabber;
import glog.util.XPathParser;

public class Temp {
	public static void main(String[] args) throws Exception {
//		Map<String, String> nds = new HashMap<String, String>();
//		try (BufferedReader br = new BufferedReader(new FileReader(new File("c:/temp/nds.txt")))) {
//			for (String line; (line = br.readLine()) != null;) {
//				line = line.replace(".nds.7z", "");
//				nds.put(line, line);
//			}
//		}
//		
//		String[] have = new File("E:\\Nintendo NDS").list();
//		for(String g:have) {
//			String key = g.replace(".zip", "").replace(".7z", "").replace(".rar", "");
//			if(nds.get(key)) {
//				System.out.println("missing " + key);
//			}
//		}
//		
//		System.exit(0);

		XPathParser xp = new XPathParser("https://the-eye.eu/public/rom/Nintendo%20DS/");
		NodeList links = xp.parseList("//a[contains(@href,'.nds')]/@href");

		for (int i = 0; i < links.getLength(); i++) {
			String decoded = URLDecoder.decode(links.item(i).getNodeValue());
			System.out.println(decoded);
			if (!new File("e:/ndsnew/" + decoded).exists()) {
				URLGrabber ug = new URLGrabber("https://the-eye.eu/public/rom/Nintendo%20DS/" + links.item(i).getNodeValue(), "e:/ndsnew/" + decoded);
				ug.saveURLBinary();
			}
		}
	}
}
