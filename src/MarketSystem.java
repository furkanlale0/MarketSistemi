import urunTipi.Urun;
import java.util.ArrayList;
import java.util.List;

public class MarketSystem {
    // Sadece ürünlerin durduğu liste (Raf)
    public static List<Urun> marketRafi = new ArrayList<>();

    // ID ile ürün bulma (Sepet işlemleri için lazım, veritabanına gitmez, listeden bakar)
    public static Urun urunBulun(int id) {
        for (Urun urun : marketRafi) {
            if (urun.getUrunId() == id) {
                return urun;
            }
        }
        return null;
    }
}