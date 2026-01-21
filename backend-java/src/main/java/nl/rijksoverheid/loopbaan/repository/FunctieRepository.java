package nl.rijksoverheid.loopbaan.repository;

// ============================================
// IMPORTS
// ============================================
import nl.rijksoverheid.loopbaan.model.Functie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ============================================
 * FUNCTIE REPOSITORY INTERFACE
 * ============================================
 * 
 * Dit is een REPOSITORY interface.
 * Het handelt alle database operaties af voor Functie entities.
 * 
 * WAT IS EEN REPOSITORY?
 * - Een repository is een laag tussen je applicatie en de database
 * - Het bevat alle database queries (SELECT, INSERT, UPDATE, DELETE)
 * - Spring Data JPA genereert automatisch de implementatie!
 * 
 * JE HOEFT GEEN CODE TE SCHRIJVEN!
 * - Spring maakt automatisch alle basis methods:
 *   * save() - Opslaan/updaten
 *   * findById() - Zoek op ID
 *   * findAll() - Haal alles op
 *   * deleteById() - Verwijder op ID
 *   * count() - Tel aantal records
 *   * exists() - Check of record bestaat
 * 
 * EXTENDS JpaRepository<Functie, Long>
 * - Functie = Het entity type (welke tabel?)
 * - Long = Het type van de primary key (id is Long)
 * 
 * @Repository = Dit is een Spring Repository bean
 */
@Repository
public interface FunctieRepository extends JpaRepository<Functie, Long> {

    /**
     * ============================================
     * AUTOMATISCHE METHODS (van JpaRepository)
     * ============================================
     * 
     * Je krijgt deze methods GRATIS zonder code te schrijven:
     * 
     * // Opslaan/updaten
     * Functie save(Functie functie)
     * 
     * // Zoeken
     * Optional<Functie> findById(Long id)
     * List<Functie> findAll()
     * 
     * // Verwijderen
     * void deleteById(Long id)
     * void delete(Functie functie)
     * 
     * // Checken
     * boolean existsById(Long id)
     * long count()
     * 
     * ============================================
     */

    /**
     * ============================================
     * CUSTOM QUERY METHODS
     * ============================================
     * 
     * Je kunt ook je eigen query methods maken!
     * Spring Data JPA genereert automatisch de SQL op basis van de method naam.
     * 
     * NAMING CONVENTION:
     * findBy[FieldName]
     * findBy[FieldName]And[OtherFieldName]
     * findBy[FieldName]Or[OtherFieldName]
     * findBy[FieldName]OrderBy[OtherFieldName]
     * 
     * VOORBEELDEN:
     */

    /**
     * Zoek alle functies met een specifieke opleiding
     * 
     * SQL die automatisch gegenereerd wordt:
     * SELECT * FROM functies WHERE opleiding = ?
     * 
     * @param opleiding Het opleidingsniveau (bijv. "HBO")
     * @return List van functies met die opleiding
     */
    List<Functie> findByOpleiding(String opleiding);

    /**
     * Zoek alle functies op een specifieke locatie
     * 
     * SQL: SELECT * FROM functies WHERE locatie = ?
     * 
     * @param locatie De locatie naam (bijv. "CJIB Leeuwarden")
     * @return List van functies op die locatie
     */
    List<Functie> findByLocatie(String locatie);

    /**
     * Zoek functies waarvan de titel een bepaalde tekst bevat
     * 
     * SQL: SELECT * FROM functies WHERE titel LIKE %?%
     * 
     * Containing = LIKE %tekst%
     * StartingWith = LIKE tekst%
     * EndingWith = LIKE %tekst
     * 
     * @param titel Deel van de titel om te zoeken
     * @return List van functies met die tekst in de titel
     */
    List<Functie> findByTitelContaining(String titel);

    /**
     * Zoek functies met een specifieke opleiding EN locatie
     * 
     * SQL: SELECT * FROM functies WHERE opleiding = ? AND locatie = ?
     * 
     * @param opleiding Het opleidingsniveau
     * @param locatie De locatie naam
     * @return List van functies die aan beide criteria voldoen
     */
    List<Functie> findByOpleidingAndLocatie(String opleiding, String locatie);

    /**
     * Zoek alle functies, gesorteerd op titel
     * 
     * SQL: SELECT * FROM functies ORDER BY titel ASC
     * 
     * @return List van alle functies, alfabetisch gesorteerd
     */
    List<Functie> findAllByOrderByTitelAsc();

    /**
     * Tel hoeveel functies er zijn met een specifieke opleiding
     * 
     * SQL: SELECT COUNT(*) FROM functies WHERE opleiding = ?
     * 
     * @param opleiding Het opleidingsniveau
     * @return Aantal functies
     */
    long countByOpleiding(String opleiding);

    /**
     * Check of er functies zijn op een locatie
     * 
     * SQL: SELECT COUNT(*) > 0 FROM functies WHERE locatie = ?
     * 
     * @param locatie De locatie naam
     * @return true als er functies zijn, anders false
     */
    boolean existsByLocatie(String locatie);
}

/**
 * ============================================
 * HOE TE GEBRUIKEN?
 * ============================================
 * 
 * In een Service of Controller, inject je de repository:
 * 
 * @Autowired
 * private FunctieRepository functieRepository;
 * 
 * Dan kun je alle methods gebruiken:
 * 
 * // Haal alle functies op
 * List<Functie> alleFuncties = functieRepository.findAll();
 * 
 * // Zoek functie met ID 1
 * Optional<Functie> functie = functieRepository.findById(1L);
 * if (functie.isPresent()) {
 *     System.out.println(functie.get().getTitel());
 * }
 * 
 * // Maak nieuwe functie aan
 * Functie nieuweFunctie = new Functie();
 * nieuweFunctie.setTitel("Data Analist");
 * nieuweFunctie.setOpleiding("HBO");
 * functieRepository.save(nieuweFunctie);
 * 
 * // Update functie
 * functie.get().setTitel("Senior Data Analist");
 * functieRepository.save(functie.get());
 * 
 * // Verwijder functie
 * functieRepository.deleteById(1L);
 * 
 * // Custom queries
 * List<Functie> hboFuncties = functieRepository.findByOpleiding("HBO");
 * List<Functie> cjibFuncties = functieRepository.findByLocatie("CJIB Leeuwarden");
 * 
 * ============================================
 * WAAROM IS DIT ZO MAKKELIJK?
 * ============================================
 * 
 * Spring Data JPA doet ALLES voor je:
 * 1. Maakt automatisch een implementatie van deze interface
 * 2. Genereert SQL queries op basis van method namen
 * 3. Handelt database connecties af
 * 4. Converteert resultaten naar Java objecten
 * 5. Handelt transacties af
 * 
 * JIJ HOEFT ALLEEN:
 * - Interface te maken
 * - Method namen te bedenken (volgens naming convention)
 * - Repository te injecteren waar je het nodig hebt
 * 
 * GEEN SQL SCHRIJVEN NODIG! (tenzij je heel complexe queries wilt)
 * 
 * ============================================
 */
