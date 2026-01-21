package hu.mbalazs.szamlazz.dtos;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class ReceiptDto {

    @XmlElement(name = "alap")
    private ReceiptDetailsDto details;

    @XmlElement(name = "tetelek")
    private ReceiptItemsDto itemList;

    @XmlElement(name = "kifizetesek")
    private PaymentItemsDto paymentList;

    @XmlElement(name = "osszegek")
    private ReceiptAmountsDto amountList;
}

