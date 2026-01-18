package hu.mbalazs.szamlazz.api;

import hu.mbalazs.szamlazz.dtos.*;
import hu.mbalazs.szamlazz.helpers.PaymentMethods;
import hu.mbalazs.szamlazz.services.ReceiptPersistenceService;
import hu.mbalazs.szamlazz.services.ReceiptWebClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class ReceiptController {

    private final ReceiptWebClientService client;
    private final ReceiptPersistenceService persistenceService;

    public ReceiptController(ReceiptWebClientService client, ReceiptPersistenceService persistenceService) {
        this.client = client;
        this.persistenceService = persistenceService;
    }

    @PostMapping(value = "/createReceipt")
    public ResponseEntity<ReceiptDto> createReceipt(String hivasAzonosito, Boolean pdfLetoltes, String elotag, PaymentMethods fizmod, String penznem, Optional<String> megjegyzes,
                                                    List<ReceiptItemsDto.ReceiptItemDto> tetelek, Optional<List<PaymentItemsDto.PaymentItemDto>> kifizetesek) {

        String response = client.createReceipt(hivasAzonosito, pdfLetoltes, elotag, fizmod, penznem,
                megjegyzes, tetelek, kifizetesek);

        ReceiptDto receipt = persistenceService.saveReceiptFromXml(response);
        return ResponseEntity.ok(receipt);
    }

    @GetMapping("/getReceipts")
    public ResponseEntity<List<ReceiptBasicInfoDto>> getReceipts() {
        List<ReceiptBasicInfoDto> receipts = persistenceService.getAllReceipts().stream().map(ReceiptDto::getAlap).toList();
        return ResponseEntity.ok(receipts);
    }

    @GetMapping("/getReceipt/{hivasAzonosito}")
    public ResponseEntity<ReceiptDto> getReceipt(@PathVariable String hivasAzonosito) {
        ReceiptDto receipt = persistenceService.getReceiptByHivasAzonosito(hivasAzonosito);
        return ResponseEntity.ok(receipt);
    }
}
