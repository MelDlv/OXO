package fr.simplon.oxo;
import fr.simplon.oxo.entity.Sondage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

import fr.simplon.oxo.SondageRepository;
import org.springframework.http.HttpHeaders;

/**
 * Classe de test pour l'application Oxo.
 */
@SpringBootTest
class OxoApplicationTests {
    /**
     * Client REST pour envoyer des requêtes HTTP.
     */
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Headers HTTP à utiliser dans les requêtes.
     */
    private final HttpHeaders headers = new HttpHeaders();

    @Autowired
    private SondageRepository repo;

    /**
     * Sondage de test à utiliser dans les tests.
     */
    private Sondage sondage;

    /**
     * Méthode exécutée avant chaque test pour initialiser le sondage de test.
     */
    @BeforeEach
    public void setup() {
        this.sondage = new Sondage("test", "test", LocalDate.now(), LocalDate.of(2023, 4, 27), "test");
        this.repo.save(sondage);
    }

    /**
     * Test pour l'ajout d'un nouveau sondage.
     */
    @Test
    public void testaddSondage() {
        //définit le type de contenu de la demande. Ici, le type de contenu est JSON.
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Sondage> request = new HttpEntity<>(sondage, headers);

        ResponseEntity<Sondage> response = restTemplate.exchange(
                "http://localhost:8080/sondages",
                HttpMethod.POST,
                request,
                Sondage.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getId());
        assertEquals(sondage.getQuestion(), response.getBody().getQuestion());
        assertEquals(sondage.getDescription(), response.getBody().getDescription());
        assertEquals(sondage.getDateCreation(), response.getBody().getDateCreation());
        assertEquals(sondage.getDateCloture(), response.getBody().getDateCloture());
        assertEquals(sondage.getCreateur(), response.getBody().getCreateur());
    }

    /**
     * Test pour la récupération de tous les sondages.
     */
    @Test
    public void testAllSondages() {
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Sondage> request = new HttpEntity<>(headers);
        ResponseEntity<Sondage[]> response = restTemplate.exchange(
                "http://localhost:8080/sondages",
                HttpMethod.GET,
                request,
                Sondage[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Sondage[] sondages = response.getBody();
        assertNotNull(sondages);
        assertTrue(sondages.length > 0);
    }

    /**
     * Test pour la mise à jour d'un sondage par son id.
     */
    @Test
    public void testupdateSondage() {
        sondage.setQuestion("update question");
        sondage.setCreateur("update createur");

        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Sondage> request = new HttpEntity<>(sondage, headers);

        ResponseEntity<Sondage> response = restTemplate.exchange(
                "http://localhost:8080/sondages/" + sondage.getId(),
                HttpMethod.PUT,
                request,
                Sondage.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sondage.getQuestion(), response.getBody().getQuestion());
        assertEquals(sondage.getDescription(), response.getBody().getDescription());
        assertEquals(sondage.getDateCreation(), response.getBody().getDateCreation());
        assertEquals(sondage.getDateCloture(), response.getBody().getDateCloture());
        assertEquals(sondage.getCreateur(), response.getBody().getCreateur());
        assertEquals(sondage.getId(), response.getBody().getId());
    }

    /**
     * Test pour la suppression d'un sondage par son id.
     */
    @Test
    public void testDeleteSondage() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:8080/sondages/" + sondage.getId(),
                HttpMethod.DELETE,
                request,
                Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(repo.findById(sondage.getId()).isPresent());
    }

    /**
     * Test pour vérifier la récupération des informations d'un sondage par son id.
     */
    @Test
    public void testinfoSondage() {
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Sondage> request = new HttpEntity<>(headers);

        ResponseEntity<Sondage> response = restTemplate.exchange(
                "http://localhost:8080/sondages/{id}",
                HttpMethod.GET,
                request,
                Sondage.class,
                sondage.getId());

        Sondage sondageRecupere = response.getBody();

        assertNotNull(sondageRecupere);
        assertEquals(sondage.getId(), sondageRecupere.getId());
        assertEquals(sondage.getQuestion(), sondageRecupere.getQuestion());
        assertEquals(sondage.getDescription(), sondageRecupere.getDescription());
        assertEquals(sondage.getDateCreation(), sondageRecupere.getDateCreation());
        assertEquals(sondage.getDateCloture(), sondageRecupere.getDateCloture());
        assertEquals(sondage.getCreateur(), sondageRecupere.getCreateur());
    }

    /**
     * Méthode exécutée après chaque test pour supprimer le sondage créé lors avant chaque test.
     */
    @AfterEach
    public void teardown() {
        if (sondage != null) {
            repo.deleteById(sondage.getId());
        }
    }
}

