package pharmacie.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pharmacie.entity.Medicament;

public interface MedicamentRepository extends JpaRepository<Medicament, Integer> {

    Optional<Medicament> findByNom(String nom);

    List<Medicament> findByIndisponibleFalse();

    @Query("""
      select m
      from Medicament m
      where m.categorie.code = :categorieCode
        and m.indisponible = false
        and m.unitesEnStock >= m.unitesCommandees
    """)
    List<Medicament> findDisponiblesALaCommandeByCategorieCode(@Param("categorieCode") Integer categorieCode);
}
