import urunTipi.Urun;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FaturaKesici {
//static cünkü cagırılmak istendiği zaman nesne uretilmesine gerek olmamalı
    public static String faturayiOlustur(Sepet sepet, double eskiTutar, double yeniTutar) {

        StringBuilder fis = new StringBuilder(); // metinleri birleştirmek için en verimli yapı

        // tarih formatlama
        LocalDateTime simdi = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        // fiş Başlığı
        fis.append("\n===================================\n");
        fis.append("       JAVA MARKET - SATIŞ FİŞİ    \n");
        fis.append("===================================\n");
        fis.append("Tarih: ").append(simdi.format(format)).append("\n");
        fis.append("-----------------------------------\n");

        // ürünleri tek tek ekle
        for (Urun u : sepet.getUrunler()) {
            //sola dayalı 15 karakterde sınırla ayrıca virgülden sonra 2 karakteri yaz
            fis.append(String.format("%-15s : %.2f TL\n", u.getAd(), u.getFiyat()));
        }

        fis.append("-----------------------------------\n");
        fis.append("Ara Toplam          : ").append(eskiTutar).append(" TL\n");

        // indirim varsa göster
        double indirim = eskiTutar - yeniTutar;
        if (indirim > 0) {
            fis.append("Kampanya İndirimi   : -").append(String.format("%.2f", indirim)).append(" TL\n");
        }

        fis.append("-----------------------------------\n");
        fis.append("GENEL TOPLAM        : ").append(yeniTutar).append(" TL\n");
        fis.append("===================================\n");
        fis.append("      Bizi Tercih Ettiğiniz İçin   \n");
        fis.append("            Teşekkür Ederiz!       \n");
        fis.append("===================================\n");

        return fis.toString();
    }
}