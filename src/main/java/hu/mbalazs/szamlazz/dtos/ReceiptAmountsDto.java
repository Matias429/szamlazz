package hu.mbalazs.szamlazz.dtos;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class ReceiptAmountsDto {

    @XmlElement(name = "totalossz")
    private TotalAmounts totalAmounts;

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class TotalAmounts {

        @XmlElement(name = "netto")
        private Double net;

        @XmlElement(name = "afa")
        private Double vat;

        @XmlElement(name = "brutto")
        private Double gross;
    }
}
