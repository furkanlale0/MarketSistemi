package kampanya;

public class YuzdeOnIndirim implements IKampanya {

    @Override
    public String kampanyaAdi(){
        return "Hoşgeldin %10 İndirimi";
    }

    @Override
    public double indirimliFiyatHesapla(double gunceltoplam){
        return gunceltoplam * 0.90;
    }
}
