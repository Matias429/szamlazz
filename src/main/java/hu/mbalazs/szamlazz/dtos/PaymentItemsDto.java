package hu.mbalazs.szamlazz.dtos;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;



import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class PaymentItemsDto {

    @XmlElement(name = "kifizetes")
    private List<PaymentItemDto> paymentList;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class PaymentItemDto {
        
        @XmlElement(name = "fizetoeszkoz")
        private String meansOfPayment;
        
        @XmlElement(name = "osszeg")
        private Double amount;
    }
}
