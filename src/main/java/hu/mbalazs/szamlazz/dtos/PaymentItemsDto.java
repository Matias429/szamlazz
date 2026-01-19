package hu.mbalazs.szamlazz.dtos;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;

import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class PaymentItemsDto {

    @XmlElement(name = "kifizetes")
    private List<PaymentItemDto> items;

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class PaymentItemDto {
        private String fizetoeszkoz;
        private Double osszeg;
    }
}
