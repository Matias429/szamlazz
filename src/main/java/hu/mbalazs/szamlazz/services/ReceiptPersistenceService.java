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
        ReceiptDto dto = parser.parse(xml);
        repository.save(mapper.toEntity(dto));
        return dto;
    }

    public List<ReceiptDto> getAllReceipts() {
        return repository.findAll().stream().map(this::entityToDtoMapper).toList();
    }

    public ReceiptDto getReceiptByHivasAzonosito(String hivasAzonosito) {
        ReceiptEntity entity = repository.findByHivasAzonosito(hivasAzonosito);
        return entityToDtoMapper(entity);
    }

    private ReceiptDto entityToDtoMapper(ReceiptEntity entity) {
        ReceiptDto dto = new ReceiptDto();

        ReceiptDetailsDto data = new ReceiptDetailsDto();
        data.setId(entity.getId());
        data.setHivasAzonosito(entity.getHivasAzonosito());
        data.setNyugtaszam(entity.getNyugtaszam());
        data.setTipus(entity.getTipus());
        data.setStornozott(entity.getStornozott());
        data.setKelt(entity.getKelt());
        data.setFizmod(entity.getFizmod());
        data.setPenznem(entity.getPenznem());
        dto.setAlap(data);

        ReceiptItemsDto itemsDto = new ReceiptItemsDto();
        itemsDto.setItems(entity.getTetelek().stream()
                .map(item -> {
                    ReceiptItemsDto.ReceiptItemDto itemDto = new ReceiptItemsDto.ReceiptItemDto();
                    itemDto.setMegnevezes(item.getMegnevezes());
                    itemDto.setMennyiseg(item.getMennyiseg());
                    itemDto.setMennyisegiEgyseg(item.getMennyisegiEgyseg());
                    itemDto.setNettoEgysegar(item.getNettoEgysegar());
                    itemDto.setNetto(item.getNetto());
                    itemDto.setAfa(item.getAfa());
                    itemDto.setBrutto(item.getBrutto());
                    itemDto.setAfakulcs(item.getAfakulcs());
                    return itemDto;
                }).toList()
        );
        dto.setTetelek(itemsDto);

        PaymentItemsDto paymentDto = new PaymentItemsDto();
        paymentDto.setItems(entity.getKifizetesek().stream()
                .map(p -> {
                    PaymentItemsDto.PaymentItemDto pDto = new PaymentItemsDto.PaymentItemDto();
                    pDto.setFizetoeszkoz(p.getFizetoeszkoz());
                    pDto.setOsszeg(p.getOsszeg());
                    return pDto;
                }).toList());
        if (!paymentDto.getItems().isEmpty()) {
            dto.setKifizetesek(paymentDto);
        }

        ReceiptAmountsDto amounts = new ReceiptAmountsDto();
        ReceiptAmountsDto.TotalAmounts totalAmounts = new ReceiptAmountsDto.TotalAmounts();
        totalAmounts.setNetto(entity.getVegosszegNetto());
        totalAmounts.setAfa(entity.getVegosszegAfa());
        totalAmounts.setBrutto(entity.getVegosszegBrutto());
        amounts.setTotalossz(totalAmounts);
        dto.setOsszegek(amounts);

        return dto;
    }
}
