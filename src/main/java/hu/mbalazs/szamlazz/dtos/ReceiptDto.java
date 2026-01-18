package hu.mbalazs.szamlazz.dtos;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class ReceiptDto {
    private ReceiptBasicInfoDto alap;
    private ReceiptItemsDto tetelek;
    private PaymentItemsDto kifizetesek;
    private ReceiptAmountsDto osszegek;
}

