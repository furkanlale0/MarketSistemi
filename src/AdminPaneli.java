import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import urunTipi.Urun;
import urunTipi.GidaUrunu;
import urunTipi.ElektronikUrunu;

public class AdminPaneli extends JFrame {

    private DefaultTableModel model;
    private JTable tablo;
    private UrunDAO dbIslemleri = new UrunDAO(); // Tek yetkili sınıf

    // Arayüz Elemanları
    private JTextField txtAd, txtFiyat, txtStok, txtSKT, txtGaranti, txtMarka;
    private JComboBox<String> cmbTur;

    public AdminPaneli() {
        setTitle("Yönetici Paneli");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(new Color(230, 230, 250));

        // --- ANA MENÜ BUTONU ---
        JButton btnAnaMenu = new JButton("Ana Menü");
        btnAnaMenu.setBounds(750, 20, 100, 30);
        add(btnAnaMenu);
        btnAnaMenu.addActionListener(e -> {
            dispose();
            new GirisEkrani().main(null);
        });

        // --- TABLO ---
        String[] kolonlar = {"ID", "Ürün Adı", "Fiyat", "Stok", "Tür", "SKT", "Garanti", "Marka"};
        model = new DefaultTableModel(kolonlar, 0);
        tablo = new JTable(model);
        JScrollPane sc = new JScrollPane(tablo);
        sc.setBounds(20, 60, 840, 240);
        add(sc);

        // --- FORM ALANLARI ---
        addLabel("Ürün Adı:", 20, 320);     txtAd = addTextField(100, 320);
        addLabel("Fiyat:", 300, 320);       txtFiyat = addTextField(350, 320);
        addLabel("Stok:", 20, 360);         txtStok = addTextField(100, 360);
        addLabel("Tür:", 300, 360);

        cmbTur = new JComboBox<>(new String[]{"Gida", "Elektronik"});
        cmbTur.setBounds(350, 360, 150, 25);
        add(cmbTur);

        addLabel("SKT:", 20, 400);          txtSKT = addTextField(100, 400);
        addLabel("Garanti:", 300, 400);     txtGaranti = addTextField(400, 400);
        addLabel("Marka:", 300, 430);       txtMarka = addTextField(400, 430);

        // --- BUTONLAR ---
        JButton btnEkle = new JButton("EKLE");
        btnEkle.setBounds(600, 320, 100, 40);
        add(btnEkle);

        JButton btnSil = new JButton("SİL");
        btnSil.setBounds(710, 320, 100, 40);
        add(btnSil);

        JButton btnGuncelle = new JButton("GÜNCELLE");
        btnGuncelle.setBounds(600, 370, 210, 40);
        add(btnGuncelle);

        // --- AKSİYONLAR (DİREKT DAO ÇAĞIRIYORUZ) ---

        // LİSTELEME
        tabloyuGuncelle();

        // EKLEME
        btnEkle.addActionListener(e -> {
            String tur = cmbTur.getSelectedItem().toString();
            boolean sonuc = dbIslemleri.urunEkle(
                    txtAd.getText(),
                    Double.parseDouble(txtFiyat.getText()),
                    Integer.parseInt(txtStok.getText()),
                    tur,
                    tur.equals("Gida") ? txtSKT.getText() : "-",
                    tur.equals("Elektronik") ? txtGaranti.getText() : "-",
                    tur.equals("Elektronik") ? txtMarka.getText() : "-"
            );
            if(sonuc) { tabloyuGuncelle(); temizle(); }
        });

        // SİLME
        btnSil.addActionListener(e -> {
            int row = tablo.getSelectedRow();
            if(row != -1) {
                int id = (int) model.getValueAt(row, 0);
                if(dbIslemleri.urunSil(id)) { tabloyuGuncelle(); temizle(); }
            }
        });

        // GÜNCELLEME
        btnGuncelle.addActionListener(e -> {
            int row = tablo.getSelectedRow();
            if(row != -1) {
                int id = (int) model.getValueAt(row, 0);
                String tur = cmbTur.getSelectedItem().toString();
                boolean sonuc = dbIslemleri.urunGuncelle(
                        id, txtAd.getText(), Double.parseDouble(txtFiyat.getText()), Integer.parseInt(txtStok.getText()),
                        tur,
                        tur.equals("Gida") ? txtSKT.getText() : "-",
                        tur.equals("Elektronik") ? txtGaranti.getText() : "-",
                        tur.equals("Elektronik") ? txtMarka.getText() : "-"
                );
                if(sonuc) { tabloyuGuncelle(); temizle(); }
            }
        });

        // TABLO TIKLAMA (Doldurma)
        tablo.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tablo.getSelectedRow();
                if(row != -1) {
                    txtAd.setText(model.getValueAt(row, 1).toString());
                    txtFiyat.setText(model.getValueAt(row, 2).toString());
                    txtStok.setText(model.getValueAt(row, 3).toString());
                    cmbTur.setSelectedItem(model.getValueAt(row, 4).toString());
                    // Diğer detayları da buraya ekleyebilirsin...
                }
            }
        });
    }

    private void tabloyuGuncelle() {
        model.setRowCount(0);
        List<Urun> liste = dbIslemleri.tumUrunleriGetir(); // DAO'dan çek
        for(Urun u : liste) {
            String tur = (u instanceof GidaUrunu) ? "Gida" : "Elektronik";
            String skt = (u instanceof GidaUrunu) ? ((GidaUrunu)u).getSonKullanimTarihi() : "-";
            String grnt = (u instanceof ElektronikUrunu) ? ((ElektronikUrunu)u).getGarantiSuresi() : "-";
            String mrk = (u instanceof ElektronikUrunu) ? ((ElektronikUrunu)u).getMarka() : "-";
            model.addRow(new Object[]{u.getUrunId(), u.getAd(), u.getFiyat(), u.getStokAdeti(), tur, skt, grnt, mrk});
        }
    }

    private void temizle() { txtAd.setText(""); txtFiyat.setText(""); txtStok.setText(""); txtSKT.setText(""); txtGaranti.setText(""); txtMarka.setText(""); }
    private void addLabel(String t, int x, int y) { JLabel l = new JLabel(t); l.setBounds(x, y, 100, 25); add(l); }
    private JTextField addTextField(int x, int y) { JTextField t = new JTextField(); t.setBounds(x, y, 150, 25); add(t); return t; }
}