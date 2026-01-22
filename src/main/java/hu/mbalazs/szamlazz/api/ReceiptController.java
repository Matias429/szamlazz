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
                createReceiptDto.getPdfDownload(),
                createReceiptDto.getPrefix(),
                createReceiptDto.getPaymentMethod(),
                createReceiptDto.getCurrency(),
                Optional.ofNullable(createReceiptDto.getNote()),
                createReceiptDto.getItemList(),
                Optional.ofNullable(createReceiptDto.getPaymentList())
        );
        try {
            ReceiptDto receipt = persistenceService.saveReceiptFromXml(response);
            return ResponseEntity.ok(receipt);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getReceipts")
    public ResponseEntity<List<ReceiptBasicInfoDto>> getReceipts() {
        List<ReceiptBasicInfoDto> receipts = persistenceService.getAllReceipts().stream()
                .map(r -> new ReceiptBasicInfoDto(r.getDetails().getReceiptNumber(), r.getDetails().getReceiptDate(), r.getDetails().getCurrency(),
                            r.getAmountList().getTotalAmounts().getNet(), r.getAmountList().getTotalAmounts().getGross())).toList();
        return ResponseEntity.ok(receipts);
    }

    @GetMapping("/getReceipt/{callId}")
    public ResponseEntity<ReceiptDto> getReceipt(@PathVariable String callId) {
        ReceiptDto receipt = persistenceService.getReceiptByCallId(callId);
        return ResponseEntity.ok(receipt);
    }
}
