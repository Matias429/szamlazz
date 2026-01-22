package hu.mbalazs.szamlazz.database.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "item")
public class ReceiptItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double amount;
    private String unitOfMeasure;
    private Double netUnitPrice;
    private String vatRate;
    private Double net;
    private Double vat;
    private Double gross;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receipt_id", nullable = false)
    @JsonIgnore
    private ReceiptEntity receipt;

    public void setReceipt(ReceiptEntity receipt) {
        this.receipt = receipt;
    }

    public ReceiptItemEntity(String name, Double amount, String unitOfMeasure, Double netUnitPrice,
                             String vatRate, Double net, Double vat, Double gross) {
        this.name = name;
        this.amount = amount;
        this.unitOfMeasure = unitOfMeasure;
        this.netUnitPrice = netUnitPrice;
        this.vatRate = vatRate;
        this.net = net;
        this.vat = vat;
        this.gross = gross;
    }

}
