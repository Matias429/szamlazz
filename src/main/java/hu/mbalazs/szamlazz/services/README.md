# Services Package

A `services` package az alkalmazás **üzleti logikáját, adattranszformációját és külső integrációit** kezeli.  
Itt történik az **XML generálás**, **XML parsolás**, **DTO ↔ Entity átalakítás**, valamint a **külső Számlázz.hu API kommunikáció**.

---

## Főbb servicek és szerepük

### ReceiptMapperService
- DTO-kat alakít át **perzisztálható JPA entitásokká**.
- Fő funkció:
    - `toEntity(ReceiptDto dto) : ReceiptEntity`
        - Átalakítja a `ReceiptDto` objektumot `ReceiptEntity`-vé.
        - Hozzáadja a nyugta tételeit (`ReceiptItemEntity`) és kifizetéseit (`PaymentItemEntity`) az entitáshoz.

---

### ReceiptPersistenceService
- Nyugták **mentéséért és lekéréséért** felel a lokális adatbázisból.
- Fő funkciók:
    - `saveReceiptFromXml(String xml) : ReceiptDto` – XML stringből DTO-t készít, majd menti az entitást.
    - `getAllReceipts() : List<ReceiptDto>` – az összes nyugta lekérése DTO formátumban.
    - `getReceiptByCallId(String callId) : ReceiptDto` – egy nyugta lekérése hívásazonosító alapján.
- **Automatikus mintadat betöltés** a `@PostConstruct` segítségével, ha az adatbázis üres.
- Használja a `ReceiptMapperService`-t DTO → Entity konverzióhoz, és az `XmlParserService`-t XML feldolgozáshoz.

---

### ReceiptWebClientService
- A Számlázz.hu **külső API-jával** kommunikál.
- Fő funkció:
    - `createReceipt(...) : String` – nyugta létrehozása XML POST kéréssel.
    - Használja az `XmlGeneratorService`-t az XML payload előállításához.
- Alap URL: `https://www.szamlazz.hu`
- WebClient használatával **HTTP POST kérést** küld a Számlázz.hu szerverre.

---

### XmlGeneratorService
- DTO-kból **Számlázz.hu kompatibilis XML-t** generál.
- Fő funkció:
    - `parseDataToXml(...) : String`
        - Létrehozza a teljes nyugta XML-t, beleértve:
            - Fejléc (callId, prefix, fizetési mód, pénznem, megjegyzés)
            - Tételek listája
            - Kifizetések listája (ha van)
            - Beállítások (pl. PDF letöltés)
- Használja a DOM és Transformer API-t XML generálásra.

---

### XmlParserService
- A Számlázz.hu által visszaadott XML-eket **DTO-kká alakítja**.
- Fő funkció:
    - `parse(String xml) : ReceiptDto`
        - JAXB segítségével deszerializálja a választ (`ResponseDto`).
        - Ellenőrzi a `successful` mezőt, és hibás válasz esetén `IllegalStateException`-t dob.
        - Beállítja a PDF linket a `ReceiptDetailsDto`-ban, ha elérhető.

---

### Technikai jellemzők
- **Spring Services** annotációval jelölt Servicek (`@Service`).
- **Tranzakció kezelés** a `ReceiptPersistenceService`-ben (`@Transactional`).
- DTO ↔ Entity mapping és XML feldolgozás elkülönítve, így a logika moduláris és tesztelhető.
- Teljes integráció a **frontend, JPA repository és külső Számlázz.hu API** között.