/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nob.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author alimk
 */
public class koneksi {

    private Connection connect;
    private String driverName = "net.sourceforge.jtds.jdbc.Driver"; // Driver Untuk Koneksi Ke SQLServer   
    private String jdbc = "jdbc:jtds:sqlserver://localhost:1433/NOBTech";
    private String host = "localhost"; // IP Host PC-Ridho
    private String port = "1433"; // Port Default SQLServer   
    private String database = "NOBTech"; // Ini Database yang akan digunakan   
    private String url = jdbc + host + port + database;
    private String username = "sa"; // username default SQLServer   
    private String password = "aditya12345";

    public Connection getKoneksi() throws SQLException {
        if (connect == null) {
            try {
                Class.forName(driverName);
                System.out.println("Class Driver Ditemukan");
                try {
                    connect = DriverManager.getConnection(url, username, password);
                    System.out.println("Connection Success");
                } catch (SQLException se) {
                    System.out.println("Connection Failed" + se);
                    System.exit(0);
                }
            } catch (ClassNotFoundException cnfe) {
                System.out.println("Class Driver Tidak Ditemukan, Terjadi Kesalahan Pada : " + cnfe);
                System.exit(0);
            }
        }
        return connect;
    }
}
