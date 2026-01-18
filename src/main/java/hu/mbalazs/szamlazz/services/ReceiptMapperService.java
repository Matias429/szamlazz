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

        receipt.setHivasAzonosito(dto.getAlap().getHivasAzonosito());
        receipt.setNyugtaszam(dto.getAlap().getNyugtaszam());
        receipt.setTipus(dto.getAlap().getTipus());
        receipt.setStornozott(dto.getAlap().getStornozott());
        receipt.setKelt(dto.getAlap().getKelt());
        receipt.setFizmod(dto.getAlap().getFizmod());
        receipt.setPenznem(dto.getAlap().getPenznem());
        receipt.setVegosszegBrutto(dto.getOsszegek().getTotalossz().getBrutto());
        receipt.setVegosszegNetto(dto.getOsszegek().getTotalossz().getNetto());
        receipt.setVegosszegAfa(dto.getOsszegek().getTotalossz().getAfa());

        for (ReceiptItemsDto.ReceiptItemDto itemDto : dto.getTetelek().getItems()) {
            ReceiptItemEntity item = new ReceiptItemEntity();
            item.setMegnevezes(itemDto.getMegnevezes());
            item.setMennyiseg(itemDto.getMennyiseg());
            item.setMennyisegiEgyseg(itemDto.getMennyisegiEgyseg());
            item.setNettoEgysegar(itemDto.getNettoEgysegar());
            item.setAfakulcs(itemDto.getAfakulcs());
            item.setNetto(itemDto.getNetto());
            item.setAfa(itemDto.getAfa());
            item.setBrutto(itemDto.getBrutto());

            receipt.addReceiptItem(item);
        }

        if (dto.getKifizetesek() != null) {
            for (PaymentItemsDto.PaymentItemDto payDto : dto.getKifizetesek().getItems()) {
                PaymentItemEntity payment = new PaymentItemEntity();
                payment.setFizetoeszkoz(payDto.getFizetoeszkoz());
                payment.setOsszeg(payDto.getOsszeg());

                receipt.addPaymentItem(payment);
            }
        }

        if (dto.getOsszegek() != null) {
            receipt.setVegosszegNetto(dto.getOsszegek().getTotalossz().getNetto());
            receipt.setVegosszegAfa(dto.getOsszegek().getTotalossz().getAfa());
            receipt.setVegosszegBrutto(dto.getOsszegek().getTotalossz().getBrutto());
        }


        return receipt;
    }
}
