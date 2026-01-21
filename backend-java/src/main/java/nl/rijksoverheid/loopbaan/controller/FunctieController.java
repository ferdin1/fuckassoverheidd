package nl.rijksoverheid.loopbaan.controller;

// ============================================
// IMPORTS
// ============================================
import nl.rijksoverheid.loopbaan.model.Functie;
import nl.rijksoverheid.loopbaan.repository.FunctieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * ============================================
 * FUNCTIE CONTROLLER
 * ============================================
 * 
 * Dit is een REST API CONTROLLER.
 * Het handelt alle HTTP requests af voor functies.
 * 
 * WAT IS EEN CONTROLLER?
 * - Een controller is de "voordeur" van je backend
 * - Het ontvangt HTTP requests van de frontend
 * - Het stuurt HTTP responses terug
 * 
 * REST API = Representational State Transfer
 * - Een manier om data uit te wisselen via HTTP
 * - Gebruikt JSON format
 * - Gebruikt HTTP methods: GET, POST, PUT, DELETE
 * 
 * ANNOTATIES:
 * @RestController = Dit is een REST API controller
 * @RequestMapping = Basis URL voor alle endpoints in deze controller
 * @CrossOrigin = Sta CORS toe (frontend kan API aanroepen)
 */
@RestController
@RequestMapping("/api/functies")  // Alle endpoints beginnen met /api/functies
@CrossOrigin(origins = "*")  // Sta alle origins toe (voor development)
public class FunctieController {

    /**
     * ============================================
     * DEPENDENCY INJECTION
     * ============================================
     * 
     * @Autowired = Spring injecteert automatisch een FunctieRepository
     * 
     * Je hoeft niet zelf een repository te maken:
     * FunctieRepository repo = new FunctieRepository(); // NIET NODIG!
     * 
     * Spring doet dit automatisch voor je!
     */
    @Autowired
    private FunctieRepository functieRepository;

    /**
     * ============================================
     * ENDPOINT 1: HAAL ALLE FUNCTIES OP
     * ============================================
     * 
     * HTTP Method: GET
     * URL: http://localhost:8080/api/functies
     * Response: JSON array met alle functies
     * 
     * @GetMapping = Dit endpoint reageert op GET requests
     * @return List van alle functies
     * 
     * VOORBEELD REQUEST (vanuit frontend):
     * fetch('http://localhost:8080/api/functies')
     *   .then(response => response.json())
     *   .then(data => console.log(data));
     * 
     * VOORBEELD RESPONSE:
     * [
     *   {
     *     "id": 1,
     *     "titel": "Juridisch Medewerker",
     *     "opleiding": "HBO",
     *     ...
     *   },
     *   {
     *     "id": 2,
     *     "titel": "Data Analist",
     *     ...
     *   }
     * ]
     */
    @GetMapping
    public List<Functie> getAlleFuncties() {
        // Haal alle functies op uit database
        return functieRepository.findAll();
    }

