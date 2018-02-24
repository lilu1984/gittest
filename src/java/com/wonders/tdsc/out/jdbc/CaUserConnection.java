package com.wonders.tdsc.out.jdbc;

import java.sql.DriverManager;

public class CaUserConnection {
	private static java.sql.Connection	con	= null;

	// Constructor
	public CaUserConnection() {
	}

	public static java.sql.Connection getConnection() {
		try {

			// Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver"); // 2000 version
			// con = DriverManager.getConnection("jdbc:microsoft:sqlserver://<ServerName>;user=<UserName>;password=<Password>");//2000 version

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); // 2005 version
			con = DriverManager.getConnection("jdbc:sqlserver://192.168.10.6:1433;DatabaseName=Wesoft_WXGT;user=sa;password=sa");// 2005 version

			if (con != null)
				System.out.println("Connection SqlServer [192.168.10.6] Successful!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error Trace in getConnection() : " + e.getMessage());
		}
		return con;
	}

	/*
	 * 
	 * Display the driver properties, database details
	 */

	public void displayDbProperties() {
		java.sql.DatabaseMetaData dm = null;
		java.sql.ResultSet rs = null;
		try {
			con = this.getConnection();
			if (con != null) {
				dm = con.getMetaData();
				System.out.println("Driver Information");
				System.out.println("£ÜtDriver Name: " + dm.getDriverName());
				System.out.println("£ÜtDriver Version: " + dm.getDriverVersion());
				System.out.println("£ÜnDatabase Information ");
				System.out.println("£ÜtDatabase Name: " + dm.getDatabaseProductName());
				System.out.println("£ÜtDatabase Version: " + dm.getDatabaseProductVersion());
				System.out.println("Avalilable Catalogs ");
				rs = dm.getCatalogs();
				while (rs.next()) {
					System.out.println("£Ütcatalog: " + rs.getString(1));
				}
				rs.close();
				rs = null;
				closeConnection();
			} else
				System.out.println("Error: No active Connection");
		} catch (Exception e) {
			e.printStackTrace();
		}
		dm = null;
	}

	public static void closeConnection() {
		try {
			if (con != null)
				con.close();
			con = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		CaUserConnection myDbTest = new CaUserConnection();
		myDbTest.displayDbProperties();

	}
}
