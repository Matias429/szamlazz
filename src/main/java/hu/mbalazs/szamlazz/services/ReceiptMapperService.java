package hu.mbalazs.szamlazz.services;

import hu.mbalazs.szamlazz.database.entities.ReceiptEntity;
import hu.mbalazs.szamlazz.database.entities.PaymentItemEntity;
import hu.mbalazs.szamlazz.database.entities.ReceiptItemEntity;
import hu.mbalazs.szamlazz.dtos.ReceiptDto;
import hu.mbalazs.szamlazz.dtos.PaymentItemsDto;
import hu.mbalazs.szamlazz.dtos.ReceiptItemsDto;
import org.springframework.stereotype.Service;

@Service
public class ReceiptMapperService {

    public ReceiptEntity toEntity(ReceiptDto dto) {

        ReceiptEntity receipt = new ReceiptEntity(dto.getDetails().getId(), dto.getDetails().getCallId(),
                dto.getDetails().getReceiptNumber(), dto.getDetails().getReceiptType(),
                dto.getDetails().getIsCancelled(), dto.getDetails().getReceiptDate(),
                dto.getDetails().getPaymentMethod(), dto.getDetails().getCurrency(),
                dto.getAmountList().getTotalAmounts().getGross(),
                dto.getAmountList().getTotalAmounts().getNet(),
                dto.getAmountList().getTotalAmounts().getVat());

        if (dto.getDetails().getNote() != null) {
            receipt.setNote(dto.getDetails().getNote());
        }

        for (ReceiptItemsDto.ReceiptItemDto itemDto : dto.getItemList().getItemList()) {
            ReceiptItemEntity item = new ReceiptItemEntity(itemDto.getName(), itemDto.getAmount(), itemDto.getUnitOfMeasure(),
                    itemDto.getNetUnitPrice(), itemDto.getVatRate(),
                    itemDto.getNet(), itemDto.getVat(), itemDto.getGross());
            receipt.addReceiptItem(item);
        }

        if (dto.getPaymentList() != null) {
            for (PaymentItemsDto.PaymentItemDto payDto : dto.getPaymentList().getPaymentList()) {
                PaymentItemEntity payment = new PaymentItemEntity(payDto.getMeansOfPayment(), payDto.getAmount());
                receipt.addPaymentItem(payment);
            }
        }
        return receipt;
    }
}
