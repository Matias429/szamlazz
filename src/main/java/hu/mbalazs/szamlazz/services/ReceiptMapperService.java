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

        ReceiptEntity receipt = new ReceiptEntity();

        receipt.setId(dto.getDetails().getId());
        receipt.setCallId(dto.getDetails().getCallId());
        receipt.setReceiptNumber(dto.getDetails().getReceiptNumber());
        receipt.setReceiptType(dto.getDetails().getReceiptType());
        receipt.setIsCancelled(dto.getDetails().getIsCancelled());
        receipt.setReceiptDate(dto.getDetails().getReceiptDate());
        receipt.setPaymentMethod(dto.getDetails().getPaymentMethod());
        receipt.setCurrency(dto.getDetails().getCurrency());
        receipt.setTotalGross(dto.getAmountList().getTotalAmounts().getGross());
        receipt.setTotalNet(dto.getAmountList().getTotalAmounts().getNet());
        receipt.setTotalVat(dto.getAmountList().getTotalAmounts().getVat());

        for (ReceiptItemsDto.ReceiptItemDto itemDto : dto.getItemList().getItemList()) {
            ReceiptItemEntity item = new ReceiptItemEntity();
            item.setName(itemDto.getName());
            item.setAmount(itemDto.getAmount());
            item.setUnitOfMeasure(itemDto.getUnitOfMeasure());
            item.setNetUnitPrice(itemDto.getNetUnitPrice());
            item.setVatRate(itemDto.getVatRate());
            item.setNet(itemDto.getNet());
            item.setVat(itemDto.getVat());
            item.setGross(itemDto.getGross());

            receipt.addReceiptItem(item);
        }

        if (dto.getPaymentList() != null) {
            for (PaymentItemsDto.PaymentItemDto payDto : dto.getPaymentList().getPaymentList()) {
                PaymentItemEntity payment = new PaymentItemEntity();
                payment.setMeansOfPayment(payDto.getMeansOfPayment());
                payment.setAmount(payDto.getAmount());

                receipt.addPaymentItem(payment);
            }
        }
        return receipt;
    }
}
