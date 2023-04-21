package fr.simplon.oxo.controller;
import fr.simplon.oxo.SondageRepository;
import fr.simplon.oxo.entity.Sondage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class SondageController {
    private final SondageRepository repo;

    public SondageController(SondageRepository repo) {
        this.repo = repo;
        //this.repo.save(new Sondage("bonbon", "Peut-on avoir plus de bonbons?", LocalDate.now(), LocalDate.of(2023, 4, 27), "Anonyme"));
        //this.repo.save(new Sondage("Les capybaras", "Aimez-vous les capybaras ?", LocalDate.now(), LocalDate.of(2023, 5, 1), "Moi-même"));
    }

    @GetMapping("/sondages")
    public List<Sondage> AllSondages() {
        return repo.findAll();
    }

    @GetMapping("/sondages/{id}")
    public Sondage infoSondage(@PathVariable Long id) {
        return repo.findById(id).orElseThrow();
    }

    @DeleteMapping("/sondages/{id}")
    public void deleteSondage(@PathVariable Long id) {
        repo.deleteById(id);
    }

    @PostMapping("/sondages")
    public Sondage addSondage(@RequestBody Sondage sondage) {
        return repo.save(sondage);
    }

    @PutMapping("/sondages/{id}")
    public Sondage updateSondage(@RequestBody Sondage sondage, @PathVariable Long id) {
        return repo.findById(id).map(Sondage -> {
            sondage.setDescription(sondage.getDescription());
            sondage.setDateCreation(sondage.getDateCreation());
            sondage.setDateCloture(sondage.getDateCloture());
            sondage.setQuestion(sondage.getQuestion());
            sondage.setCreateur(sondage.getCreateur());
            return repo.save(sondage);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sondage non trouvé"));
    }
}

