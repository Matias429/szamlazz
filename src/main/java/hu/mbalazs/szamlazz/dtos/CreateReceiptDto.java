package hu.mbalazs.szamlazz.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateReceiptDto {
    private Boolean pdfLetoltes;
    private String elotag;
    private String fizmod;
    private String penznem;
    private String megjegyzes;
    private List<ReceiptItemsDto.ReceiptItemDto> tetelek;
    private List<PaymentItemsDto.PaymentItemDto> kifizetesek;
}
