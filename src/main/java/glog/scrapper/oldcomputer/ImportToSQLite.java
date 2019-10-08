package glog.scrapper.oldcomputer;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
