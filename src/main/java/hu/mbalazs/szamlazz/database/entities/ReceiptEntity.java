package hu.mbalazs.szamlazz.database.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "nyugta")
public class ReceiptEntity {

    @Id
    private Long id;
    private String hivasAzonosito;
    private String nyugtaszam;
    private String tipus;
    private Boolean stornozott;
    private String kelt;
    private String fizmod;
    private String penznem;
    private Double vegosszegBrutto;
    private Double vegosszegNetto;
    private Double vegosszegAfa;

    @OneToMany(mappedBy = "nyugta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReceiptItemEntity> tetelek = new ArrayList<>();

    @OneToMany(mappedBy = "nyugta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentItemEntity> kifizetesek = new ArrayList<>();

    public void addReceiptItem(ReceiptItemEntity item) {
        tetelek.add(item);
        item.setReceipt(this);
    }

    public void addPaymentItem(PaymentItemEntity item) {
        kifizetesek.add(item);
        item.setReceipt(this);
    }
}
