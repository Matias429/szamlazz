# Számlázz.hu Próbafeladat

## Repository struktúra

**Branchek:**

- **main**: `docker-compose.yml` az indításhoz, feladatleírás  
- **frontend**: Angular (v21.1) + Angular Material frontend kód  
- **backend**: Java (v21) + Spring Boot (v4.0.1) backend kód  

---

## Indítás

1. **Docker konténerek indítása**  

   A `main` branchen a projekt gyökérkönyvtárában futtasd:  
   ```
   docker compose up -d
   ```
   Ez letölti a szükséges image-eket Docker Hub-ról (ha még nincsenek), majd elindítja a konténereket.

2. **Frontend elérés**

    A frontend elérhető: https://localhost:8443

    Mivel self-signed tanúsítványt használ, a böngésző figyelmeztethet a Your connection isn't private üzenettel. Itt tovább kell lépni a localhost felé.

3. **Tesztadatok**

    Induláskor az adatbázisban automatikusan 2 teszt nyugta lesz elérhető.

4. **Fontos beállítás**

    A konténerek indítása előtt a docker-compose.yml fájlban állítsd be a környezeti változót:

    ```yaml
    SZAMLAZZ_AGENT_ID=<szamlaagentkulcs>
    ```
    Enélkül új nyugtát nem lehet létrehozni.

## Frontend

**Fő oldalak és funkciók:**

| URL | Funkció |
|-----|---------|
| `/home` | Kezdőoldal, az összes nyugta listázása. Nyugtára kattintva a `details` oldalra irányít. `Új nyugta` gomb a `create` oldalra vezet. |
| `/details/{callId}` | Egy nyugta részletei, pl. Hívásazonosító, Típus, Fizetési mód, Pénznem, Státusz, Megjegyzés, Tételek, Kifizetések. `Nyugták listája` gomb a `home` oldalra, `Új nyugta` gomb a `create` oldalra visz. |
| `/create` | Új nyugta létrehozása. Kötelező mezők: **Előtag**, **Fizetési mód**, **Pénznem**, valamint legalább 1 tétel a Tételek listában (**Megnevezés**, **Mennyiség**, **Egység**, **Nettó egységár**, **ÁFA kulcs**). A rendszer automatikusan kiszámolja a Nettó Összeg, ÁFA Összeg és Bruttó Összeg mezőket. A Tételek és Kifizetések összegeinek egyeznie kell a beküldéshez. `Nyugták listája` gomb a `home` oldalra visz. |

---

## Backend

- **Adatbázis:** in-memory, így az alkalmazás újraindításakor a manuálisan létrehozott nyugták törlődnek.
- **Endpointok:**

| Endpoint | Funkció |
|----------|---------|
| `/createReceipt` | Új nyugta létrehozása az adatbázisban |
| `/getReceipts` | Az összes nyugta lekérése (összegzett adatok) |
| `/getReceipt/{callId}` | Egy adott nyugta részletes lekérése |

**Megjegyzés:** biztonsági okokból az endpointokat csak a frontenden keresztül lehet meghívni.

---

## Működés röviden

### Nyugta létrehozása

1. Felhasználó kitölti a webes űrlapot a szükséges adatokkal.  
2. A frontend meghívja a backend `/createReceipt` endpointját.  
3. A backend generál egy XML-t, amelyet a Számlázz.hu nyugta-generáló API-ja felé továbbít.  
   - A `SZAMLAZZ_AGENT_ID` a docker-compose környezeti változóban van megadva.  
   - A hívásazonosítót a backend generálja.  
4. Ha minden rendben, a backend a visszakapott XML-ből létrehozza a nyugta entity-t az adatbázisban, valamint az adatokat visszaadja a frontendnek.  
5. Hiba esetén a backend továbbítja a hibaüzenetet a frontendnek, amely felugró ablakban jelzi azt.

### Nyugta lekérdezése

- **Home oldal:**  
  A frontend meghívja a `/getReceipts` endpointot. Visszakapott lista tartalmazza:  
  - Nyugtaszám  
  - Keltezés  
  - Teljes Nettó összeg  
  - Teljes Bruttó összeg
  - Pénznem (az összegek megjelenítéséhez)
  - Hívásazonosító (nincs megjelenítve)

- **Details oldal:**  
  A frontend meghívja a `/getReceipt/{callId}` endpointot. A visszakapott nyugta minden részlete megjelenik, a fentiekhez képest extra:  
  - Hívásazonosító, Típus, Fizetési mód, Pénznem, Státusz, Megjegyzés (ha nincs, "Nincs megjegyzés")  
  - Tételek  
  - Kifizetések (ha vannak)
