package pharmacie.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pharmacie.entity.Dispensaire;

public interface DispensaireRepository extends JpaRepository<Dispensaire, Integer> {
    Optional<Dispensaire> findByNom(String nom);
    List<Dispensaire> findByVille(String ville);
    List<Dispensaire> findByPays(String pays);
    List<Dispensaire> findByRegion(String region);
}
