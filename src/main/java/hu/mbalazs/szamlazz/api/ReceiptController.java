package hu.mbalazs.szamlazz.api;

import hu.mbalazs.szamlazz.dtos.*;
import hu.mbalazs.szamlazz.services.ReceiptPersistenceService;
import hu.mbalazs.szamlazz.services.ReceiptWebClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(value = "/createReceipt")
    public ResponseEntity<ReceiptDto> createReceipt(@RequestBody CreateReceiptDto createReceiptDto) {

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

        ReceiptDto receipt = persistenceService.saveReceiptFromXml(response);
        return ResponseEntity.ok(receipt);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/getReceipts")
    public ResponseEntity<List<ReceiptDto>> getReceipts() {
        List<ReceiptDto> receipts = persistenceService.getAllReceipts();
        return ResponseEntity.ok(receipts);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/getReceipt/{hivasAzonosito}")
    public ResponseEntity<ReceiptDto> getReceipt(@PathVariable String hivasAzonosito) {
        ReceiptDto receipt = persistenceService.getReceiptByHivasAzonosito(hivasAzonosito);
        return ResponseEntity.ok(receipt);
    }
}
