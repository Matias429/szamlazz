# API Package

## ReceiptController

A `ReceiptController` felelős a nyugták létrehozásához, lekérdezéséhez és részleteinek kiszolgálásához kapcsolódó REST API végpontokért. A controller összeköti a frontend alkalmazást a külső számlázó/nyugta szolgáltatással, valamint a perzisztens adattárolással.

### Általános jellemzők

- **REST alapú API**
- **CORS támogatás** a frontend alkalmazás számára
- Külső nyugta/számlázó szolgáltatás integráció
- XML alapú válasz feldolgozása és perzisztálása
- DTO-alapú adatcsere a frontend felé

---

### Végpontok

#### `POST /createReceipt`

Új nyugta létrehozása a megadott adatok alapján.

**Leírás:**
- A frontend által küldött nyugtaadatokat továbbítja a külső számlázó szolgáltatás felé
- Egyedi `callId` kerül generálásra az aktuális időbélyeg alapján
- A külső szolgáltatás XML válasza feldolgozásra és eltárolásra kerül
- Sikeres létrehozás esetén a teljes nyugta DTO kerül visszaadásra

**Request body:**
- `CreateReceiptDto`
    - PDF letöltés engedélyezése
    - Előtag
    - Fizetési mód
    - Pénznem
    - Opcionális megjegyzés
    - Tételek listája
    - Opcionális kifizetések listája

**Response:**
- `200 OK` – létrehozott nyugta (`ReceiptDto`)
- `400 Bad Request` – hibás vagy érvénytelen adatok esetén

---

#### `GET /getReceipts`

Az összes rögzített nyugta lekérdezése, **lista nézethez optimalizált** formában.

**Leírás:**
- A perzisztált nyugták alapadatait adja vissza
- Csak a listázáshoz szükséges mezőket tartalmazza

**Response:**
- `200 OK`
- `List<ReceiptBasicInfoDto>`
    - Nyugtaszám
    - Keltezés
    - Pénznem
    - Teljes nettó összeg
    - Teljes bruttó összeg

---

#### `GET /getReceipt/{callId}`

Egy adott nyugta részletes adatainak lekérdezése.

**Paraméterek:**
- `callId` – a nyugta egyedi azonosítója

**Leírás:**
- A megadott `callId` alapján visszaadja a teljes nyugtaadat-struktúrát
- Részletező nézetek kiszolgálására szolgál

**Response:**
- `200 OK`
- `ReceiptDto`

---

### Technikai megvalósítás

- **Spring Boot REST Controller**
- **DTO-alapú adatmodellezés**
- **ReceiptWebClientService**
    - Külső nyugta/számlázó szolgáltatás hívása
- **ReceiptPersistenceService**
    - XML feldolgozás és adatbázisba mentés