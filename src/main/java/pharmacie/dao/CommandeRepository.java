package pharmacie.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pharmacie.entity.Commande;
import pharmacie.entity.Dispensaire;

public interface CommandeRepository extends JpaRepository<Commande, Integer> {

    List<Commande> findByDispensaire(Dispensaire dispensaire);

    List<Commande> findByEnvoyeeLeAfter(LocalDate date);

    List<Commande> findBySaisieLeBefore(LocalDate date);

    List<Commande> findBySaisieLeAfter(LocalDate date);

    // ✅ IMPORTANT : dispensaireCode est Integer (Dispensaire.code = Integer)
    List<Commande> findByDispensaireCodeAndEnvoyeeLeIsNull(Integer dispensaireCode);
}
