package glog.scrapper.mobygames;

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
		Map<String, String> moby = new HashMap<String, String>();
		Gson gson = new Gson();
		for (String file : new File("D:\\MSX\\mobygames\\MSX - MSX 1\\json").list()) {
			String json = IOUtil.readFully(new FileInputStream("D:\\MSX\\mobygames\\MSX - MSX 1\\json\\" + file));
			MobyGame game = gson.fromJson(json, MobyGame.class);
			String tmp = game.getName();
			if (tmp.indexOf("(") > 0) {
				tmp = tmp.substring(0, tmp.indexOf("(")).trim();
			}
			moby.put(tmp, file);
		}
		String url = "jdbc:sqlite:C:\\luisoft\\develop\\octupusgdb-web\\octupusgdb.db";
		Connection c = DriverManager.getConnection(url);
		String sql = "SELECT name FROM game WHERE system_id = 920";
		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			String name = rs.getString("name");
			if (moby.get(name) != null) {
				System.out.println("Found mobygames entry for " + name);
			}

		}
		rs.close();
		ps.close();
		c.close();
	}

}
