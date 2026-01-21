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
    private List<ReceiptItemDto> itemList;

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ReceiptItemDto {

        @XmlElement(name = "megnevezes")
        private String name;

        @XmlElement(name = "mennyiseg")
        private Double amount;

        @XmlElement(name = "mennyisegiegyseg")
        private String unitOfMeasure;

        @XmlElement(name = "nettoEgysegar")
        private Double netUnitPrice;

        @XmlElement(name = "afakulcs")
        private String vatRate;

        @XmlElement(name = "netto")
        private Double net;

        @XmlElement(name = "afa")
        private Double vat;

        @XmlElement(name = "brutto")
        private Double gross;

    }
}
