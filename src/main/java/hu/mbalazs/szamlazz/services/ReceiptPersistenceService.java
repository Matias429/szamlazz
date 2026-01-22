package hu.mbalazs.szamlazz.services;

import hu.mbalazs.szamlazz.database.entities.ReceiptEntity;
import hu.mbalazs.szamlazz.database.repositories.ReceiptRepository;
import hu.mbalazs.szamlazz.dtos.*;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class ReceiptPersistenceService {

    private final ReceiptRepository repository;
    private final ReceiptMapperService mapper;
    private final XmlParserService parser;
    private final ResourceLoader resourceLoader;

    public ReceiptPersistenceService(ReceiptRepository repository, ReceiptMapperService mapper, XmlParserService parser, ResourceLoader resourceLoader) {
        this.repository = repository;
        this.mapper = mapper;
        this.parser = parser;
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    private void initSampleData() {
        if (repository.count() == 0) {
            String[] xmlFiles = {"sample-receipts/sample-receipt1.xml", "sample-receipts/sample-receipt2.xml"};
            for (String filePath: xmlFiles) {
                try {
                    String xml = new String(resourceLoader.getResource("classpath:" + filePath).getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                    saveReceiptFromXml(xml);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Transactional
    public ReceiptDto saveReceiptFromXml(String xml) {
        try {
            ReceiptDto dto = parser.parse(xml);
            repository.save(mapper.toEntity(dto));
            return dto;
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public List<ReceiptDto> getAllReceipts() {
        return repository.findAll().stream().map(this::entityToDtoMapper).toList();
    }

    public ReceiptDto getReceiptByCallId(String callId) {
        ReceiptEntity entity = repository.findByCallId(callId);
        return entityToDtoMapper(entity);
    }

    private ReceiptDto entityToDtoMapper(ReceiptEntity entity) {
        ReceiptDto dto = new ReceiptDto();

        ReceiptDetailsDto receiptDetails = new ReceiptDetailsDto(entity.getId(), entity.getCallId(), entity.getReceiptNumber(),
                entity.getReceiptType(), entity.getIsCancelled(), entity.getReceiptDate(),
                entity.getPaymentMethod(), entity.getCurrency());

        if (entity.getNote() != null) {
            receiptDetails.setNote(entity.getNote());
        }

        dto.setDetails(receiptDetails);

        ReceiptItemsDto itemsDto = new ReceiptItemsDto();
        itemsDto.setItemList(entity.getItemList().stream()
                .map(item -> new ReceiptItemsDto.ReceiptItemDto(item.getName(), item.getAmount(), item.getUnitOfMeasure(),
                        item.getNetUnitPrice(), item.getVatRate(),
                        item.getNet(), item.getVat(), item.getGross())).toList()
        );
        dto.setItemList(itemsDto);

        PaymentItemsDto paymentDto = new PaymentItemsDto();
        paymentDto.setPaymentList(entity.getPaymentList().stream()
                .map(p -> new PaymentItemsDto.PaymentItemDto(p.getMeansOfPayment(), p.getAmount())).toList());
        if (!paymentDto.getPaymentList().isEmpty()) {
            dto.setPaymentList(paymentDto);
        }

        ReceiptAmountsDto receiptAmounts = new ReceiptAmountsDto();
        receiptAmounts.setTotalAmounts(new ReceiptAmountsDto.TotalAmounts(entity.getTotalNet(), entity.getTotalVat(), entity.getTotalGross()));
        dto.setAmountList(receiptAmounts);

        return dto;
    }
}
