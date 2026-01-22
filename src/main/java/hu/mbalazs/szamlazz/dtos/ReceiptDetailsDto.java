package hu.mbalazs.szamlazz.dtos;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

import lombok.NoArgsConstructor;
import lombok.Data;

@NoArgsConstructor
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

    @XmlElement(name = "megjegyzes")
    private String note;

    public ReceiptDetailsDto(Long id, String callId, String receiptNumber, String receiptType, Boolean isCancelled, String receiptDate,
                             String paymentMethod, String currency) {
        this.id = id;
        this.callId = callId;
        this.receiptNumber = receiptNumber;
        this.receiptType = receiptType;
        this.isCancelled = isCancelled;
        this.receiptDate = receiptDate;
        this.paymentMethod = paymentMethod;
        this.currency = currency;
    }
}
