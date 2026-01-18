package hu.mbalazs.szamlazz.database.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tetel")
public class ReceiptItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String megnevezes;
    private Double mennyiseg;
    private String mennyisegiEgyseg;
    private Double nettoEgysegar;
    private String afakulcs;
    private Double netto;
    private Double afa;
    private Double brutto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nyugta_id", nullable = false)
    @JsonIgnore
    private ReceiptEntity nyugta;

    public void setReceipt(ReceiptEntity nyugta) {
        this.nyugta = nyugta;
    }

}
