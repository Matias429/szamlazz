package hu.mbalazs.szamlazz.dtos;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class ReceiptAmountsDto {

    @XmlElement(name = "totalossz")
    private TotalAmounts totalossz;

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class TotalAmounts {
        private Double netto;
        private Double afa;
        private Double brutto;
    }
}
