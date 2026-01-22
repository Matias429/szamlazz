# Számlázz.hu próbafeladat frontend

## Home Page / Nyugták listája (Főoldal)

Ez az oldal a rendszerben rögzített nyugták **áttekintő listáját** jeleníti meg. A főoldal célja, hogy gyors hozzáférést biztosítson a nyugták alapadataihoz, valamint belépési pontként szolgáljon az egyes nyugták részletes megtekintéséhez.

### Fő funkciók

* **Nyugták listázása**
  * A backend szolgáltatásból lekért nyugták megjelenítése táblázatos formában
  * A lista dinamikusan frissül az adatok betöltése után
  * Üres állapot kezelése („Nincsenek nyugták”)

* **Áttekinthető táblázatos nézet**
  * Angular Material `mat-table` használata
  * Megjelenített oszlopok:
  * Nyugtaszám
  * Keltezés
  * Teljes nettó összeg
  * Teljes bruttó összeg
  * Az összegek a megfelelő pénznemben jelennek meg

* **Navigáció és interakció**
  * Teljes sor kattintható a nyugta részleteinek megnyitásához
  * Automatikus navigáció a részletező oldalra (`/details/:callId`)
  * Külön gomb új nyugta létrehozásához

### Technikai megvalósítás

* Angular Standalone Component
* Angular Signals a nyugták listájának kezelésére
* Angular Router az oldalak közötti navigációhoz
* Angular Material (kártyák, táblázatok, ikonok, gombok)

## Details Page / Nyugta részletei oldal

Ez az oldal egy korábban létrehozott nyugta részletes megtekintését biztosítja. A nyugta adatai az URL-ben kapott azonosító (callId) alapján kerülnek lekérésre, és áttekinthető, strukturált formában jelennek meg Angular Material komponensek segítségével.

### Fő funkciók

* **Nyugta adatok betöltése**
  * Dinamikus adatlekérés az útvonal paraméteréből (`callId`)
  * Backend API hívás a nyugta részleteinek lekérésére
  * Betöltési állapot kezelése („Betöltés...”)

* **Alapinformációk megjelenítése**
  * Nyugtaszám
  * Hívásazonosító
  * Keltezés
  * Nyugta típusa
  * Fizetési mód
  * Pénznem
  * Státusz (aktív / sztornózott)
  * Opcionális megjegyzés

* **Összesítések**
  * Nettó, ÁFA és bruttó végösszegek kiemelt megjelenítése
  * Összegek a nyugta pénznemében

* **Tételek részletezése**
  * Táblázatos megjelenítés Angular Material `mat-table` segítségével
  * Részletes oszlopok:
    * Megnevezés
    * Mennyiség és mértékegység
    * Nettó egységár
    * ÁFA kulcs
    * Nettó összeg
    * ÁFA összeg
    * Bruttó összeg
  * Fixált (sticky) fejléc a jobb áttekinthetőség érdekében

* **Kifizetések megjelenítése**
  * Opcionális szekció, csak akkor jelenik meg, ha tartozik kifizetés a nyugtához
  * Fizetőeszköz és összeg megjelenítése táblázatos formában

* **Navigáció**
  * Visszalépés a nyugták listájára
  * Új nyugta létrehozása közvetlenül a részletező oldalról

### Technikai megvalósítás

* Angular Standalone Component
* Angular Signals az állapotkezeléshez
* Angular Router útvonalparaméterek kezelése
* Angular Material (kártyák, listák, táblázatok, ikonok, gombok)

## Create Page / Új nyugta létrehozása oldal

Ez az oldal egy új nyugta létrehozását teszi lehetővé Angular és Angular Material felhasználásával. A felület reaktív űrlapokra épül, és támogatja a tételek, kifizetések, valamint az automatikus nettó–ÁFA–bruttó számítás kezelését.

### Fő funkciók

* **Alapadatok megadása**
  * Előtag
  * Fizetési mód
  * Pénznem (autocomplete támogatással)
  * Opcionális megjegyzés
  * PDF automatikus letöltésének beállítása

* **Tételek kezelése**
  * Több tétel hozzáadása és törlése
  * Mennyiség, egység, nettó egységár és ÁFA kulcs megadása
  * Nettó összeg, ÁFA és bruttó összeg automatikus számítása
  * Bruttó végösszeg valós idejű összesítése

* **Kifizetések kezelése**
  * Több kifizetés rögzítése
  * Kifizetett összeg és fizetőeszköz megadása
  * Hátralék automatikus számítása
  * Validáció: a nyugta csak akkor hozható létre, ha a kifizetések összege megegyezik a bruttó végösszeggel

* **Validáció és hibakezelés**
  * Kötelező mezők és minimum értékek ellenőrzése
  * Felhasználóbarát hibaüzenetek MatSnackBar segítségével
  * A „Létrehozás” gomb automatikusan letiltásra kerül érvénytelen állapot esetén

* **Beküldés és PDF kezelés**
  * Nyugta létrehozása backend szolgáltatáson keresztül
  * Opcionális PDF letöltés base64 formátumból
  * Sikeres létrehozás után automatikus navigáció a részletező oldalra

### Technikai megvalósítás

* Angular Standalone Component
* Reactive Forms (FormGroup, FormArray)
* Angular Material (form mezők, gombok, ikonok, snack bar)
* Angular Router útvonalparaméterek kezelése
* Valós idejű számítások valueChanges eseményekkel
