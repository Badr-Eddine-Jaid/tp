package pharmacie.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pharmacie.entity.Commande;
import pharmacie.entity.Ligne;
import pharmacie.entity.Medicament;

public interface LigneRepository extends JpaRepository<Ligne, Integer> {

    List<Ligne> findByCommande(Commande commande);

    List<Ligne> findByMedicament(Medicament medicament);

    // ✅ IMPORTANT : param = Integer (Dispensaire.code)
    @Query("""
      select coalesce(sum(l.quantite), 0)
      from Ligne l
      where l.commande.dispensaire.code = :dispCode
        and l.commande.envoyeeLe is not null
    """)
    Long totalArticlesEnvoyesByDispensaireCode(@Param("dispCode") Integer dispCode);
}
