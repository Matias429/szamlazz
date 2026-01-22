# DTOs Package

A `dtos` package az alkalmazás **adatátviteli objektumait (DTO-kat)** tartalmazza, amelyek a frontend és a backend közötti kommunikációt, valamint a külső számlázó/nyugta szolgáltatás integrációját szolgálják. Ezek a DTO-k gyakran a JPA entitásokhoz hasonló struktúrával rendelkeznek, de kizárólag az adatcserére szolgálnak, nem tartalmaznak perzisztenciális logikát.

---

## Főbb DTO-k és szerepük

### CreateReceiptDto
- A frontend által küldött adatok fogadására szolgál új nyugta létrehozásakor.
- Mezők:
    - `pdfDownload` – PDF generálás engedélyezése
    - `prefix` – nyugta előtag
    - `paymentMethod` – fizetési mód
    - `currency` – pénznem
    - `note` – opcionális megjegyzés
    - `itemList` – tételek listája (`ReceiptItemsDto.ReceiptItemDto`)
    - `paymentList` – kifizetések listája (`PaymentItemsDto.PaymentItemDto`)

---

### ReceiptItemDto / ReceiptItemsDto
- Egy nyugta tételét reprezentálja.
- Mezők:
    - `name` – tétel megnevezése
    - `amount` – mennyiség
    - `unitOfMeasure` – mértékegység
    - `netUnitPrice` – nettó egységár
    - `vatRate` – ÁFA kulcs
    - `net` – nettó összeg
    - `vat` – ÁFA összeg
    - `gross` – bruttó összeg

---

### PaymentItemDto / PaymentItemsDto
- Nyugtához tartozó kifizetést reprezentál.
- Mezők:
    - `meansOfPayment` – fizetőeszköz
    - `amount` – összeg

---

### ReceiptAmountsDto
- Nyugta összesített pénzügyi adatait tartalmazza.
- Mezők:
    - `net` – teljes nettó összeg
    - `vat` – teljes ÁFA
    - `gross` – teljes bruttó összeg

---

### ReceiptDetailsDto
- Nyugta alapadatai részletesen.
- Mezők:
    - `id`, `callId`, `receiptNumber`, `receiptType`
    - `isCancelled` – sztornózott státusz
    - `receiptDate`, `paymentMethod`, `currency`
    - `receiptPdf` – opcionális PDF link
    - `note` – megjegyzés

---

### ReceiptBasicInfoDto
- Nyugta **lista nézethez** optimalizált, összefoglaló adatok.
- Mezők:
    - `receiptNumber`, `receiptDate`, `currency`, `net`, `gross`

---

### ReceiptDto
- A teljes nyugta adatstruktúrája XML válaszokhoz és frontend részletező nézethez.
- Tartalmazza:
    - `details` – ReceiptDetailsDto
    - `itemList` – ReceiptItemsDto
    - `paymentList` – PaymentItemsDto
    - `amountList` – ReceiptAmountsDto

---

### ResponseDto
- Külső szolgáltatás válaszának DTO-ja.
- Mezők:
    - `successful` – sikeres művelet
    - `receiptPdf` – PDF adatok
    - `receipt` – ReceiptDto
    - `errorCode`, `errorMessage` – hibainformációk

---

### Technikai jellemzők

- **Lombok** a getter/setter és konstruktorok automatikus generálásához
- **JAXB annotációk** (`@XmlElement`, `@XmlAccessorType`, `@XmlRootElement`) a XML alapú kommunikációhoz
- DTO-k kizárólag **adatátviteli célt szolgálnak**, nem tartalmaznak perzisztens logikát
- Teljes integráció a frontend űrlapokkal és a backend szolgáltatásokkal
- Segít a **strukturált, típusbiztos adatátvitel** megvalósításában