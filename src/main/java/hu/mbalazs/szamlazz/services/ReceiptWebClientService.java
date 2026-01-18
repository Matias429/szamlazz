package hu.mbalazs.szamlazz.services;

import hu.mbalazs.szamlazz.dtos.PaymentItemsDto;
import hu.mbalazs.szamlazz.dtos.ReceiptItemsDto;
import hu.mbalazs.szamlazz.helpers.PaymentMethods;
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

    public String createReceipt(String hivasazonosito, Boolean pdfLetoltes, String elotag, PaymentMethods fizmod, String penznem, Optional<String> megjegyzes,
                                List<ReceiptItemsDto.ReceiptItemDto> tetelek, Optional<List<PaymentItemsDto.PaymentItemDto>> kifizetesek) {


        String xml = xmlGeneratorService.parseDataToXml(hivasazonosito, pdfLetoltes, elotag, fizmod, penznem, megjegyzes, tetelek, kifizetesek);
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder
                .part("action-szamla_agent_nyugta_create", xml)
                .filename("nyugta.xml")
                .contentType(MediaType.APPLICATION_XML);

        System.out.println("Generated XML:\n" + xml);

        return webClient.post()
                .uri("/szamla/")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(bodyBuilder.build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
