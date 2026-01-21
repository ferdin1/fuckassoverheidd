package nl.rijksoverheid.loopbaan.model;

// ============================================
// IMPORTS
// ============================================
import jakarta.persistence.*;  // JPA annotations voor database mapping
import lombok.Data;            // Lombok voor automatische getters/setters
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * ============================================
 * FUNCTIE MODEL CLASS
 * ============================================
 * 
 * Dit is een MODEL class (ook wel ENTITY genoemd).
 * Het representeert een rij in de database tabel "functies".
 * 
 * JAVA OBJECT <-> DATABASE TABEL
 * Functie object <-> functies tabel
 * 
 * Elk veld in deze class = een kolom in de database
 * Elk object = een rij in de database
 * 
 * ANNOTATIES UITLEG:
 * @Entity = Dit is een database entity (tabel)
 * @Table = Naam van de tabel in database
 * @Data = Lombok genereert automatisch getters, setters, toString, equals, hashCode
 * @NoArgsConstructor = Lombok maakt een lege constructor
 * @AllArgsConstructor = Lombok maakt een constructor met alle velden
 */
@Entity  // Dit is een JPA entity
@Table(name = "functies")  // Tabel naam in database
@Data  // Lombok: automatische getters/setters
@NoArgsConstructor  // Lege constructor (nodig voor JPA)
@AllArgsConstructor  // Constructor met alle velden
public class Functie {

    /**
     * ============================================
     * PRIMARY KEY (ID)
     * ============================================
     * 
     * @Id = Dit is de primary key
     * @GeneratedValue = Database genereert automatisch een waarde
     * IDENTITY = Auto-increment (1, 2, 3, 4, ...)
     * 
     * Je hoeft zelf geen ID in te vullen, MySQL doet dit automatisch!
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ============================================
     * FUNCTIE INFORMATIE
     * ============================================
     */
    
    // Titel van de functie (bijv. "Juridisch Medewerker")
    // @Column = Extra database kolom configuratie
    // nullable = false -> Dit veld MAG NIET leeg zijn (NOT NULL in SQL)
    // length = 100 -> Maximaal 100 karakters (VARCHAR(100) in SQL)
    @Column(nullable = false, length = 100)
    private String titel;

    // Beschrijving van de functie
    // @Column(columnDefinition = "TEXT") -> Gebruik TEXT type (voor lange teksten)
    // TEXT kan veel meer karakters bevatten dan VARCHAR
    @Column(columnDefinition = "TEXT")
    private String beschrijving;

    // Wat heb je nodig voor deze functie?
    @Column(columnDefinition = "TEXT")
    private String benodigd;

    // Opleidingsniveau (MBO, HBO, WO)
    @Column(length = 50)
    private String opleiding;

    /**
     * ============================================
     * EXTRA INFORMATIE
     * ============================================
     */
    
    // Welke cursus moet je volgen?
    @Column(length = 100)
    private String cursus;

    // Welke functie moet je eerst hebben gedaan?
    @Column(length = 100)
    private String vervolgFunctie;

    /**
     * ============================================
     * LOCATIE INFORMATIE
     * ============================================
     */
    
    // Naam van de locatie (bijv. "CJIB Leeuwarden")
    @Column(length = 100)
    private String locatie;

    // Latitude (breedtegraad) voor kaart
    // DECIMAL(10,8) in SQL = 10 cijfers totaal, 8 na de komma
    // Voorbeeld: 53.20100000
    @Column(precision = 10, scale = 8)
    private Double lat;

    // Longitude (lengtegraad) voor kaart
    // DECIMAL(11,8) in SQL = 11 cijfers totaal, 8 na de komma
    // Voorbeeld: 5.79850000
    @Column(precision = 11, scale = 8)
    private Double lng;

    /**
     * ============================================
     * TIMESTAMPS
     * ============================================
     * 
     * Automatisch bijgehouden door JPA
     */
    
    // Wanneer is deze functie aangemaakt?
    // @Column(updatable = false) = Kan niet meer gewijzigd worden na aanmaken
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Wanneer is deze functie voor het laatst gewijzigd?
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * ============================================
     * LIFECYCLE CALLBACKS
     * ============================================
     * 
     * Deze methods worden automatisch aangeroepen door JPA
     * op bepaalde momenten in de lifecycle van een entity
     */
    
    /**
     * Wordt aangeroepen VOOR het opslaan in database (eerste keer)
     * Zet createdAt en updatedAt op huidige tijd
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Wordt aangeroepen VOOR het updaten in database
     * Update alleen updatedAt (createdAt blijft hetzelfde)
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

/**
 * ============================================
 * HOE WERKT DIT?
 * ============================================
 * 
 * VOORBEELD: Nieuwe functie aanmaken
 * 
 * // Maak een nieuw Functie object
 * Functie functie = new Functie();
 * functie.setTitel("Juridisch Medewerker");
 * functie.setBeschrijving("Ondersteunt bij juridische processen");
 * functie.setOpleiding("HBO");
 * functie.setLocatie("CJIB Leeuwarden");
 * functie.setLat(53.2010);
 * functie.setLng(5.7985);
 * 
 * // Sla op in database (via repository)
 * functieRepository.save(functie);
 * 
 * // JPA doet nu automatisch:
 * // 1. Roept onCreate() aan -> zet timestamps
 * // 2. Genereert SQL: INSERT INTO functies (titel, beschrijving, ...) VALUES (?, ?, ...)
 * // 3. Voert SQL uit
 * // 4. Haalt gegenereerde ID op
 * // 5. Zet ID in het object
 * 
 * ============================================
 * DATABASE TABEL
 * ============================================
 * 
 * Hibernate maakt automatisch deze tabel aan:
 * 
 * CREATE TABLE functies (
 *     id BIGINT AUTO_INCREMENT PRIMARY KEY,
 *     titel VARCHAR(100) NOT NULL,
 *     beschrijving TEXT,
 *     benodigd TEXT,
 *     opleiding VARCHAR(50),
 *     cursus VARCHAR(100),
 *     vervolg_functie VARCHAR(100),
 *     locatie VARCHAR(100),
 *     lat DECIMAL(10,8),
 *     lng DECIMAL(11,8),
 *     created_at DATETIME,
 *     updated_at DATETIME
 * );
 * 
 * ============================================
 * LOMBOK UITLEG
 * ============================================
 * 
 * Zonder Lombok zou je dit moeten schrijven:
 * 
 * public String getTitel() { return titel; }
 * public void setTitel(String titel) { this.titel = titel; }
 * public String getBeschrijving() { return beschrijving; }
 * public void setBeschrijving(String beschrijving) { this.beschrijving = beschrijving; }
 * ... (voor ALLE velden!)
 * 
 * Met @Data doet Lombok dit AUTOMATISCH!
 * 
 * ============================================
 */
