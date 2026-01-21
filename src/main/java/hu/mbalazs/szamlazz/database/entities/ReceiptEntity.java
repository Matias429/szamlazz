package hu.mbalazs.szamlazz.database.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "receipt")
public class ReceiptEntity {

    @Id
    private Long id;
    private String callId;
    private String receiptNumber;
    private String receiptType;
    private Boolean isCancelled;
    private String receiptDate;
    private String paymentMethod;
    private String currency;
    private Double totalGross;
    private Double totalNet;
    private Double totalVat;

    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReceiptItemEntity> itemList = new ArrayList<>();

    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentItemEntity> paymentList = new ArrayList<>();

    public void addReceiptItem(ReceiptItemEntity item) {
        itemList.add(item);
        item.setReceipt(this);
    }

    public void addPaymentItem(PaymentItemEntity item) {
        paymentList.add(item);
        item.setReceipt(this);
    }
}
