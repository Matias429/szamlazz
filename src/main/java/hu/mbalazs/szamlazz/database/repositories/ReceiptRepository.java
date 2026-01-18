package hu.mbalazs.szamlazz.database.repositories;

import hu.mbalazs.szamlazz.database.entities.ReceiptEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReceiptRepository extends JpaRepository<ReceiptEntity, Long> {

    List<ReceiptEntity> findAll();

    ReceiptEntity findByHivasAzonosito(String hivasAzonosito);
}
