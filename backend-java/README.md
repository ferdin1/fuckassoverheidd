# 🚀 RIJKSOVERHEID LOOPBAAN - JAVA BACKEND

**Complete Java Spring Boot backend met MySQL database**

## 📋 WAT HEB JE NODIG?

### 1. **Java JDK 17 of hoger**
- Download: https://www.oracle.com/java/technologies/downloads/
- Installeer en check versie: `java -version`

### 2. **IntelliJ IDEA**
- Download Community Edition (GRATIS): https://www.jetbrains.com/idea/download/
- Of gebruik Eclipse/VS Code met Java extensies

### 3. **MySQL Workbench**
- Download: https://dev.mysql.com/downloads/workbench/
- Voor database management

### 4. **Maven** (komt meestal met IntelliJ)
- Check versie: `mvn -version`

---

## 🛠️ INSTALLATIE STAPPEN

### STAP 1: DATABASE OPZETTEN

1. **Open MySQL Workbench**
2. **Maak nieuwe database aan:**
   ```sql
   CREATE DATABASE rijksoverheid_loopbaan;
   ```
3. **Onthoud je wachtwoord!** (of laat leeg als je geen wachtwoord hebt)

### STAP 2: PROJECT OPENEN

1. **Open IntelliJ IDEA**
2. **File → Open**
3. **Selecteer de `backend-java` folder**
4. **Wacht tot Maven alle dependencies download** (zie rechtsonder)
   - Dit kan 5-10 minuten duren de eerste keer
   - Je ziet een progress bar

### STAP 3: CONFIGURATIE AANPASSEN

1. **Open `src/main/resources/application.properties`**
2. **Pas wachtwoord aan:**
   ```properties
   spring.datasource.password=JOUW_WACHTWOORD_HIER
   ```
3. **Als je database een andere naam heeft, pas ook aan:**
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/JOUW_DATABASE_NAAM
   ```

### STAP 4: APPLICATIE STARTEN

**Optie A: Via IntelliJ (makkelijkst)**
1. Open `RijksoverheidApplication.java`
2. Klik op groene play knop (▶️) naast `public static void main`
3. Of: Rechtermuisklik → Run 'RijksoverheidApplication'

**Optie B: Via Terminal**
```bash
cd backend-java
mvn spring-boot:run
```

### STAP 5: CHECK OF HET WERKT

1. **Open browser: http://localhost:8080**
   - Je ziet een error pagina (dat is normaal!)
   - Backend draait nu!

2. **Test API endpoint: http://localhost:8080/api/functies**
   - Je ziet `[]` (lege array, want database is nog leeg)
   - Als je dit ziet: **SUCCESS! 🎉**

---

## 📁 PROJECT STRUCTUUR

```
backend-java/
├── pom.xml                          # Maven configuratie (dependencies)
├── src/
│   └── main/
│       ├── java/
│       │   └── nl/rijksoverheid/loopbaan/
│       │       ├── RijksoverheidApplication.java    # ⭐ START HIER
│       │       ├── controller/
│       │       │   └── FunctieController.java       # REST API endpoints
│       │       ├── model/
│       │       │   └── Functie.java                 # Database tabel
│       │       └── repository/
│       │           └── FunctieRepository.java       # Database queries
│       └── resources/
│           └── application.properties               # Database instellingen
```

---

## 🔌 API ENDPOINTS

**Base URL:** `http://localhost:8080/api/functies`

### 1. **Haal alle functies op**
```http
GET /api/functies
```
**Response:**
```json
[
  {
    "id": 1,
    "titel": "Juridisch Medewerker",
    "beschrijving": "Ondersteunt bij juridische processen",
    "opleiding": "HBO",
    "locatie": "CJIB Leeuwarden",
    "lat": 53.2010,
    "lng": 5.7985
  }
]
```

### 2. **Haal 1 functie op**
```http
GET /api/functies/1
```

