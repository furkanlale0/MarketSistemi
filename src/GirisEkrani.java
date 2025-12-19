import Baglanti.VeriTabaniBaglantisi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GirisEkrani {

    public static void main(String[] args) {
        JFrame girisPenceresi = new JFrame("Java Market Otomasyonu - Giriş");
        girisPenceresi.setSize(400, 300);
        girisPenceresi.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        girisPenceresi.setLayout(null);
        girisPenceresi.getContentPane().setBackground(new Color(240, 248, 255));
        girisPenceresi.setLocationRelativeTo(null); // Ekranın ortasında açılır

        // Başlık
        JLabel lblBaslik = new JLabel("HOŞGELDİNİZ");
        lblBaslik.setBounds(120, 30, 200, 30);
        lblBaslik.setFont(new Font("Arial", Font.BOLD, 20));
        lblBaslik.setForeground(new Color(25, 25, 112));
        girisPenceresi.add(lblBaslik);

        // Müşteri Butonu
        JButton btnMusteri = new JButton("Müşteri Girişi (Alışveriş)");
        btnMusteri.setBounds(80, 90, 240, 45);
        btnMusteri.setBackground(new Color(60, 179, 113)); // Yeşil
        btnMusteri.setForeground(Color.WHITE);
        girisPenceresi.add(btnMusteri);

        // Admin Butonu
        JButton btnAdmin = new JButton("Yönetici (Admin) Girişi");
        btnAdmin.setBounds(80, 150, 240, 45);
        btnAdmin.setBackground(new Color(70, 130, 180)); // Mavi
        btnAdmin.setForeground(Color.WHITE);
        girisPenceresi.add(btnAdmin);

//musteri girisi
        btnMusteri.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                girisPenceresi.dispose(); // Giriş ekranını kapat
                MarketForm.marketiBaslat(); // Senin yazdığın MarketForm'u başlat
            }
        });

//admin girisi
        btnAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Küçük bir giriş paneli oluşturuyoruz
                JPanel panel = new JPanel(new GridLayout(2, 2));
                JTextField txtKullanici = new JTextField();
                JPasswordField txtSifre = new JPasswordField();

                panel.add(new JLabel("Kullanıcı Adı:"));
                panel.add(txtKullanici);
                panel.add(new JLabel("Şifre:"));
                panel.add(txtSifre);

                int sonuc = JOptionPane.showConfirmDialog(girisPenceresi, panel, "Yönetici Girişi", JOptionPane.OK_CANCEL_OPTION);

                if (sonuc == JOptionPane.OK_OPTION) {
                    String kAdi = txtKullanici.getText();
                    String sifre = new String(txtSifre.getPassword());

                    // Veritabanı kontrolü
                    if (adminDogrula(kAdi, sifre)) {
                        JOptionPane.showMessageDialog(girisPenceresi, "Giriş Başarılı!");
                        girisPenceresi.dispose();

                        new AdminPaneli().setVisible(true);
                        // --------------------------------

                    }else {
                        JOptionPane.showMessageDialog(girisPenceresi, "Hatalı Kullanıcı Adı veya Şifre!", "Hata", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        girisPenceresi.setVisible(true);
    }

    // VERİTABANI KONTROL
    public static boolean adminDogrula(String kadi, String sifre) {
        boolean girisBasarili = false;

        // VeritabanıBaglantisi sınıfından bağlantıyı alır
        try (Connection conn = VeriTabaniBaglantisi.baglan()) {

            String sql = "SELECT * FROM adminler WHERE kullanici_adi = ? AND sifre = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, kadi);
            ps.setString(2, sifre);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                girisBasarili = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return girisBasarili;
    }
}