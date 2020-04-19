package glog.scrapper.generationmsx;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import glog.util.IOUtil;

public class ImportToSQLite {

	public static void main(String[] args) throws Exception {
		Map<String, String> genmsx = new HashMap<String, String>();
		Gson gson = new Gson();
		for (String file : new File("D:\\MSX\\generationmsx\\json").list()) {
			String json = IOUtil.readFully(new FileInputStream("D:\\MSX\\generationmsx\\json\\" + file));
			GenerationMSXGame game = gson.fromJson(json, GenerationMSXGame.class);
			String tmp = game.getTranslatedTitle();
			if (tmp.indexOf("(") > 0) {
				tmp = tmp.substring(0, tmp.indexOf("(")).trim();
			}
			genmsx.put(tmp, file);
		}

		String url = "jdbc:sqlite:C:\\luisoft\\develop\\octupusgdb-web\\octupusgdb.db";
		Connection c = DriverManager.getConnection(url);
		String sql = "SELECT name FROM game WHERE system_id = 920";
		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			String name = rs.getString("name");

//			genmsx.forEach((k, v) -> {
//				if (k.contains(name)) {
//					System.out.println("Found genmsx entry for " + name + " (" + k + ")");
//				}
//			});

			if (genmsx.get(name) != null) {
				System.out.format("Found for %s (%s)%n", name, genmsx.get(name));
			}
		}
		rs.close();
		ps.close();
		c.close();
	}

}
