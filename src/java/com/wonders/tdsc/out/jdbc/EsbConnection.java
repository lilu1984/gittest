package com.wonders.tdsc.out.jdbc;

public class EsbConnection {
	private static java.sql.Connection	con	= null;

	// Constructor
	public EsbConnection() {
	}

	public static java.sql.Connection getConnection() {
		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");

			//Ô­¿âcon = java.sql.DriverManager.getConnection("jdbc:oracle:thin:@32.51.213.116:1521:WXESB", "qlyxjk", "qlyxjk");
			con = java.sql.DriverManager.getConnection("jdbc:oracle:thin:@32.51.213.116:1521:WXQLYXJK", "qlyxjk", "qlyxjk");
			//con = java.sql.DriverManager.getConnection("jdbc:oracle:thin:@192.168.10.13:1521:WXESB", "qlyxjk", "qlyxjk");
			//con = java.sql.DriverManager.getConnection("jdbc:oracle:thin:@10.168.105.48:1521:fdjbo", "WXLANDTRADE", "WXLANDTRADE");

			if (con != null)
				System.out.println("Connection Oracle [32.51.213.116] Successful!");
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
		EsbConnection myDbTest = new EsbConnection();
		myDbTest.displayDbProperties();

	}
}
