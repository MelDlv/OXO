package fr.simplon.oxo.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

/**
 * Cette classe représente un sondage avec une description, une question, une date de création, une date de clôture et un créateur.
 */
@Entity
@Table(name = "sondages")
public class Sondage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 120)
    private String description;

    @NotBlank
    @Size(max = 120)
    private String question;

    @NotNull
    @Column(name="dateCreation", updatable=false)
    private LocalDate dateCreation;

    @Future
    private LocalDate dateCloture;
    @NotBlank
    @Size(max = 64)
    private String createur;

    /**
     * Constructeur avec paramètres.
     * @param description la description du sondage
     * @param question la question posée dans le sondage
     * @param dateCreation la date de création du sondage
     * @param dateCloture la date de clôture du sondage
     * @param createur le créateur du sondage
     */
    public Sondage(String description, String question, LocalDate dateCreation, LocalDate dateCloture, String createur) {
        this.description = description;
        this.question = question;
        this.dateCreation = dateCreation;
        this.dateCloture = dateCloture;
        this.createur = createur;
    }
    public Sondage(){

    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDate getDateCloture() {
        return dateCloture;
    }

    public void setDateCloture(LocalDate dateCloture) {
        this.dateCloture = dateCloture;
    }

    public String getCreateur() {
        return createur;
    }

    public void setCreateur(String createur) {
        this.createur = createur;
    }
}
