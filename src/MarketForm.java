import kampanya.BuyukSepetIndirimi;
import kampanya.IKampanya;
import kampanya.YuzdeOnIndirim;
import urunTipi.Urun;
import urunTipi.GidaUrunu;       // Tür kontrolü yapıp SKT göstermek istersen lazım olabilir
import urunTipi.ElektronikUrunu; // Marka göstermek istersen lazım olabilir

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MarketForm {
    // Statik sepet (Program açık kaldığı sürece sepeti tutar)
    static Sepet musteriSepeti = new Sepet();

    // NOT: Artık main metodu değil, GirisEkrani'ndan çağrılan bir metot
    public static void marketiBaslat() {

        // 1. VERİLERİ YÜKLE (DAO KULLANARAK)
        // MarketSystem'in içi boşaldığı için doldurma işini burası yapıyor
        UrunDAO dao = new UrunDAO();
        MarketSystem.marketRafi = dao.tumUrunleriGetir();

        // 2. PENCERE TASARIMI
        JFrame pencere = new JFrame("Java Market Otomasyonu - Müşteri Ekranı");
        pencere.setSize(500, 650);
        pencere.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pencere.setLayout(null);
        pencere.getContentPane().setBackground(new Color(230, 240, 255));
        pencere.setLocationRelativeTo(null); // Ekranı ortala

        // --- GERİ DÖN BUTONU ---
        JButton btnGeri = new JButton("<< Geri Dön");
        btnGeri.setBounds(10, 20, 110, 30);
        btnGeri.setBackground(new Color(220, 20, 60)); // Kırmızımsı
        btnGeri.setForeground(Color.WHITE);
        btnGeri.setFont(new Font("Arial", Font.BOLD, 12));
        pencere.add(btnGeri);

        btnGeri.addActionListener(e -> {
            pencere.dispose(); // Market ekranını kapat
            GirisEkrani.main(null); // Giriş ekranına dön
        });

        // Başlık
        JLabel baslik = new JLabel("MARKET İŞLEM MENÜSÜ");
        baslik.setBounds(130, 20, 300, 30);
        baslik.setFont(new Font("Arial", Font.BOLD, 18));
        baslik.setForeground(new Color(0, 51, 102));
        pencere.add(baslik);

        // Ekran (JTextArea)
        JTextArea ekran = new JTextArea();
        ekran.setBounds(50, 70, 380, 280);
        ekran.setFont(new Font("Monospaced", Font.PLAIN, 12));
        ekran.setEditable(false);
        ekran.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        pencere.add(ekran);

        // Butonlar
        JButton btnListele = new JButton("Ürünleri Listele");
        btnListele.setBounds(50, 350, 180, 40);
        pencere.add(btnListele);

        JButton btnEkle = new JButton("Sepete Ürün Ekle");
        btnEkle.setBounds(250, 350, 180, 40);
        pencere.add(btnEkle);

        JButton btnSepet = new JButton("Sepetim");
        btnSepet.setBounds(50, 410, 180, 40);
        pencere.add(btnSepet);

        JButton btnOdeme = new JButton("Ödeme Yap");
        btnOdeme.setBounds(250, 410, 180, 40);
        btnOdeme.setBackground(new Color(255, 140, 0)); // Turuncu
        btnOdeme.setForeground(Color.WHITE);
        pencere.add(btnOdeme);

        // --- AKSİYONLAR ---

        // 1. LİSTELEME
        btnListele.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ekran.setText("--- MARKETTEKİ ÜRÜNLER ---\n\n");
                // MarketSystem rafındaki güncel ürünleri yazdır
                for (Urun u : MarketSystem.marketRafi) {
                    // Stok 0 ise (Tükendi) yazabiliriz, opsiyonel
                    String stokDurumu = (u.getStokAdeti() > 0) ? "Stok:" + u.getStokAdeti() : "[TÜKENDİ]";
                    ekran.append(String.format("ID: %d - %-15s : %.2f TL (%s)\n", u.getUrunId(), u.getAd(), u.getFiyat(), stokDurumu));
                }
            }
        });

        // 2. EKLEME
        btnEkle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String girilenId = JOptionPane.showInputDialog(pencere, "Almak istediğiniz ürünün ID'sini girin:");
                if (girilenId != null && !girilenId.isEmpty()) {
                    try {
                        int id = Integer.parseInt(girilenId);
                        Urun bulunan = MarketSystem.urunBulun(id);

                        if (bulunan != null) {
                            if (bulunan.getStokAdeti() > 0) {
                                musteriSepeti.sepeteEkle(bulunan);
                                JOptionPane.showMessageDialog(pencere, bulunan.getAd() + " sepete eklendi!");
                            } else {
                                JOptionPane.showMessageDialog(pencere, "Üzgünüz, bu ürün stokta kalmadı!", "Stok Hatası", JOptionPane.WARNING_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(pencere, "Ürün bulunamadı!", "Hata", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(pencere, "Lütfen sayı giriniz!", "Hata", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // 3. SEPETİ GÖR
        btnSepet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ekran.setText("--- SEPETİNİZ ---\n\n");
                double toplam = musteriSepeti.toplamHesapla();
                if (toplam == 0) {
                    ekran.append("Sepetiniz boş.");
                } else {
                    for (Urun u : musteriSepeti.getUrunler()) {
                        ekran.append(String.format("- %-15s : %.2f TL\n", u.getAd(), u.getFiyat()));
                    }
                    ekran.append("\n--------------------------\nTOPLAM: " + toplam + " TL");
                }
            }
        });

        // 4. ÖDEME VE FATURA (Stok Düşme Entegreli)
        btnOdeme.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double hamTutar = musteriSepeti.toplamHesapla();

                if (hamTutar == 0) {
                    JOptionPane.showMessageDialog(pencere, "Sepet boş!", "Uyarı", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Kampanya Hesapla (3000 TL Kuralı)
                IKampanya aktifKampanya = (hamTutar >= 3000) ? new BuyukSepetIndirimi() : new YuzdeOnIndirim();
                double odenecekTutar = aktifKampanya.indirimliFiyatHesapla(hamTutar);

                // Ödeme Seçenekleri
                String[] secenekler = {"Nakit", "Kredi Kartı"};
                int secim = JOptionPane.showOptionDialog(pencere,
                        "Tutar: " + hamTutar + " TL\nKampanya: " + aktifKampanya.kampanyaAdi() +
                                "\nÖDENECEK: " + odenecekTutar + " TL\n\nÖdeme Yöntemi:",
                        "Ödeme Ekranı", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, secenekler, secenekler[0]);

                boolean islemBasarili = false;
                String ekMesaj = "";

                // NAKİT
                if (secim == 0) {
                    String verilenStr = JOptionPane.showInputDialog(pencere, "Ödenecek: " + odenecekTutar + " TL\nVerilen Para:");
                    if (verilenStr != null) {
                        try {
                            double verilen = Double.parseDouble(verilenStr);
                            if (verilen >= odenecekTutar) {
                                islemBasarili = true;
                                ekMesaj = "\nPara Üstü: " + (verilen - odenecekTutar) + " TL";
                            } else {
                                JOptionPane.showMessageDialog(pencere, "Yetersiz Bakiye!", "Hata", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(pencere, "Geçersiz Tutar!", "Hata", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                // KREDİ KARTI
                else if (secim == 1) {
                    String kartNo = JOptionPane.showInputDialog(pencere, "16 Haneli Kart No:");
                    if (kartNo != null && kartNo.length() == 16) {
                        String cvc = JOptionPane.showInputDialog(pencere, "3 Haneli CVC:");
                        if (cvc != null && cvc.length() == 3) {
                            islemBasarili = true;
                            ekMesaj = "\nKredi Kartı İşlemi Onaylandı.";
                        } else {
                            JOptionPane.showMessageDialog(pencere, "Hatalı CVC!", "Hata", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(pencere, "Hatalı Kart No!", "Hata", JOptionPane.ERROR_MESSAGE);
                    }
                }

                // --- SONUÇ KISMI ---
                if (islemBasarili) {

                    // 1. Veritabanından Stokları Düş (DAO KULLANIYORUZ)
                    UrunDAO daoIslem = new UrunDAO();
                    daoIslem.stokDusur(musteriSepeti.getUrunler());

                    // 2. Faturayı Ekrana Bas
                    String faturaMetni = FaturaKesici.faturayiOlustur(musteriSepeti, hamTutar, odenecekTutar);
                    ekran.setText(faturaMetni);

                    JOptionPane.showMessageDialog(pencere, "Ödeme Başarılı!" + ekMesaj + "\nFaturanız ekrana yazdırıldı.");

                    // 3. Sepeti Temizle (Satış bitti)
                    musteriSepeti.sepetiTemizle();

                    // 4. Listeyi Yenile (Güncel stokları MarketSystem rafına yükle)
                    MarketSystem.marketRafi = daoIslem.tumUrunleriGetir();
                }
            }
        });

        pencere.setVisible(true);
    }
}