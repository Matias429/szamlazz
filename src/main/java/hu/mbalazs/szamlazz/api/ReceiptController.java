package hu.mbalazs.szamlazz.api;

import hu.mbalazs.szamlazz.dtos.*;
import hu.mbalazs.szamlazz.services.ReceiptPersistenceService;
import hu.mbalazs.szamlazz.services.ReceiptWebClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://frontend:8443")
public class ReceiptController {

    private final ReceiptWebClientService client;
    private final ReceiptPersistenceService persistenceService;

    public ReceiptController(ReceiptWebClientService client, ReceiptPersistenceService persistenceService) {
        this.client = client;
        this.persistenceService = persistenceService;
    }

    @PostMapping(value = "/createReceipt")
    public ResponseEntity<?> createReceipt(@RequestBody CreateReceiptDto createReceiptDto) {

        String response = client.createReceipt(
                "CID-" + System.currentTimeMillis(),
                createReceiptDto.getPdfLetoltes(),
                createReceiptDto.getElotag(),
                createReceiptDto.getFizmod(),
                createReceiptDto.getPenznem(),
                Optional.ofNullable(createReceiptDto.getMegjegyzes()),
                createReceiptDto.getTetelek(),
                Optional.ofNullable(createReceiptDto.getKifizetesek())
        );
        try {
            ReceiptDto receipt = persistenceService.saveReceiptFromXml(response);
            return ResponseEntity.ok(receipt);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getReceipts")
    public ResponseEntity<List<ReceiptDto>> getReceipts() {
        List<ReceiptDto> receipts = persistenceService.getAllReceipts();
        return ResponseEntity.ok(receipts);
    }

    @GetMapping("/getReceipt/{hivasAzonosito}")
    public ResponseEntity<ReceiptDto> getReceipt(@PathVariable String hivasAzonosito) {
        ReceiptDto receipt = persistenceService.getReceiptByHivasAzonosito(hivasAzonosito);
        return ResponseEntity.ok(receipt);
    }
}
