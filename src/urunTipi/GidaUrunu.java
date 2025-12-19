package urunTipi;

public class GidaUrunu extends Urun {
    private String sonKullanimTarihi;

    public GidaUrunu(int urunId,String ad,double fiyat,int stokAdeti,String sonKullanimTarihi) {
        super(urunId,ad,fiyat,stokAdeti);
        setSonKullanimTarihi(sonKullanimTarihi);
    }

    public String getSonKullanimTarihi() {
        return sonKullanimTarihi;
    }

    public void setSonKullanimTarihi(String sonKullanimTarihi) {
        this.sonKullanimTarihi = sonKullanimTarihi;
    }
    @Override
    public void bilgiGoster(){
        super.bilgiGoster();
        System.out.println("Son kullanım tarihi: "+getSonKullanimTarihi());
    }

}
