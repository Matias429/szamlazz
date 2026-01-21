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

        ReceiptDetailsDto data = new ReceiptDetailsDto();
        data.setId(entity.getId());
        data.setCallId(entity.getCallId());
        data.setReceiptNumber(entity.getReceiptNumber());
        data.setReceiptType(entity.getReceiptType());
        data.setIsCancelled(entity.getIsCancelled());
        data.setReceiptDate(entity.getReceiptDate());
        data.setPaymentMethod(entity.getPaymentMethod());
        data.setCurrency(entity.getCurrency());
        dto.setDetails(data);

        ReceiptItemsDto itemsDto = new ReceiptItemsDto();
        itemsDto.setItemList(entity.getItemList().stream()
                .map(item -> {
                    ReceiptItemsDto.ReceiptItemDto itemDto = new ReceiptItemsDto.ReceiptItemDto();
                    itemDto.setName(item.getName());
                    itemDto.setAmount(item.getAmount());
                    itemDto.setUnitOfMeasure(item.getUnitOfMeasure());
                    itemDto.setNetUnitPrice(item.getNetUnitPrice());
                    itemDto.setNet(item.getNet());
                    itemDto.setVat(item.getVat());
                    itemDto.setGross(item.getGross());
                    itemDto.setVatRate(item.getVatRate());
                    return itemDto;
                }).toList()
        );
        dto.setItemList(itemsDto);

        PaymentItemsDto paymentDto = new PaymentItemsDto();
        paymentDto.setPaymentList(entity.getPaymentList().stream()
                .map(p -> {
                    PaymentItemsDto.PaymentItemDto pDto = new PaymentItemsDto.PaymentItemDto();
                    pDto.setMeansOfPayment(p.getMeansOfPayment());
                    pDto.setAmount(p.getAmount());
                    return pDto;
                }).toList());
        if (!paymentDto.getPaymentList().isEmpty()) {
            dto.setPaymentList(paymentDto);
        }

        ReceiptAmountsDto amounts = new ReceiptAmountsDto();
        ReceiptAmountsDto.TotalAmounts totalAmounts = new ReceiptAmountsDto.TotalAmounts();
        totalAmounts.setNet(entity.getTotalNet());
        totalAmounts.setVat(entity.getTotalVat());
        totalAmounts.setGross(entity.getTotalGross());
        amounts.setTotalAmounts(totalAmounts);
        dto.setAmountList(amounts);

        return dto;
    }
}
