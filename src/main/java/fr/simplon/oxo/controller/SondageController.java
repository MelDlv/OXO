package fr.simplon.oxo.controller;
import fr.simplon.oxo.SondageRepository;
import fr.simplon.oxo.entity.Sondage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.List;

/**
 * Contrôleur gérant les opérations liées aux sondages.
 */
@RestController
public class SondageController {
    private final SondageRepository repo;

    public SondageController(SondageRepository repo) {
        this.repo = repo;
        //this.repo.save(new Sondage("Logo", "Pensez-vous que le capybara pourrait être un bon choix de logo pour l'entreprise Oxo?", LocalDate.now(), LocalDate.of(2023, 4, 29), "Anonyme"));
        //this.repo.save(new Sondage("Les capybaras", "Aimez-vous les capybaras ?", LocalDate.now(), LocalDate.of(2023, 5, 1), "Moi-même"));
    }

    /**
     * Cette méthode permet de récupérer tous les sondages enregistrés.
     * @return une liste de tous les sondages enregistrés en bdd
     */
    @GetMapping("/sondages")
    public List<Sondage> AllSondages() {
        return repo.findAll();
    }

    /**
     * Cette méthode permet de récupérer les informations d'un sondage à partir de son ID.
     * @param id l'ID du sondage dont on veut récupérer les informations
     * @return les informations du sondage correspondant à l'ID
     */
    @GetMapping("/sondages/{id}")
    public Sondage infoSondage(@PathVariable Long id) {
        return repo.findById(id).orElseThrow();
    }

    /**
     * Cette méthode permet de supprimer un sondage enregistré à partir de son ID.
     * @param id l'ID du sondage à supprimer
     */
    @DeleteMapping("/sondages/{id}")
    public void deleteSondage(@PathVariable Long id) {
        repo.deleteById(id);
    }

    /**
     * Cette méthode permet d'ajouter un nouveau sondage à la base de données.
     * @param sondage le nouveau sondage à ajouter
     * @return le sondage ajouté
     */
    @PostMapping("/sondages")
    public Sondage addSondage(@RequestBody Sondage sondage) {
        return repo.save(sondage);
    }

    /**
     * Cette méthode permet de mettre à jour les informations d'un sondage enregistré à partir de son ID.
     * @param sondage
     * @param id l'ID du sondage à mettre à jour
     * @return le sondage mis à jour
     */
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

