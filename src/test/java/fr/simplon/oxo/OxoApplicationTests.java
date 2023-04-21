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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import fr.simplon.oxo.SondageRepository;
import org.springframework.http.HttpHeaders;

@SpringBootTest
class OxoApplicationTests {
    private final RestTemplate restTemplate = new RestTemplate();
    private final HttpHeaders headers = new HttpHeaders();

    @Autowired
    private SondageRepository repo;

    private Sondage sondage;

    @BeforeEach
    public void setup() {
        this.sondage = new Sondage("test", "test", LocalDate.now(), LocalDate.of(2023, 4, 27), "test");
        this.repo.save(sondage);
    }

    @Test
    public void testaddSondage() {
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

    @Test
    public void testAllSondages() {
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Sondage> request = new HttpEntity<>(headers);
        ResponseEntity<Sondage[]> response = restTemplate.exchange(
                "http://localhost:8080/sondages",
                HttpMethod.GET,
                request,
                Sondage[].class);
    }

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

    @Test
    public void testDeleteSondage() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:8080/sondages/" + sondage.getId(),
                HttpMethod.DELETE,
                entity,
                Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(repo.findById(sondage.getId()).isPresent());
    }

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

        Sondage sondage = response.getBody();

        assertNotNull(sondage);
        assertEquals(sondage.getId(), sondage.getId());
        assertEquals(sondage.getQuestion(), sondage.getQuestion());
        assertEquals(sondage.getDescription(), sondage.getDescription());
        assertEquals(sondage.getDateCreation(), sondage.getDateCreation());
        assertEquals(sondage.getDateCloture(), sondage.getDateCloture());
        assertEquals(sondage.getCreateur(), sondage.getCreateur());
    }

    @AfterEach
    public void teardown() {
        if (sondage != null) {
            repo.deleteById(sondage.getId());
        }
    }
}

