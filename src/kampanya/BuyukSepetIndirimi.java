package kampanya;

public class BuyukSepetIndirimi implements IKampanya {

    @Override
    public String kampanyaAdi() {
        return "EFSANE FIRSAT 3000 TL ÜZERİNE 1000 TL BİZDEN!";
    }
    @Override
    public double indirimliFiyatHesapla(double gunceltoplam) {
        if(gunceltoplam >= 3000){
            return gunceltoplam-1000;
    }else{
        return gunceltoplam;
    }

}}
