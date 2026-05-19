# KitchenSync

> Piattaforma gestionale SaaS per ristoranti — Capstone Project Epicode

## L'idea

L'idea nasce da un anno di lavoro in un ristorante in Germania. KitchenSync digitalizza la comunicazione tra reparti (
cucina, pizzeria, sushi, bar) tenendo sincronizzati menu, ingredienti e personale di sala in tempo reale.

## Stack tecnologico

- Java 25 + Spring Boot
- Spring Security + JWT
- PostgreSQL + JPA/Hibernate
- React + Vite *(in arrivo)*

## Stato

Progetto in sviluppo attivo. README completo in arrivo a fine sviluppo.

## Endpoint testati

### Login — `POST /auth/login`

![Login con JWT](docs/screenshots/login.png)

### Crea utente — `POST /utenti`

Endpoint protetto, accessibile solo al SUPER_ADMIN. Permette di creare nuovi ADMIN o METRE assegnando uno o più ruoli
contemporaneamente.

![Crea utente con ruoli multipli](docs/screenshots/crea-utente.png)

### Get me — `GET /utenti/me`

L'utente loggato recupera i propri dati. Spring Security passa direttamente l'oggetto `Utente` tramite
`@AuthenticationPrincipal`, niente query, niente parametri.

![Get me](docs/screenshots/Get-me.png)

### Lista utenti — `GET /utenti`

Endpoint protetto, solo SUPER_ADMIN. Restituisce la lista paginata degli utenti con parametri `page`, `size` (max 30) e
`sortBy`.

![Lista utenti paginata](docs/screenshots/lista-utenti.png)

## Gestione errori centralizzata

Tutte le eccezioni del backend vengono catturate da un `ErrorsHandler` con `@RestControllerAdvice` e trasformate in JSON
pulito con messaggio e timestamp. Status code semanticamente corretti per ogni tipo di errore.

### Errore di credenziali

Stesso endpoint, email inesistente → risposta strutturata, status `401 Unauthorized`.

![Errore gestito](docs/screenshots/errore-gestito.png)

### Errore di validazione

Email malformata e password vuota → tutti gli errori di validazione raccolti in un unico messaggio, status
`400 Bad Request`.

![Errore di validazione](docs/screenshots/errore-validazione.png)

## 📚 Documentazione API — Swagger

Tutta l'API è documentata con OpenAPI 3.1 e Swagger UI. Aggiunta una configurazione dedicata per l'autorizzazione Bearer
Token, così ogni endpoint protetto può essere testato direttamente dal browser senza Postman.

**Disponibile su:** `http://localhost:3001/swagger-ui.html`

![Swagger UI con autenticazione Bearer](docs/screenshots/swagger-overwie.png)

## Autore

**Antonino Palazzolo** — Capstone Project Epicode