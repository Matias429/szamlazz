package hu.mbalazs.szamlazz;


public class ReceiptItem {
    String megnevezes;
    Double mennyiseg;
    String mennyisegiEgyseg;
    Double nettoEgysegar;
    String afakulcs;
    Double netto;
    Double afa;
    Double brutto;

    public ReceiptItem(String megnevezes, Double mennyiseg, String mennyisegiEgyseg, Double nettoEgysegar, String afakulcs, Double netto, Double afa, Double brutto) {
        this.megnevezes = megnevezes;
        this.mennyiseg = mennyiseg;
        this.mennyisegiEgyseg = mennyisegiEgyseg;
        this.nettoEgysegar = nettoEgysegar;
        this.afakulcs = afakulcs;
        this.netto = netto;
        this.afa = afa;
        this.brutto = brutto;
    }

    public String getMegnevezes() {
        return megnevezes;
    }

    public Double getMennyiseg() {
        return mennyiseg;
    }

    public String getMennyisegiEgyseg()
    {
        return mennyisegiEgyseg;
    }

    public Double getNettoEgysegar() {
        return nettoEgysegar;
    }

    public String getAfakulcs() {
        return afakulcs;
    }

    public Double getNetto() {
        return netto;
    }

    public Double getAfa() {
        return afa;
    }

    public Double getBrutto() {
        return brutto;
    }
}
