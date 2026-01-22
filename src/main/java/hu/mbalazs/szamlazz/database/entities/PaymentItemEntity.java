package hu.mbalazs.szamlazz.database.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "payment")
public class PaymentItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String meansOfPayment;
    private Double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receipt_id", nullable = false)
    @JsonIgnore
    private ReceiptEntity receipt;

    public void setReceipt(ReceiptEntity receipt) {
        this.receipt = receipt;
    }

    public PaymentItemEntity(String meansOfPayment, Double amount) {
        this.meansOfPayment = meansOfPayment;
        this.amount = amount;
    }

}
