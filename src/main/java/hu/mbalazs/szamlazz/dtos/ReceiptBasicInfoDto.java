package hu.mbalazs.szamlazz.dtos;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class ReceiptBasicInfoDto {
    private String hivasAzonosito;
    private String nyugtaszam;
    private String tipus;
    private Boolean stornozott;
    private String kelt;
    private String fizmod;
    private String penznem;
}
