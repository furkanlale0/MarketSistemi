package urunTipi;

public class ElektronikUrunu extends Urun {
    private String garantiSuresi;
    private String marka;

    public ElektronikUrunu(int urunId,String ad,double fiyat,int stokAdeti,String garantiSuresi, String marka) {
        super(urunId,ad,fiyat,stokAdeti);
        setGarantiSuresi(garantiSuresi);
        setMarka(marka);
    }
    public String getGarantiSuresi() {

        return garantiSuresi;
    }
    public void setGarantiSuresi(String garantiSuresi) {

        this.garantiSuresi = garantiSuresi;
    }
    public String getMarka() {

        return marka;
    }
    public void setMarka(String marka) {

        this.marka = marka;
    }
    @Override
    public void bilgiGoster() {
        super.bilgiGoster();
        System.out.println("Garanti suresi: "+getGarantiSuresi());
        System.out.println("Marka: "+getMarka());
    }

}
