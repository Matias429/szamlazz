package hu.mbalazs.szamlazz.api;

import hu.mbalazs.szamlazz.xmlhandling.ReceiptClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReceiptController {

    private final ReceiptClient client;

    public ReceiptController(ReceiptClient client) {
        this.client = client;
    }

    @PostMapping(value = "/createReceipt")
    public ResponseEntity<String> createReceipt() {
        String response = client.createReceipt();
        return ResponseEntity.ok(response);
    }
}
