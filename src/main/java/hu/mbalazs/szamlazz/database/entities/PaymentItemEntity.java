package hu.mbalazs.szamlazz.database.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "kifizetes")
public class PaymentItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fizetoeszkoz;
    private Double osszeg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nyugta_id", nullable = false)
    @JsonIgnore
    private ReceiptEntity nyugta;

    public void setReceipt(ReceiptEntity nyugta) {
        this.nyugta = nyugta;
    }

}