### 3. **Maak nieuwe functie aan**
```http
POST /api/functies
Content-Type: application/json

{
  "titel": "Data Analist",
  "beschrijving": "Analyseert data",
  "benodigd": "HBO diploma in data/ICT",
  "opleiding": "HBO",
  "cursus": "SQL & Data Visualisatie",
  "vervolgFunctie": "Junior Data Analist",
  "locatie": "DUO Groningen",
  "lat": 53.2100,
  "lng": 6.5600
}
```

### 4. **Update functie**
```http
PUT /api/functies/1
Content-Type: application/json

{
  "titel": "Senior Juridisch Medewerker",
  ...
}
```

### 5. **Verwijder functie**
```http
DELETE /api/functies/1
```

### 6. **Zoek op opleiding**
```http
GET /api/functies/opleiding/HBO
```

### 7. **Zoek op locatie**
```http
GET /api/functies/locatie/CJIB%20Leeuwarden
```

---

## 🧪 TESTEN MET POSTMAN

1. **Download Postman:** https://www.postman.com/downloads/
2. **Maak nieuwe request**
3. **Selecteer method** (GET, POST, PUT, DELETE)
4. **Vul URL in:** `http://localhost:8080/api/functies`
5. **Voor POST/PUT:**
   - Body → raw → JSON
   - Plak JSON data
6. **Klik Send**

---

## 🌐 FRONTEND KOPPELEN

**In je JavaScript (functies.html):**

```javascript
// Haal alle functies op
async function loadFuncties() {
    const response = await fetch('http://localhost:8080/api/functies');
    const functies = await response.json();
    console.log(functies);
}

// Maak nieuwe functie aan
async function createFunctie(data) {
    const response = await fetch('http://localhost:8080/api/functies', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });
    const result = await response.json();
    console.log('Functie aangemaakt:', result);
}

// Update functie
async function updateFunctie(id, data) {
    const response = await fetch(`http://localhost:8080/api/functies/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });
    const result = await response.json();
    console.log('Functie geüpdatet:', result);
}

// Verwijder functie
async function deleteFunctie(id) {
    await fetch(`http://localhost:8080/api/functies/${id}`, {
        method: 'DELETE'
    });
    console.log('Functie verwijderd');
}
```

---

## ❗ TROUBLESHOOTING

### **PROBLEEM: "Cannot resolve symbol SpringBootApplication"**
**OPLOSSING:**
- Maven dependencies niet gedownload
- Rechtermuisklik op `pom.xml` → Maven → Reload Project
- Wacht tot download klaar is

### **PROBLEEM: "Port 8080 already in use"**
**OPLOSSING:**
- Andere applicatie gebruikt poort 8080
- Pas `server.port=8081` aan in `application.properties`
- Of stop andere applicatie

### **PROBLEEM: "Access denied for user 'root'@'localhost'"**
**OPLOSSING:**
- Verkeerd wachtwoord
- Pas `spring.datasource.password` aan in `application.properties`

### **PROBLEEM: "Unknown database 'rijksoverheid_loopbaan'"**
**OPLOSSING:**
- Database bestaat niet
- Maak aan in MySQL Workbench:
  ```sql
  CREATE DATABASE rijksoverheid_loopbaan;
  ```

### **PROBLEEM: "CORS error" in browser**
**OPLOSSING:**
- Check of `@CrossOrigin` annotatie staat op controller
- Check of backend draait op poort 8080

### **PROBLEEM: "404 Not Found" bij API call**
**OPLOSSING:**
- Check of URL klopt: `http://localhost:8080/api/functies`
- Check of backend draait (zie console)

---

## 📚 MEER LEREN?

- **Spring Boot Docs:** https://spring.io/projects/spring-boot
- **JPA Tutorial:** https://spring.io/guides/gs/accessing-data-jpa/
- **REST API Tutorial:** https://spring.io/guides/gs/rest-service/

---

## 🎯 VOLGENDE STAPPEN

1. ✅ Backend draait
2. ✅ Database verbinding werkt
3. ✅ API endpoints werken
4. 🔲 Koppel frontend (functies.html)
5. 🔲 Voeg User login toe
6. 🔲 Voeg Pins toe voor kaart

---

**Succes! Als je vragen hebt, vraag het gewoon! 🚀**
