package glog.scrapper.oldcomputer;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;

import glog.util.IOUtil;

public class ImportToSQLite {

	private List<OldComputer> computers = new ArrayList<OldComputer>();

	/**
	 * 
	 */
	public ImportToSQLite() {
		String[] files = new File("data/oldcomputer/consoles/json").list();
		for (String file : files) {
			File json = new File("data/oldcomputer/consoles/json/" + file);
			OldComputer oc = new Gson().fromJson(IOUtil.getContents(json), OldComputer.class);
			oc.setCategoryId(2);
			oc.setType("Console");
			oc.setUrl("http://www.old-computers.com/museum/computer.asp?c=" + file.replace(".json", ""));
			computers.add(oc);
		}
		files = new File("data/oldcomputer/computers/json").list();
		for (String file : files) {
			File json = new File("data/oldcomputer/computers/json/" + file);
			OldComputer oc = new Gson().fromJson(IOUtil.getContents(json), OldComputer.class);
			oc.setCategoryId(1);
			oc.setType("Computer");
			oc.setUrl("http://www.old-computers.com/museum/computer.asp?c=" + file.replace(".json", ""));
			computers.add(oc);
		}
		files = new File("data/oldcomputer/pongs/json").list();
		for (String file : files) {
			File json = new File("data/oldcomputer/pongs/json/" + file);
			OldComputer oc = new Gson().fromJson(IOUtil.getContents(json), OldComputer.class);
			oc.setCategoryId(4);
			oc.setType("Pong");
			oc.setUrl("http://www.old-computers.com/museum/computer.asp?c=" + file.replace(".json", ""));
			computers.add(oc);
		}
	}

	/**
	 * @return
	 */
	private Connection connect() {
		String url = "jdbc:sqlite:C:\\luisoft\\develop\\octupusgdb-web\\octupusgdb.db";
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}

	/**
	 * 
	 */
	private void insertManufacturer() {
		Set<String> manufacturers = new HashSet<String>();
		for (OldComputer oc : this.computers) {
			if (oc.getManufacturer() != null) {
				manufacturers.add(oc.getManufacturer().trim());
			}
		}

		for (String manufacturer : manufacturers) {
			String sql = "INSERT INTO manufacturer (name)" + "values (?)";

			try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, manufacturer);
				pstmt.executeUpdate();
				pstmt.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}

		}
	}

	public void insert(OldComputer oc) {
		Long systemId = 0L;
		Long manufacturerId = 0L;

		String sql = "SELECT id FROM manufacturer WHERE name = ?";
		try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, oc.getManufacturer());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				manufacturerId = rs.getLong("id");
			} else {
				System.out.println("***WARNING manufacturer not found for " + oc.getName() + " " + oc.getUrl());
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		sql = "INSERT INTO system (name, name_alt, manufacturer_id, category_id, type, country, year, description, price)"
				+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			String description = oc.getDescription();
			description = description.replaceAll("\n", "<br>");
			pstmt.setString(1, oc.getName());
			pstmt.setString(2, oc.getName());
			pstmt.setLong(3, manufacturerId);
			pstmt.setInt(4, oc.getCategoryId());
			pstmt.setString(5, oc.getType());
			pstmt.setString(6, oc.getOrigin());
			pstmt.setString(7, oc.getYear());
			pstmt.setString(8, description);
			pstmt.setString(9, oc.getPrice());
			pstmt.executeUpdate();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select last_insert_rowid()");
			rs.next();
			systemId = rs.getLong(1);
			rs.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		sql = "INSERT INTO technical_information\r\n"
				+ "(system_id, batteries, buttons, built_in_games, colors, controllers, coprocessor, cpu, graphics, gun, keyboard, language, media, num_games, peripherals, ports, power, ram, rom, size, sound, speed, switches, text, vram)\r\n"
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

		try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setLong(1, systemId);
			pstmt.setString(2, oc.getBatteries());
			pstmt.setString(3, oc.getButtons());
			pstmt.setString(4, oc.getBuiltInGames());
			pstmt.setString(5, oc.getColors());
			pstmt.setString(6, oc.getControllers());
			pstmt.setString(7, oc.getCoprocessor());
			pstmt.setString(8, oc.getCpu());
			pstmt.setString(9, oc.getGraphics());
			pstmt.setString(10, oc.getGun());
			pstmt.setString(11, oc.getKeyboard());
			pstmt.setString(12, oc.getLanguage());
			pstmt.setString(13, oc.getMedia());
			pstmt.setString(14, oc.getNumGames());
			pstmt.setString(15, oc.getPeripherals());
			pstmt.setString(16, oc.getPorts());
			pstmt.setString(17, oc.getPower());
			pstmt.setString(18, oc.getRom());
			pstmt.setString(19, oc.getRam());
			pstmt.setString(20, oc.getSize());
			pstmt.setString(21, oc.getSound());
			pstmt.setString(22, oc.getSpeed());
			pstmt.setString(23, oc.getSwitches());
			pstmt.setString(24, oc.getText());
			pstmt.setString(25, oc.getVram());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		sql = "INSERT INTO link (system_id, url, description) VALUES(?, ?, ?)";

		try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setLong(1, systemId);
			pstmt.setString(2, oc.getUrl());
			pstmt.setString(3, "old-computers.com");
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("--->" + e.getMessage());
		}

	}

	private void importData() {
		insertManufacturer();
		for (OldComputer oc : computers) {
			insert(oc);
		}

	}

	private void deleteData() throws SQLException {
		Connection conn = this.connect();
		conn.prepareStatement("DELETE FROM manufacturer").execute();
		conn.prepareStatement("DELETE FROM link").execute();
		conn.prepareStatement("DELETE FROM system").execute();
		conn.prepareStatement("DELETE FROM technical_information").execute();
	}

	/**
	 * WARNING! Must have the CATEGORY table created and populated BEFORE running this script. Check octupusgdb project.
	 * 
	 * @param args
	 * @throws SQLException
	 */
	public static void main(String[] args) throws SQLException {
		ImportToSQLite imp = new ImportToSQLite();
		imp.deleteData();
		imp.importData();
	}

}
