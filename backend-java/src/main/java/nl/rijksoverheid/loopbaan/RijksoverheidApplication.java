package nl.rijksoverheid.loopbaan;

// ============================================
// IMPORTS
// ============================================
// Dit zijn alle classes die we nodig hebben
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * ============================================
 * HOOFDBESTAND VAN DE APPLICATIE
 * ============================================
 * 
 * Dit is het startpunt van je Spring Boot applicatie.
 * Hier begint alles!
 * 
 * @SpringBootApplication = Dit is een Spring Boot app
 * Dit zorgt voor:
 * - Component scanning (vindt automatisch alle @Controller, @Service, etc.)
 * - Auto-configuration (stelt alles automatisch in)
 * - Property support (leest application.properties)
 * 
 * BELANGRIJK: Dit bestand moet in de ROOT package staan!
 * Alle andere classes moeten in sub-packages staan (controller, model, etc.)
 */
@SpringBootApplication
public class RijksoverheidApplication {

    /**
     * ============================================
     * MAIN METHOD - STARTPUNT
     * ============================================
     * 
     * Dit is waar Java begint met uitvoeren
     * SpringApplication.run() start de hele Spring Boot applicatie
     * 
     * @param args Command line argumenten (negeer voor nu)
     */
    public static void main(String[] args) {
        // Start de Spring Boot applicatie
        SpringApplication.run(RijksoverheidApplication.class, args);
        
        // Als dit succesvol is, zie je in de console:
        // "Started RijksoverheidApplication in X.XXX seconds"
        System.out.println("\n" +
            "============================================\n" +
            "🚀 RIJKSOVERHEID LOOPBAAN BACKEND GESTART!\n" +
            "============================================\n" +
            "Backend draait op: http://localhost:8080\n" +
            "Swagger UI: http://localhost:8080/swagger-ui.html\n" +
            "============================================\n"
        );
    }

    /**
     * ============================================
     * CORS CONFIGURATIE
     * ============================================
     * 
     * CORS = Cross-Origin Resource Sharing
     * 
     * PROBLEEM:
     * - Frontend draait op localhost:5500 (Live Server)
     * - Backend draait op localhost:8080
     * - Browser blokkeert requests tussen verschillende origins (security)
     * 
     * OPLOSSING:
     * - We vertellen de backend: "Sta requests toe van localhost:5500"
     * - Dan kan de frontend gewoon API calls doen
     * 
     * @Bean = Dit is een Spring Bean (Spring beheert dit object)
     * @return WebMvcConfigurer met CORS instellingen
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                    // Alle endpoints (/** = alles)
                    .addMapping("/**")
                    
                    // Sta requests toe van deze origins
                    .allowedOrigins(
                        "http://localhost:5500",     // Live Server
                        "http://127.0.0.1:5500",     // Live Server (alternatief)
                        "http://localhost:3000",     // Als je React gebruikt
                        "http://localhost:8080"      // Voor testen
                    )
                    
                    // Welke HTTP methods zijn toegestaan?
                    .allowedMethods(
                        "GET",      // Ophalen van data
                        "POST",     // Nieuwe data aanmaken
                        "PUT",      // Data updaten
                        "DELETE",   // Data verwijderen
                        "OPTIONS"   // Preflight request (browser doet dit automatisch)
                    )
                    
                    // Welke headers zijn toegestaan?
                    .allowedHeaders("*")  // Alle headers
                    
                    // Sta credentials toe (cookies, authorization headers)
                    .allowCredentials(true)
                    
                    // Hoe lang mag browser CORS response cachen? (1 uur)
                    .maxAge(3600);
            }
        };
    }
}

/**
 * ============================================
 * HOE TE GEBRUIKEN?
 * ============================================
 * 
 * STAP 1: INSTALLEER JAVA & MAVEN
 * - Download Java JDK 17: https://www.oracle.com/java/technologies/downloads/
 * - Maven komt met IntelliJ IDEA
 * 
 * STAP 2: OPEN PROJECT IN INTELLIJ
 * - File -> Open -> Selecteer de backend-java folder
 * - IntelliJ herkent automatisch dat het een Maven project is
 * - Wacht tot Maven alle dependencies download (zie rechtsonder)
 * 
 * STAP 3: CONFIGUREER DATABASE
 * - Open MySQL Workbench
 * - Maak database aan: CREATE DATABASE rijksoverheid_loopbaan;
 * - Pas wachtwoord aan in application.properties
 * 
 * STAP 4: START DE APPLICATIE
 * - Klik op groene play knop naast main() method
 * - Of: Rechtermuisklik op bestand -> Run 'RijksoverheidApplication'
 * - Of: Terminal: mvn spring-boot:run
 * 
 * STAP 5: TEST OF HET WERKT
 * - Open browser: http://localhost:8080
 * - Je ziet een error pagina (dat is normaal, we hebben nog geen endpoints)
 * - Check console: "Started RijksoverheidApplication"
 * 
 * STAP 6: MAAK ENDPOINTS
 * - Maak FunctieController.java (zie volgende bestanden)
 * - Test met Postman of browser
 * 
 * ============================================
 * TROUBLESHOOTING
 * ============================================
 * 
 * PROBLEEM: "Cannot resolve symbol SpringBootApplication"
 * OPLOSSING: Maven dependencies niet gedownload
 *   -> Rechtermuisklik op pom.xml -> Maven -> Reload Project
 * 
 * PROBLEEM: "Port 8080 already in use"
 * OPLOSSING: Andere applicatie gebruikt poort 8080
 *   -> Pas server.port aan in application.properties (bijv. 8081)
 *   -> Of stop andere applicatie
 * 
 * PROBLEEM: "Access denied for user 'root'@'localhost'"
 * OPLOSSING: Verkeerd wachtwoord
 *   -> Pas spring.datasource.password aan in application.properties
 * 
 * PROBLEEM: "Unknown database 'rijksoverheid_loopbaan'"
 * OPLOSSING: Database bestaat niet
 *   -> Maak aan in MySQL Workbench: CREATE DATABASE rijksoverheid_loopbaan;
 * 
 * ============================================
 */
