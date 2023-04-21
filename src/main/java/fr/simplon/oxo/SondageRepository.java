package fr.simplon.oxo;
import fr.simplon.oxo.entity.Sondage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SondageRepository extends JpaRepository<Sondage, Long> {
}
