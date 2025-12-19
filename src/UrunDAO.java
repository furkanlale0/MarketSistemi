import Baglanti.VeriTabaniBaglantisi;
import urunTipi.ElektronikUrunu;
import urunTipi.GidaUrunu;
import urunTipi.Urun;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UrunDAO {

    // 1. TÜM ÜRÜNLERİ GETİR
    public List<Urun> tumUrunleriGetir() {
        List<Urun> liste = new ArrayList<>();
        try (Connection conn = VeriTabaniBaglantisi.baglan()) {
            String sql = "SELECT * FROM urunler";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                String ad = rs.getString("urun_adi");
                double fiyat = rs.getDouble("fiyat");
                int stok = rs.getInt("stok");
                String tur = rs.getString("tur");

                if (tur.equalsIgnoreCase("Gida")) {
                    String skt = rs.getString("son_kullanim_tarihi");
                    liste.add(new GidaUrunu(id, ad, fiyat, stok, skt));
                } else if (tur.equalsIgnoreCase("Elektronik")) {
                    String garanti = rs.getString("garanti_suresi");
                    String marka = rs.getString("marka");
                    liste.add(new ElektronikUrunu(id, ad, fiyat, stok, garanti, marka));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return liste;
    }

    // 2. ÜRÜN EKLE
    public static boolean urunEkle(String ad, double fiyat, int stok, String tur, String skt, String garanti, String marka) {
        try (Connection conn = VeriTabaniBaglantisi.baglan()) {
            String sql = "INSERT INTO urunler (urun_adi, fiyat, stok, tur, son_kullanim_tarihi, garanti_suresi, marka) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, ad);
            ps.setDouble(2, fiyat);
            ps.setInt(3, stok);
            ps.setString(4, tur);
            ps.setString(5, (tur.equals("Gida") ? skt : "-"));
            ps.setString(6, (tur.equals("Elektronik") ? garanti : "-"));
            ps.setString(7, (tur.equals("Elektronik") ? marka : "-"));

            ps.executeUpdate();
            return true; // Başarılı
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Hata var
        }
    }

    // 3. ÜRÜN SİL
    public static boolean urunSil(int id) {
        try (Connection conn = VeriTabaniBaglantisi.baglan()) {
            String sql = "DELETE FROM urunler WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. ÜRÜN GÜNCELLE
    public static boolean urunGuncelle(int id, String ad, double fiyat, int stok, String tur, String skt, String garanti, String marka) {
        try (Connection conn = VeriTabaniBaglantisi.baglan()) {
            String sql = "UPDATE urunler SET urun_adi=?, fiyat=?, stok=?, tur=?, son_kullanim_tarihi=?, garanti_suresi=?, marka=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, ad);
            ps.setDouble(2, fiyat);
            ps.setInt(3, stok);
            ps.setString(4, tur);
            ps.setString(5, (tur.equals("Gida") ? skt : "-"));
            ps.setString(6, (tur.equals("Elektronik") ? garanti : "-"));
            ps.setString(7, (tur.equals("Elektronik") ? marka : "-"));
            ps.setInt(8, id);

            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. STOK DÜŞÜR
    public static void stokDusur(List<Urun> sepetUrunleri) {
        try (Connection conn = VeriTabaniBaglantisi.baglan()) {
            String sql = "UPDATE urunler SET stok = stok - 1 WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            for (Urun u : sepetUrunleri) {
                ps.setInt(1, u.getUrunId());
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}