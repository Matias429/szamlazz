package hu.mbalazs.szamlazz.dtos;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;

import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class ReceiptItemsDto {

    @XmlElement(name = "tetel")
    private List<ReceiptItemDto> items;

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ReceiptItemDto {

        public ReceiptItemDto(String megnevezes, Double mennyiseg, String mennyisegiEgyseg, Double nettoEgysegar, String afakulcs, Double netto, Double afa, Double brutto) {
            this.megnevezes = megnevezes;
            this.mennyiseg = mennyiseg;
            this.mennyisegiEgyseg = mennyisegiEgyseg;
            this.nettoEgysegar = nettoEgysegar;
            this.afakulcs = afakulcs;
            this.netto = netto;
            this.afa = afa;
            this.brutto = brutto;
        }

        public ReceiptItemDto(){}

        private String megnevezes;
        private Double mennyiseg;
        private String mennyisegiEgyseg;
        private Double nettoEgysegar;
        private String afakulcs;
        private Double netto;
        private Double afa;
        private Double brutto;

    }
}
