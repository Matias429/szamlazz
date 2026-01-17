package hu.mbalazs.szamlazz.xmlhandling;

import hu.mbalazs.szamlazz.PaymentItem;
import hu.mbalazs.szamlazz.PaymentMethods;
import hu.mbalazs.szamlazz.ReceiptItem;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class ReceiptClient {

    private final WebClient webClient;
    private final XmlWriterService xmlWriterService;

    public ReceiptClient(XmlWriterService xmlWriterService) {
        WebClient.Builder builder = WebClient.builder();
        this.webClient = builder
                .baseUrl("https://www.szamlazz.hu")
                .build();
        this.xmlWriterService = xmlWriterService;
    }

    public String createReceipt() {

        List<ReceiptItem> tetelek = List.of(
                new ReceiptItem("Termék 1", 2.0, "db", 1000.0, "27.0", 2000.0, 540.0, 2540.0),
                new ReceiptItem("Termék 2", 1.0, "db", 500.0, "27.0", 500.0, 135.0, 635.0)
        );

        List<PaymentItem> kifizetesek = List.of(
                new PaymentItem("voucher", 3175.0)
        );

        String xml = xmlWriterService.createReceipt(
                false, "LR47A", PaymentMethods.CASH, "HUF", "ASFSHFSDG348147123",
                java.util.Optional.of("Teszt nyugta megjegyzes"), tetelek, kifizetesek);
        // 2️⃣ Wrap XML as multipart file
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder
                .part("action-szamla_agent_nyugta_create",
                        xml)
                .filename("nyugta.xml")
                .contentType(MediaType.APPLICATION_XML);

        System.out.println("Generated XML:\n" + xml);

        // 3️⃣ Send request
        return webClient.post()
                .uri("/szamla/")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(bodyBuilder.build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
