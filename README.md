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

## Modello dati

La V1 copre autenticazione, menu vivo e gestione utenti.
Tutti gli id sono UUID. Nessun `@ManyToMany` — ogni relazione many-to-many
è implementata con una tabella di mezzo esplicita con due `@ManyToOne`.

![Diagramma V1](docs/screenshots/diagramma-v1.webp)

### Sezioni

I reparti del ristorante (Pizzeria, Cucina, Sushi, Bar). Il flag `attiva`
permette di disattivare un reparto senza eliminarlo.

### Ingredienti

Il flag `disponibile` è il cuore del menu vivo: quando un ingrediente è
esaurito, tutti i piatti che lo contengono spariscono automaticamente dal
menu senza toccare il database.

### piatti_ingredienti

Tabella di mezzo esplicita tra `piatti` e `ingredienti`. Ogni riga dice
"questo piatto contiene questo ingrediente" — è questa tabella che permette
al sistema di calcolare quali piatti escludere dal menu vivo.

### utenti_ruoli

Tabella di mezzo esplicita tra `utenti` e `ruoli`. Un utente può avere
più ruoli contemporaneamente.

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

### Crea sezione — `POST /sezioni`

Endpoint protetto, accessibile solo al SUPER_ADMIN. Permette di creare una nuova sezione del ristorante (es. Pizzeria,
Cucina, Sushi, Bar). Il nome deve essere univoco — se esiste già viene restituito un errore 400.

![Crea sezione](docs/screenshots/crea-sezione.png)

### Lista sezioni — `GET /sezioni`

Endpoint protetto, solo SUPER_ADMIN. Restituisce la lista paginata delle sezioni con parametri `page`, `size` (max 30) e
`sortBy`. Ordinate alfabeticamente per nome di default.

![Lista sezioni paginata](docs/screenshots/lista-sezioni.png)

Dalla Slice 3 è possibile assegnare una sezione all'utente ADMIN al momento della creazione.

![Crea utente con sezione](docs/screenshots/crea-utente-con-sezione.png)

### Crea categoria — `POST /categorie`

Endpoint protetto, accessibile solo all'ADMIN. La categoria viene automaticamente assegnata alla sezione dell'admin
loggato — niente parametri extra. Il nome deve essere univoco dentro la stessa sezione.

![Crea categoria](docs/screenshots/crea-categoria-con-sezione.png)

### Lista categorie — `GET /categorie`

Endpoint protetto, solo ADMIN. Restituisce le categorie filtrate automaticamente per la sezione dell'admin loggato.

![Lista categorie](docs/screenshots/lista-categorie-con-sezione.png)

### Crea ingrediente — `POST /ingredienti`

Endpoint protetto, accessibile solo all'ADMIN. L'ingrediente viene automaticamente assegnato alla sezione dell'admin
loggato. Il nome deve essere univoco dentro la stessa sezione.

![Crea ingrediente](docs/screenshots/crea-ingrediente.png)

### Cambia disponibilità — `PATCH /ingredienti/{id}/disponibilita`

L'ADMIN può segnare un ingrediente come esaurito con una chiamata minimale — solo un boolean nel body. Quando un
ingrediente è `false`, tutti i piatti che lo usano spariscono dal menu vivo.

![Cambia disponibilità ingrediente](docs/screenshots/cambia-disponibiltà-ingrediente.png)

### Crea piatto — `POST /piatti`

Endpoint protetto, accessibile solo all'ADMIN. Il piatto viene assegnato alla categoria specificata nel body — il
sistema controlla automaticamente che la categoria appartenga alla sezione dell'admin loggato.

![Crea piatto](docs/screenshots/crea-piatto.png)

### Collega ingrediente a piatto — `POST /piatti-ingredienti`

Endpoint protetto, accessibile solo all'ADMIN. Collega un ingrediente a un piatto — il sistema controlla che
appartengano alla stessa sezione e che il collegamento non esista già.

![Collegamento piatto ingrediente](docs/screenshots/collegamento-piatto-ingrediente.png)

### Menu vivo — `GET /menu`

Endpoint protetto, accessibile solo al METRE e al SUPER_ADMIN. Restituisce il menu completo organizzato per sezione →
categoria → piatti. Esclude automaticamente le sezioni disattivate e i piatti con almeno un ingrediente non disponibile.

![Menu vivo](docs/screenshots/visualizzazione-menu.png)

### Upload immagine piatto — `POST /piatti/{id}/immagine`

Endpoint protetto, accessibile solo all'ADMIN. Carica l'immagine del piatto su Cloudinary e salva l'URL pubblico nel
database. La richiesta usa `multipart/form-data` con il campo `file`.

![Upload immagine piatto](docs/screenshots/assegnazione-immagine-a-piatto.png)

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

### Sezione già esistente

Nome duplicato → risposta strutturata, status `400 Bad Request`.

![Sezione duplicata](docs/screenshots/messaggio-errore-sezione.png)

### Sezione non trovata

Id sezione inesistente durante la creazione utente → risposta strutturata, status `404 Not Found`.

![Sezione non trovata](docs/screenshots/crea-tente-con-sezione-id-sbagliato.png)

### Email duplicata con sezione

Email già in uso durante la creazione utente con sezione → risposta strutturata, status `400 Bad Request`.

![Email duplicata](docs/screenshots/crea-utente-con-sezione-email-duplicata.png)

### Ingrediente duplicato

Nome già esistente nella stessa sezione → risposta strutturata, status `400 Bad Request`.

![Ingrediente duplicato](docs/screenshots/ingrediente-duplicato.png)

### Piatto duplicato

Nome già esistente nella stessa categoria → risposta strutturata, status `400 Bad Request`.

![Piatto duplicato](docs/screenshots/piatto-replicato.png)

### Collegamento duplicato

Ingrediente già collegato allo stesso piatto → risposta strutturata, status `400 Bad Request`.

![Collegamento duplicato](docs/screenshots/errore-duplicato.png)

## 📚 Documentazione API — Swagger

Tutta l'API è documentata con OpenAPI 3.1 e Swagger UI. Aggiunta una configurazione dedicata per l'autorizzazione Bearer
Token, così ogni endpoint protetto può essere testato direttamente dal browser senza Postman.

**Disponibile su:** `http://localhost:3001/swagger-ui.html`

![Swagger UI con autenticazione Bearer](docs/screenshots/swagger-overwie.png)

## Frontend

Il frontend React è disponibile su: [kitchensync-frontend](https://github.com/palax-99/kitchensync-frontend)

## Autore

**Antonino Palazzolo** — Capstone Project Epicode