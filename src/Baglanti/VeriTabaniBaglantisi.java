package Baglanti;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class VeriTabaniBaglantisi {

    private static final String URL = "jdbc:mysql://localhost:3306/MarketOtomasyonu";
    private static final String KULLANICI_ADI = "root";
    private static final String SIFRE = "Asdera4545";

    public static Connection baglan() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, KULLANICI_ADI, SIFRE);
        } catch (SQLException e) {
            System.out.println("Bağlantı Hatası: " + e.getMessage());
        }
        return conn;
    }
}