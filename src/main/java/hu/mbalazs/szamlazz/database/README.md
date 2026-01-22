# Database Package

## Entities Package (Perzisztens adatmodell)

Ez a package tartalmazza az alkalmazás **adatbázis-perzisztenciáért felelős JPA entitásait**, amelyek a nyugták, azok tételei és a kapcsolódó kifizetések struktúráját modellezik. Az entitások a backend üzleti logikájának alapját képezik, és közvetlenül megfeleltethetők az adatbázis tábláknak.


### ReceiptEntity

A `ReceiptEntity` a rendszer **központi entitása**, amely egy teljes nyugtát reprezentál.

**Főbb jellemzők:**
- Egyedi azonosító (`id`) és külső hivatkozási azonosító (`callId`)
- Nyugta alapadatai:
    - Nyugtaszám
    - Típus
    - Státusz (aktív / sztornózott)
    - Keltezés
    - Fizetési mód
    - Pénznem
- Összesített pénzügyi adatok:
    - Nettó összeg
    - ÁFA összeg
    - Bruttó összeg
- Opcionális megjegyzés

**Kapcsolatok:**
- `@OneToMany` kapcsolat a nyugtához tartozó:
    - `ReceiptItemEntity` (tételek)
    - `PaymentItemEntity` (kifizetések)
- Cascade és orphan removal biztosítja az adatintegritást

**Segédmetódusok:**
- `addReceiptItem(...)`
- `addPaymentItem(...)`  
  Ezek a metódusok gondoskodnak a kétoldalú kapcsolatok helyes beállításáról.

---

### ReceiptItemEntity

A `ReceiptItemEntity` egy nyugtán szereplő **egyedi tételt** reprezentál.

**Főbb jellemzők:**
- Megnevezés
- Mennyiség és mértékegység
- Nettó egységár
- ÁFA kulcs
- Számított értékek:
    - Nettó összeg
    - ÁFA összeg
    - Bruttó összeg

**Kapcsolatok:**
- `@ManyToOne` kapcsolat a `ReceiptEntity` felé
- Lazy betöltés a teljesítmény optimalizálása érdekében
- `@JsonIgnore` az esetleges ciklikus JSON-szerializáció elkerülésére

---

### PaymentItemEntity

A `PaymentItemEntity` egy nyugtához tartozó **kifizetési tételt** ír le.

**Főbb jellemzők:**
- Fizetőeszköz megnevezése
- Kifizetett összeg

**Kapcsolatok:**
- `@ManyToOne` kapcsolat a `ReceiptEntity` felé
- A kapcsolat kötelező (`nullable = false`)
- Lazy betöltés és JSON-szerializáció elleni védelem

---

### Technikai jellemzők

- **JPA / Hibernate** annotációk az ORM megvalósításhoz
- **Lombok** az egyszerűbb getter/setter és konstruktor kezeléshez
- **CascadeType.ALL** és **orphanRemoval = true** a konzisztens adatkezelésért
- Az entitások megfeleltethetők az adatbázis alábbi tábláinak:
    - `receipt`
    - `item`
    - `payment`

## Repositories Package

Ez a package tartalmazza az alkalmazás **adatbázis-hozzáférési rétegét**, amely a perzisztált nyugták lekérdezéséért és kezeléséért felel. A repository-k a Spring Data JPA által biztosított absztrakcióra épülnek, így minimális boilerplate kóddal biztosítanak típusbiztos adatbázis-műveleteket.

---

### ReceiptRepository

A `ReceiptRepository` a `ReceiptEntity` perzisztenciáját kezeli, és hozzáférést biztosít a nyugták adatbázisban tárolt adataihoz.

**Alapfunkciók:**
- Teljes CRUD műveletek a `JpaRepository` öröklésén keresztül
- Automatikusan generált SQL lekérdezések a metódusnevek alapján

**Elérhető metódusok:**

- `findAll()`
    - Az összes nyugta lekérdezése az adatbázisból
    - Lista nézetek kiszolgálására használatos

- `findByCallId(String callId)`
    - Egy adott nyugta lekérdezése az egyedi `callId` alapján
    - Részletező nézetekhez és üzleti logikához használható

---

### Technikai jellemzők

- **Spring Data JPA**
- **JpaRepository** interfész használata
- Metódusnév-alapú query generálás
