## **Próbafeladat: Nyugta kezelő alkalmazás**

### **Cél**

Készíts egy olyan webalkalmazást, amellyel:

* új nyugtákat lehet **létrehozni** (a Számlázz.hu Nyugta létrehozás API-n keresztül),  
* a létrejött nyugták adatai a saját adatbázisban tárolódnak,  
* a nyugták **listázhatók** és **részleteik megtekinthetők** már a saját DB-ből.

---

## **Technikai elvárások**

### **Stack**

* **Frontend:** Angular \+ Angular Material  
* **Backend:** Spring Boot \+ relációs adatbázis (lehet in-memory)  
* **Indítás:** docker compose up paranccsal  
* **Forráskód:** saját GitHub repóban

---

## **Funkcionális követelmények**

### **1\) Nyugta létrehozás**

**Létrehozás menete:**

1. A nyugta létrehozáshoz a dokumentációt itt találod: [https://docs.szamlazz.hu/hu/agent/category/generating-a-receipt](https://docs.szamlazz.hu/hu/agent/category/generating-a-receipt)

2. Az egyszerűség kedvéért csak a minimálisan kötelező adatokat küld be.

3. Kapcsolódási adatok  
   * POST URL: https://www.szamlazz.hu/szamla/  
   * fájl mező neve: action-szamla\_agent\_nyugta\_create    
   * Agent kulcs: *97039xbwy2gws4iv7yn4xk8cniuird56tyamat6gy3* (ez amúgy a demo fiók egyik API kulcsa, de használhatsz sajátot is tetszőleges [tesztfiókból](https://tudastar.szamlazz.hu/gyik/teszt-api-hozzaferes))

4. A kapott válasz adatait mentsd el az alkalmazás saját DB-jébe.

### **2\) Minimálisan szükséges mezők használata**

A nyugta létrehozás XML-ben a kötelező mezők ott látszanak, ahol az XSD-ben minOccurs="1" szerepel.

**Minimál “must have” mezők (a feladatban ezek legyenek benne):**

**beallitasok**

* pdfLetoltes (kötelező)    
* hitelesítéshez használd a szamlaagentkulcs mezőt (a példában is ez van)  

**fejlec**

* elotag (kötelező)    
* fizmod (kötelező)    
* penznem (kötelező)    
* (opcionális, de ajánlott) hivasAzonosito az idempotencia miatt  

**tetelek**

Legalább 1 tétel, tételenként kötelező:

* megnevezes, mennyiseg, mennyisegiEgyseg, nettoEgysegar, afakulcs, netto, afa, brutto  

**Megjegyzés:** A mezők sorrendje kötött az XML-ben.

### **3\) Nyugták listázása**

* A landing oldal a **nyugta lista** oldal legyen.  
* Listában megjelenítendő mezők:  
  * nyugtaszám  
  * keltezés  
  * teljes nettó összeg  
  * teljes bruttó összeg  
* Induláskor legyen **pár teszt nyugta** a DB-ben..

### **4\) Nyugta részletek oldal**

* Nyugta kiválasztása után részletek oldalon jelenjen meg **az összes tárolt adat** (alapadatok, tételek, összegek, Számlázz.hu nyugtaszám stb.).

### **5\) Új nyugta űrlap**

* Angular Material alapú űrlap:  
  * fejléchez: előtag, fizetési mód, pénznem (és opcionálisan megjegyzés)  
  * legalább 1 tétel felvitele (dinamikus tételsorok előny)  
* Mentés gombra:  
  * hívja a saját backend create receipt végpontját,  
  * siker esetén navigáljon vissza a listára (vagy a részletekre).

---

## **Konfiguráció és futtatás**

### **Kötelező konfigurálhatóság**

* Számlázz.hu Agent kulcs ne legyen hardcode-olva:  
  * env var / docker-compose environment ajánlott  
* A pdfLetoltes legyen beállítható (alapból false)


---

## **Leadandó**

* GitHub repo link (itt majd megadjuk kikkel oszd meg a repót)  
* Rövid README:  
  * indítás (docker compose)  
  * elérhető URL-ek (frontend, backend, swagger ha van)  
  * röviden: hogy történik a Számlázz.hu hívás

*2026.01.15.*

