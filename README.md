# Számlázz.hu próbafeladat backend

Ez a backend alkalmazás a **nyugta kezelő rendszer** része, amely a Számlázz.hu API-jával integrálódik.  
A rendszer lehetővé teszi nyugták létrehozását, tárolását, lekérdezését és feldolgozását.

---

## Főbb funkciók

- Nyugták **létrehozása** a Számlázz.hu API-n keresztül.
- Nyugták **mentése** helyi adatbázisba (JPA/H2 vagy más relációs DB).
- Nyugták **lekérdezése** hívásazonosító alapján.
- Automatikus mintanyugta betöltés az adatbázisba induláskor, ha nincs korábban mentett nyugta.
- DTO ↔ Entity **térképezés** és XML feldolgozás (JAXB).

---

## Projekt struktúra és package-ek

A backend modulárisan van felépítve, package-ekre bontva:

1. **`api`** – REST controller-ek
    - Nyugták létrehozása és lekérése.
    - Részletes dokumentáció: `api/README.md`

2. **`database.entities`** – JPA entitások
    - `ReceiptEntity`, `ReceiptItemEntity`, `PaymentItemEntity`
    - Részletes dokumentáció: `database/README.md`

3. **`database.repositories`** – Spring Data JPA repository
    - `ReceiptRepository`
    - Részletes dokumentáció: `database/README.md`

4. **`dtos`** – Adatátviteli objektumok (DTO-k)
    - Nyugták, tételek, kifizetések és válasz DTO-k.
    - Részletes dokumentáció: `dtos/README.md`

5. **`services`** – Üzleti logika, XML kezelés, DTO ↔ Entity mapping, külső API kommunikáció
    - `ReceiptMapperService`, `ReceiptPersistenceService`, `ReceiptWebClientService`, `XmlGeneratorService`, `XmlParserService`
    - Részletes dokumentáció: `services/README.md`

---

## Technológiák

- **Java:** 21
- **Spring Boot:** 4.0.1
- **Spring Data JPA**
- **WebClient** a Számlázz.hu API kommunikációhoz
- **JAXB** az XML feldolgozáshoz
- **H2 / más relációs adatbázis** a nyugták tárolásához