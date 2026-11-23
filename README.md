# Net Pizzéria – Java alkalmazások gyakorlat beadandó

Szerver oldali webalkalmazás Java 25 + Spring Boot 4 keretrendszerrel, a **Pizza** adatbázisra
(táblák: `kategoria`, `pizza`, `rendeles`). A tanári *Autentikáció* megoldás továbbfejlesztése.

**Élő oldal:** http://rivendell.nje.hu:9443/qjawqn/

## Menük

| Menü | Ki látja | Tartalom |
|---|---|---|
| Főoldal | mindenki | az alkalmazás bemutatása |
| Adatbázis | mindenki | a 3 tábla adatai (Repository-kon keresztül) |
| Kapcsolat | mindenki | üzenetküldő űrlap, szerver oldali validáció, mentés adatbázisba |
| Üzenetek | bejelentkezett felhasználó | az üzenetek fordított időrendben |
| Diagram | mindenki | Chart.js diagramok |
| CRUD | admin | a pizza tábla kezelése |
| REST | mindenki | RESTful API (`/api/pizzak`) |
| Login / Regisztráció | nem bejelentkezett | |

Téma: **Bootswatch „United”** (Bootstrap 5, ingyenes, reszponzív).

## Futtatás a saját gépen

IntelliJ-ben a `PizzaApplication` indítása. Helyben a `local` profil fut **H2 memória-adatbázissal**,
nem kell hozzá MySQL. Induláskor a program betölti a Pizza adatokat (`src/main/resources/data`)
és létrehozza az admin felhasználót.

http://localhost:8080 – admin belépés: `admin` / `admin123`

## WAR készítése és feltöltés a szerverre

1. Maven → Lifecycle → `clean`, majd `package` → `target/qjawqn.war`
2. WinSCP: `rivendell.nje.hu`, Neptun-kód + Neptun-jelszó
3. A `qjawqn.war` bemásolása a `/opt/tomcat/webapps` mappába
4. Kis idő múlva: http://rivendell.nje.hu:9443/qjawqn/

A szerveren a MySQL adatbázist használja (`application.properties`); a táblákat és az adatokat
az első induláskor automatikusan létrehozza.

## REST API

| Metódus | Cím |
|---|---|
| GET | `/api/pizzak` |
| GET | `/api/pizzak/{nev}` |
| POST | `/api/pizzak` |
| PUT | `/api/pizzak/{nev}` |
| DELETE | `/api/pizzak/{nev}` |
