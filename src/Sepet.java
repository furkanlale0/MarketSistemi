import urunTipi.Urun;

import java.util.ArrayList;
import java.util.List;

public class Sepet {
    private List<Urun> urunler=new ArrayList<Urun>();

    public Sepet() {
        setUrunler(urunler);
    }
    public List<Urun> getUrunler() {
        return urunler;
    }
    public void setUrunler(List<Urun> urunler) {
        this.urunler = urunler;
    }

    public void sepeteEkle(Urun urun){
        urunler.add(urun);
        urun.setStokAdeti(urun.getStokAdeti()-1);
        System.out.println(urun.getAd()+" sepetinize eklendi.İyi alışverişler!");
    }

    public void sepettenCikar(Urun urun){
        urunler.remove(urun);
        urun.setStokAdeti(urun.getStokAdeti()+1);
        System.out.println(urun.getAd()+" sepetinizden çıkarıldı.iyi alışverişler!");
    }

    public double toplamHesapla(){
        double toplam = 0;

        for(Urun urun : urunler){
            toplam=urun.getFiyat()+toplam;
        }
        return toplam;
    }
    public void sepetiTemizle(){
        urunler.clear();
        System.out.println("Sepet Boşaltıldı!");
    }
    public void sepetiYazdir() {
        if (urunler.isEmpty()) {
            System.out.println("Sepetiniz şu an boş.");
        } else {
            for (Urun u : urunler) {
                System.out.println("- " + u.getAd() + " (" + u.getFiyat() + " TL)");
            }
        }
    }
}
