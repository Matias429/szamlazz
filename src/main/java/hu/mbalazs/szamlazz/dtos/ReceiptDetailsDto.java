package hu.mbalazs.szamlazz.dtos;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class ReceiptDetailsDto {
    private Long id;

    @XmlElement(name = "hivasAzonosito")
    private String callId;

    @XmlElement(name = "nyugtaszam")
    private String receiptNumber;

    @XmlElement(name = "tipus")
    private String receiptType;

    @XmlElement(name = "stornozott")
    private Boolean isCancelled;

    @XmlElement(name = "kelt")
    private String receiptDate;

    @XmlElement(name = "fizmod")
    private String paymentMethod;

    @XmlElement(name = "penznem")
    private String currency;

    @XmlElement(name = "nyugtaPdf")
    private String receiptPdf;
}
