package hu.mbalazs.szamlazz;


public class PaymentItem {
    String fizetoeszkoz;
    Double osszeg;

    public PaymentItem(String fizetoeszkoz, Double osszeg) {
        this.fizetoeszkoz = fizetoeszkoz;
        this.osszeg = osszeg;
    }

    public String getFizetoeszkoz() {
        return fizetoeszkoz;
    }

    public Double getOsszeg() {
        return osszeg;
    }
}
