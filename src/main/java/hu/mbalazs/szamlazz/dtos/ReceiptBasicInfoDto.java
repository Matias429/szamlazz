package hu.mbalazs.szamlazz.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReceiptBasicInfoDto {
    String callId;
    String receiptNumber;
    String receiptDate;
    String currency;
    Double net;
    Double gross;
}
