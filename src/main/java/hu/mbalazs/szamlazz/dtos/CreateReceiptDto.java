package hu.mbalazs.szamlazz.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateReceiptDto {
    private Boolean pdfDownload;
    private String prefix;
    private String paymentMethod;
    private String currency;
    private String note;
    private List<ReceiptItemsDto.ReceiptItemDto> itemList;
    private List<PaymentItemsDto.PaymentItemDto> paymentList;
}