    /**
     * ============================================
     * ENDPOINT 2: HAAL 1 FUNCTIE OP (OP BASIS VAN ID)
     * ============================================
     * 
     * HTTP Method: GET
     * URL: http://localhost:8080/api/functies/1
     * Response: JSON object van 1 functie
     * 
     * @GetMapping("/{id}") = URL parameter {id}
     * @PathVariable = Haal {id} uit de URL
     * @return ResponseEntity met functie of 404 error
     * 
     * VOORBEELD REQUEST:
     * fetch('http://localhost:8080/api/functies/1')
     *   .then(response => response.json())
     *   .then(data => console.log(data));
     * 
     * VOORBEELD RESPONSE (als gevonden):
     * {
     *   "id": 1,
     *   "titel": "Juridisch Medewerker",
     *   "beschrijving": "Ondersteunt bij juridische processen",
     *   "opleiding": "HBO",
     *   ...
     * }
     * 
     * VOORBEELD RESPONSE (als NIET gevonden):
     * HTTP 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Functie> getFunctieById(@PathVariable Long id) {
        // Zoek functie in database
        Optional<Functie> functie = functieRepository.findById(id);
        
        // Check of functie bestaat
        if (functie.isPresent()) {
            // Functie gevonden -> return 200 OK met functie data
            return ResponseEntity.ok(functie.get());
        } else {
            // Functie NIET gevonden -> return 404 Not Found
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * ============================================
     * ENDPOINT 3: MAAK NIEUWE FUNCTIE AAN
     * ============================================
     * 
     * HTTP Method: POST
     * URL: http://localhost:8080/api/functies
     * Request Body: JSON object met functie data
     * Response: JSON object van aangemaakte functie
     * 
     * @PostMapping = Dit endpoint reageert op POST requests
     * @RequestBody = Haal JSON data uit request body en converteer naar Functie object
     * @return De aangemaakte functie (met gegenereerde ID)
     * 
     * VOORBEELD REQUEST:
     * fetch('http://localhost:8080/api/functies', {
     *   method: 'POST',
     *   headers: { 'Content-Type': 'application/json' },
     *   body: JSON.stringify({
     *     titel: "Data Analist",
     *     beschrijving: "Analyseert data",
     *     opleiding: "HBO",
     *     locatie: "DUO Groningen",
     *     lat: 53.2100,
     *     lng: 6.5600
     *   })
     * });
     * 
     * VOORBEELD RESPONSE:
     * {
     *   "id": 11,  // <-- Automatisch gegenereerd door database!
     *   "titel": "Data Analist",
     *   "beschrijving": "Analyseert data",
     *   "opleiding": "HBO",
     *   "locatie": "DUO Groningen",
     *   "lat": 53.21,
     *   "lng": 6.56,
     *   "createdAt": "2026-01-21T15:00:00",
     *   "updatedAt": "2026-01-21T15:00:00"
     * }
     */
    @PostMapping
    public ResponseEntity<Functie> createFunctie(@RequestBody Functie functie) {
        // Sla functie op in database
        // save() doet automatisch INSERT als ID null is
        Functie savedFunctie = functieRepository.save(functie);
        
        // Return 201 Created met de aangemaakte functie
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFunctie);
    }

    /**
     * ============================================
     * ENDPOINT 4: UPDATE BESTAANDE FUNCTIE
     * ============================================
     * 
     * HTTP Method: PUT
     * URL: http://localhost:8080/api/functies/1
     * Request Body: JSON object met nieuwe functie data
     * Response: JSON object van geüpdatete functie
     * 
     * @PutMapping("/{id}") = URL parameter {id}
     * @PathVariable = Haal {id} uit de URL
     * @RequestBody = Haal nieuwe data uit request body
     * @return De geüpdatete functie of 404 error
     * 
     * VOORBEELD REQUEST:
     * fetch('http://localhost:8080/api/functies/1', {
     *   method: 'PUT',
     *   headers: { 'Content-Type': 'application/json' },
     *   body: JSON.stringify({
     *     titel: "Senior Juridisch Medewerker",  // <-- Gewijzigd!
     *     beschrijving: "Ondersteunt bij complexe juridische processen",
     *     opleiding: "HBO",
     *     ...
     *   })
     * });
     */
    @PutMapping("/{id}")
    public ResponseEntity<Functie> updateFunctie(
            @PathVariable Long id,
            @RequestBody Functie functieDetails) {
        
        // Zoek bestaande functie
        Optional<Functie> optionalFunctie = functieRepository.findById(id);
        
        if (optionalFunctie.isPresent()) {
            // Functie bestaat -> update alle velden
            Functie functie = optionalFunctie.get();
            
            // Update velden met nieuwe data
            functie.setTitel(functieDetails.getTitel());
            functie.setBeschrijving(functieDetails.getBeschrijving());
            functie.setBenodigd(functieDetails.getBenodigd());
            functie.setOpleiding(functieDetails.getOpleiding());
            functie.setCursus(functieDetails.getCursus());
            functie.setVervolgFunctie(functieDetails.getVervolgFunctie());
            functie.setLocatie(functieDetails.getLocatie());
            functie.setLat(functieDetails.getLat());
            functie.setLng(functieDetails.getLng());
            
            // Sla geüpdatete functie op
            // save() doet automatisch UPDATE als ID bestaat
            Functie updatedFunctie = functieRepository.save(functie);
            
            // Return 200 OK met geüpdatete functie
            return ResponseEntity.ok(updatedFunctie);
        } else {
            // Functie bestaat NIET -> return 404 Not Found
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * ============================================
     * ENDPOINT 5: VERWIJDER FUNCTIE
     * ============================================
     * 
     * HTTP Method: DELETE
     * URL: http://localhost:8080/api/functies/1
     * Response: 204 No Content (success) of 404 Not Found
     * 
     * @DeleteMapping("/{id}") = URL parameter {id}
     * @PathVariable = Haal {id} uit de URL
     * @return 204 No Content of 404 Not Found
     * 
     * VOORBEELD REQUEST:
     * fetch('http://localhost:8080/api/functies/1', {
     *   method: 'DELETE'
     * });
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFunctie(@PathVariable Long id) {
        // Check of functie bestaat
        if (functieRepository.existsById(id)) {
            // Functie bestaat -> verwijder
            functieRepository.deleteById(id);
            
            // Return 204 No Content (success, geen data terug)
            return ResponseEntity.noContent().build();
        } else {
            // Functie bestaat NIET -> return 404 Not Found
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * ============================================
     * ENDPOINT 6: ZOEK FUNCTIES OP OPLEIDING
     * ============================================
     * 
     * HTTP Method: GET
     * URL: http://localhost:8080/api/functies/opleiding/HBO
     * Response: JSON array met functies
     * 
     * VOORBEELD REQUEST:
     * fetch('http://localhost:8080/api/functies/opleiding/HBO')
     *   .then(response => response.json())
     *   .then(data => console.log(data));
     */
    @GetMapping("/opleiding/{opleiding}")
    public List<Functie> getFunctiesByOpleiding(@PathVariable String opleiding) {
        return functieRepository.findByOpleiding(opleiding);
    }

    /**
     * ============================================
     * ENDPOINT 7: ZOEK FUNCTIES OP LOCATIE
     * ============================================
     * 
     * HTTP Method: GET
     * URL: http://localhost:8080/api/functies/locatie/CJIB Leeuwarden
     * Response: JSON array met functies
     */
    @GetMapping("/locatie/{locatie}")
    public List<Functie> getFunctiesByLocatie(@PathVariable String locatie) {
        return functieRepository.findByLocatie(locatie);
    }
}

/**
 * ============================================
 * HOE TE TESTEN?
 * ============================================
 * 
 * OPTIE 1: BROWSER (alleen GET requests)
 * - Open: http://localhost:8080/api/functies
 * - Je ziet JSON response
 * 
 * OPTIE 2: POSTMAN (alle requests)
 * - Download Postman (gratis)
 * - Maak nieuwe request
 * - Selecteer method (GET, POST, PUT, DELETE)
 * - Vul URL in
 * - Voor POST/PUT: Body -> raw -> JSON
 * - Klik Send
 * 
 * OPTIE 3: FRONTEND (JavaScript fetch)
 * - Zie voorbeelden hierboven
 * 
 * ============================================
 * TROUBLESHOOTING
 * ============================================
 * 
 * PROBLEEM: "404 Not Found"
 * OPLOSSING: Check of URL klopt en backend draait
 * 
 * PROBLEEM: "CORS error"
 * OPLOSSING: @CrossOrigin annotatie toegevoegd? Check!
 * 
 * PROBLEEM: "500 Internal Server Error"
 * OPLOSSING: Check console logs voor error message
 * 
 * ============================================
 */
