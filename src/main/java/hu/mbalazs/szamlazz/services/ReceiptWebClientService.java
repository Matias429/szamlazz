package hu.mbalazs.szamlazz.services;

import hu.mbalazs.szamlazz.dtos.PaymentItemsDto;
import hu.mbalazs.szamlazz.dtos.ReceiptItemsDto;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Service
public class ReceiptWebClientService {

    private final WebClient webClient;
    private final XmlGeneratorService xmlGeneratorService;

    public ReceiptWebClientService(XmlGeneratorService xmlGeneratorService) {
        WebClient.Builder builder = WebClient.builder();
        this.webClient = builder
                .baseUrl("https://www.szamlazz.hu")
                .build();
        this.xmlGeneratorService = xmlGeneratorService;
    }

    public String createReceipt(String callId, Boolean pdfDownload, String prefix, String paymentMethod, String currency, Optional<String> note,
                                List<ReceiptItemsDto.ReceiptItemDto> itemList, Optional<List<PaymentItemsDto.PaymentItemDto>> paymentList) {

        String xml = xmlGeneratorService.parseDataToXml(callId, pdfDownload, prefix, paymentMethod, currency, note, itemList, paymentList);
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder
                .part("action-szamla_agent_nyugta_create", xml)
                .filename("nyugta.xml")
                .contentType(MediaType.APPLICATION_XML);

        System.out.println(xml);

        return webClient.post()
                .uri("/szamla/")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(bodyBuilder.build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
