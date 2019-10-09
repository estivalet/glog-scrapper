package glog.scrapper.oldcomputer;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.Gson;

import glog.util.IOUtil;

public class ImportToSQLite {

	private Connection connect() {
		String url = "jdbc:sqlite:C://temp/octupus.db";
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}

	public void insert(OldComputer oc) {
		Long systemId = 0L;
		String sql = "INSERT INTO system (name, manufacturer, type, country, year, description, price)"
				+ "values (?, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, oc.getName());
			pstmt.setString(2, oc.getManufacturer());
			pstmt.setString(3, oc.getType());
			pstmt.setString(4, oc.getOrigin());
			pstmt.setString(5, oc.getYear());
			pstmt.setString(6, oc.getDescription());
			pstmt.setString(7, oc.getPrice());
			pstmt.executeUpdate();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select last_insert_rowid()");
			rs.next();
			systemId = rs.getLong(1);
			System.out.println("--->" + systemId);
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

	}

	public static void main(String[] args) {
		ImportToSQLite imp = new ImportToSQLite();

		String[] files = new File("data/oldcomputer/consoles/json").list();
		for (String file : files) {
			File json = new File("data/oldcomputer/consoles/json/" + file);
			OldComputer oc = new Gson().fromJson(IOUtil.getContents(json), OldComputer.class);
			System.out.println(oc.getManufacturer() + " " + oc.getName());
			imp.insert(oc);
		}

	}
}
