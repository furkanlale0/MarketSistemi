package urunTipi;

public abstract class Urun {
    private int urunId;
    private String ad;
    private double fiyat;
    private int stokAdeti;

    public Urun(int urunId, String ad, double fiyat, int stokAdeti) {
        seturunId(urunId);
        setAd(ad);
        setFiyat(fiyat);
        setStokAdeti(stokAdeti);
    }

    public int getUrunId() {
        return urunId;
    }
    public void seturunId(int urunId) {
        if(urunId > 0){
            this.urunId = urunId;
        }else{
            System.out.println("Geçerli ürün id giriniz!");
        }
    }
    public String getAd() {
        return ad;
    }
    public void setAd(String ad) {
        if(ad != null){
            this.ad = ad;
        }else{
            System.out.println("Geçerli bir karakter giriniz!");
        }
    }
    public double getFiyat() {
        return fiyat;
    }
    public void setFiyat(double fiyat) {
        if(fiyat > 0){
            this.fiyat = fiyat;
        }else{
            System.out.println("Geçerli fiyat giriniz!");
        }
    }
    public int getStokAdeti() {
        return stokAdeti;
    }
    public void setStokAdeti(int stokAdeti) {
        if(stokAdeti > 0){
            this.stokAdeti = stokAdeti;
        }else{
            System.out.println("Geçerli stok değeri giriniz!");
        }
    }

    public void bilgiGoster(){
        System.out.println("Ürün id: " + getUrunId());
        System.out.println("Ürün adı: "+getAd());
        System.out.println("Ürün fiyat: "+getFiyat());
    }
}