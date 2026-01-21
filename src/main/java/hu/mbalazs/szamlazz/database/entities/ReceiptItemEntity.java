package hu.mbalazs.szamlazz.database.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

}
