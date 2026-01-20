package hu.mbalazs.szamlazz.dtos;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "xmlnyugtavalasz")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class ResponseDto {

    @XmlElement(name = "sikeres")
    private Boolean sikeres;

    @XmlElement(name = "nyugtaPdf")
    private String nyugtaPdf;

    @XmlElement(name = "nyugta")
    private ReceiptDto nyugta;

    @XmlElement(name = "hibakod")
    private String hibakod;

    @XmlElement(name = "hibauzenet")
    private String hibauzenet;
}