package glog.scrapper.mobygames;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import glog.util.IOUtil;

public class ImportToSQLite {

	private static String cleanTextContent(String text) {
		// strips off all non-ASCII characters
		text = text.replaceAll("[^\\x00-\\x7F]", " ");

		// erases all the ASCII control characters
		text = text.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", " ");

		// removes non-printable characters from Unicode
		text = text.replaceAll("\\p{C}", " ");

		return text.trim();
	}

	private static void updateWithMobyGameData(String system, String systemId) throws Exception {
		Map<String, MobyGame> moby = new HashMap<String, MobyGame>();

		// Change 2 lines below for each system to import
		String sql = "SELECT id, name FROM game WHERE system_id = " + systemId;

		Gson gson = new Gson();
		for (String file : new File("C:\\luisoft\\develop\\glog-scrapper\\mobygames\\" + system + "\\json").list()) {
			String json = IOUtil.readFully(new FileInputStream(
					"C:\\luisoft\\develop\\glog-scrapper\\mobygames\\" + system + "\\json\\" + file));
			MobyGame game = gson.fromJson(json, MobyGame.class);
			String tmp = game.getName();
			if (tmp.indexOf("(") > 0) {
				tmp = tmp.substring(0, tmp.indexOf("(")).trim();
			}
			moby.put(tmp, game);
		}
		String url = "jdbc:sqlite:C:\\temp\\octupus.db";
		Connection c = DriverManager.getConnection(url);

		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			String name = rs.getString("name");
			if (moby.get(name) != null) {
				System.out.println("Found mobygames entry for " + name);
				Long id = rs.getLong("id");
				if (moby.get(name).getDescription() != null) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					String ts = sdf.format(timestamp);
					String update = "UPDATE game SET description = ?, modified_at = ? WHERE id = ?";
					PreparedStatement psupd = c.prepareStatement(update);
					psupd.setString(1, cleanTextContent(moby.get(name).getDescription()));
					psupd.setString(2, ts);
					psupd.setLong(3, id);
					psupd.executeUpdate();
					psupd.close();
				}
				if (moby.get(name).getReleased() != null) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					String ts = sdf.format(timestamp);
					String update = "UPDATE game SET year = ?, modified_at = ? WHERE id = ?";
					PreparedStatement psupd = c.prepareStatement(update);
					psupd.setString(1, cleanTextContent(moby.get(name).getReleased()));
					psupd.setString(2, ts);
					psupd.setLong(3, id);
					psupd.executeUpdate();
					psupd.close();
				}
				if (moby.get(name).getPublishedBy() != null) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					String ts = sdf.format(timestamp);
					String update = "UPDATE game SET published_by = ?, modified_at = ? WHERE id = ?";
					PreparedStatement psupd = c.prepareStatement(update);
					psupd.setString(1, cleanTextContent(moby.get(name).getPublishedBy()));
					psupd.setString(2, ts);
					psupd.setLong(3, id);
					psupd.executeUpdate();
					psupd.close();
				}
				if (moby.get(name).getDevelopedBy() != null) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					String ts = sdf.format(timestamp);
					String update = "UPDATE game SET developed_by = ?, modified_at = ? WHERE id = ?";
					PreparedStatement psupd = c.prepareStatement(update);
					psupd.setString(1, cleanTextContent(moby.get(name).getDevelopedBy()));
					psupd.setString(2, ts);
					psupd.setLong(3, id);
					psupd.executeUpdate();
					psupd.close();
				}
				if (moby.get(name).getShot().size() > 0) {
					for (MobyGameScreenshot shot : moby.get(name).getShot()) {
						String destFileName = shot.getLink().substring(shot.getLink().lastIndexOf("/") + 1);
						destFileName = destFileName.substring(destFileName.indexOf("screenshot-") + 11);
						String release = "INSERT INTO game_screenshot(game_id, filename, caption) VALUES(?, ?, ?)";
						PreparedStatement psupd = c.prepareStatement(release);
						psupd.setLong(1, id);
						psupd.setString(2, cleanTextContent(destFileName));
						psupd.setString(3, cleanTextContent(shot.getCaption()));
						psupd.executeUpdate();
						psupd.close();
					}
				}
				if (moby.get(name).getCover().size() > 0) {
					for (MobyGameCoverArt cover : moby.get(name).getCover()) {
						String destFileName = cover.getLink().substring(cover.getLink().lastIndexOf("/") + 1);
						destFileName = destFileName
								.substring(destFileName.indexOf("-" + system + "-") + system.length() + 2);
						String release = "INSERT INTO game_cover(game_id, filename, scan_of, packaging, country) VALUES(?,?, ?, ?, ?)";
						PreparedStatement psupd = c.prepareStatement(release);
						psupd.setLong(1, id);
						psupd.setString(2, cleanTextContent(destFileName));
						psupd.setString(3, cleanTextContent(cover.getScanOf()));
						psupd.setString(4, cleanTextContent(cover.getPackaging()));
						psupd.setString(5, cleanTextContent(cover.getCountry()));
						psupd.executeUpdate();
						psupd.close();
					}
				}
				if (moby.get(name).getRelease().size() > 0) {
					for (int i = 0; i < moby.get(name).getRelease().size(); i++) {
						MobyGameRelease mgr = moby.get(name).getRelease().get(i);
						for (int j = 0; j < mgr.getReleases().size(); j++) {
							MobyGameReleaseInfo mgri = mgr.getReleases().get(j);
							String release = "INSERT INTO game_release(game_id,publisher, developer, ported, country, release_date, ean13, comments) VALUES(?,?, ?, ?, ?, ?, ?, ?)";
							PreparedStatement psupd = c.prepareStatement(release);
							psupd.setLong(1, id);
							psupd.setString(2, cleanTextContent(mgri.getPublisher()));
							psupd.setString(3, cleanTextContent(mgri.getDeveloper()));
							psupd.setString(4, cleanTextContent(mgri.getPorted()));
							psupd.setString(5, cleanTextContent(mgri.getCountry()));
							psupd.setString(6, cleanTextContent(mgri.getReleaseDate()));
							psupd.setString(7, cleanTextContent(mgri.getEan13()));
							psupd.setString(8, cleanTextContent(mgri.getComments()));
							psupd.execute();
							psupd.close();

						}
					}
				}
			}

		}
		rs.close();
		ps.close();
		c.close();

	}

	public static void main(String[] args) throws Exception {
//		ImportToSQLite.updateWithMobyGameData("atari-2600", "127");
//		ImportToSQLite.updateWithMobyGameData("atari-7800", "131");
//		ImportToSQLite.updateWithMobyGameData("msx", "920");
//		ImportToSQLite.updateWithMobyGameData("sega-master-system", "121");
//		ImportToSQLite.updateWithMobyGameData("genesis", "120");
//		ImportToSQLite.updateWithMobyGameData("nes", "126");
//		ImportToSQLite.updateWithMobyGameData("snes", "45");
//		ImportToSQLite.updateWithMobyGameData("n64", "54");
//		ImportToSQLite.updateWithMobyGameData("gameboy", "1078");
//		ImportToSQLite.updateWithMobyGameData("gameboy-color", "1079");
//		ImportToSQLite.updateWithMobyGameData("gameboy-advance", "1080");
//		ImportToSQLite.updateWithMobyGameData("casio-pv-1000", "124");
//		ImportToSQLite.updateWithMobyGameData("bally-astrocade", "87");
//		ImportToSQLite.updateWithMobyGameData("channel-f", "130");
//		ImportToSQLite.updateWithMobyGameData("colecovision", "100");
//		ImportToSQLite.updateWithMobyGameData("intellivision", "37");
//		ImportToSQLite.updateWithMobyGameData("spectravideo", "396");
//		ImportToSQLite.updateWithMobyGameData("colecoadam", "1084");
//		ImportToSQLite.updateWithMobyGameData("exelvision", "657");
//		ImportToSQLite.updateWithMobyGameData("neo-geo-pocket", "69");
//		ImportToSQLite.updateWithMobyGameData("neo-geo-pocket-color", "70");
//		ImportToSQLite.updateWithMobyGameData("wonderswan", "26");
//		ImportToSQLite.updateWithMobyGameData("wonderswan-color", "1085");
//		ImportToSQLite.updateWithMobyGameData("atari-5200", "132");
//		ImportToSQLite.updateWithMobyGameData("jaguar", "43");
//		ImportToSQLite.updateWithMobyGameData("sega-32x", "1086");
//		ImportToSQLite.updateWithMobyGameData("sg-1000", "67");
//		ImportToSQLite.updateWithMobyGameData("arcadia-2001", "119");
//		ImportToSQLite.updateWithMobyGameData("amiga-cd32", "68");
//		ImportToSQLite.updateWithMobyGameData("game-gear", "12");
//		ImportToSQLite.updateWithMobyGameData("zx-spectrum", "392");
//		ImportToSQLite.updateWithMobyGameData("c64", "983");
//		ImportToSQLite.updateWithMobyGameData("cpc", "873");
//		ImportToSQLite.updateWithMobyGameData("amiga", "771");
//		ImportToSQLite.updateWithMobyGameData("bbc-micro_", "456");
//		ImportToSQLite.updateWithMobyGameData("electron", "265");
//		ImportToSQLite.updateWithMobyGameData("dreamcast", "58");
//		ImportToSQLite.updateWithMobyGameData("playstation", "46");
//		ImportToSQLite.updateWithMobyGameData("nintendo-ds", "1087");
//		ImportToSQLite.updateWithMobyGameData("gamecube", "1088");
//		ImportToSQLite.updateWithMobyGameData("atari-8-bit", "598");
//		ImportToSQLite.updateWithMobyGameData("3do", "23");
//		ImportToSQLite.updateWithMobyGameData("atari-st", "369");

	}

}
