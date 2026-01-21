package pharmacie.dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import pharmacie.entity.Categorie;
import pharmacie.entity.Commande;
import pharmacie.entity.Dispensaire;
import pharmacie.entity.Ligne;
import pharmacie.entity.Medicament;

@DataJpaTest
public class RepositoryCustomMethodsTest {

    @Autowired private CategorieRepository categorieRepository;
    @Autowired private MedicamentRepository medicamentRepository;
    @Autowired private DispensaireRepository dispensaireRepository;
    @Autowired private CommandeRepository commandeRepository;
    @Autowired private LigneRepository ligneRepository;

    @Test
    public void testMedicamentCustomMethods() {
        Medicament indisponible = medicamentRepository.findByNom("Lévofloxacine 500mg").orElseThrow();
        Medicament disponible   = medicamentRepository.findByNom("Doliprane Effervescent 1g").orElseThrow();

        List<Medicament> disponibles = medicamentRepository.findByIndisponibleFalse();

        assertTrue(disponibles.contains(disponible));
        assertFalse(disponibles.contains(indisponible));
        assertFalse(disponibles.isEmpty());
    }

    @Test
    public void testCategorieCustomMethods() {
        Categorie c1 = new Categorie();
        c1.setLibelle("AnalgesiquesTest");
        categorieRepository.saveAndFlush(c1);

        Categorie c2 = new Categorie();
        c2.setLibelle("AntibiotiquesTest");
        categorieRepository.saveAndFlush(c2);

        Categorie found = categorieRepository.findByLibelle("AnalgesiquesTest");
        assertNotNull(found);
        assertEquals("AnalgesiquesTest", found.getLibelle());

        List<Categorie> list = categorieRepository.findByLibelleContaining("iquesTest");
        assertEquals(2, list.size());
        assertTrue(list.stream().anyMatch(cat -> cat.getLibelle().equals("AntibiotiquesTest")));
        assertTrue(list.stream().anyMatch(cat -> cat.getLibelle().equals("AnalgesiquesTest")));
    }

    @Test
    public void testUnMedicamentDoitAvoirUneCategorie() {
        Medicament m = new Medicament();
        m.setNom("MedSansCategorie");
        m.setCategorie(null);

        assertThrows(DataIntegrityViolationException.class, () -> {
            medicamentRepository.saveAndFlush(m);
        });
    }

    @Test
    public void testSuppressionCategorieSansMedicaments_OK() {
        Categorie c = new Categorie();
        c.setLibelle("CategorieVide");
        Categorie saved = categorieRepository.saveAndFlush(c);

        categorieRepository.delete(saved);
        categorieRepository.flush();

        assertTrue(categorieRepository.findById(saved.getCode()).isEmpty());
    }

    @Test
    public void testSuppressionCategorieAvecMedicaments_INTERDITE() {
        Categorie c = new Categorie();
        c.setLibelle("CategorieAvecMed");
        Categorie savedCat = categorieRepository.saveAndFlush(c);

        Medicament m = new Medicament();
        m.setNom("MedDeCategorieAvecMed");
        m.setCategorie(savedCat);
        medicamentRepository.saveAndFlush(m);

        assertThrows(RuntimeException.class, () -> {
            categorieRepository.delete(savedCat);
            categorieRepository.flush();
        });
    }

    @Test
    public void testSupprimerCommandeSupprimeSesLignes() {
        Dispensaire savedDisp = createDispensaire();
        Categorie savedCat = createCategorie("CatCommande");
        Medicament savedMed = createMedicament("MedCommande", savedCat, false, 10, 0);

        Commande savedCmd = createCommandeEnvoyee(savedDisp);

        Ligne l1 = new Ligne();
        l1.setMedicament(savedMed);
        l1.setQuantite(3);
        savedCmd.addLigne(l1);

        commandeRepository.saveAndFlush(savedCmd);


        Integer idLigne = savedLigne.getId();
        Integer idCommande = savedCmd.getNumero();

        commandeRepository.deleteById(idCommande);
        commandeRepository.flush();

        assertTrue(commandeRepository.findById(idCommande).isEmpty());
        assertTrue(ligneRepository.findById(idLigne).isEmpty());
    }

    @Test
    public void testSupprimerDispensaireSupprimeCommandesEtLignes() {
        Dispensaire savedDisp = createDispensaire();
        Categorie savedCat = createCategorie("CatDisp");
        Medicament savedMed = createMedicament("MedDisp", savedCat, false, 10, 0);

        Commande savedCmd = createCommandeEnvoyee(savedDisp);

        Ligne l = new Ligne();
        l.setCommande(savedCmd);
        l.setMedicament(savedMed);
        l.setQuantite(2);
        Ligne savedLigne = ligneRepository.saveAndFlush(l);

        Integer idCommande = savedCmd.getNumero();
        Integer idLigne = savedLigne.getId();
        Integer codeDisp = savedDisp.getCode();

        dispensaireRepository.deleteById(codeDisp);
        dispensaireRepository.flush();

        assertTrue(dispensaireRepository.findById(codeDisp).isEmpty());
        assertTrue(commandeRepository.findById(idCommande).isEmpty());
        assertTrue(ligneRepository.findById(idLigne).isEmpty());
    }

    @Test
    public void testNombreArticlesEnvoyesParDispensaire() {
        Dispensaire savedDisp = createDispensaire();
        Integer code = savedDisp.getCode();
        Categorie savedCat = createCategorie("CatReq1");
        Medicament savedMed = createMedicament("MedReq1", savedCat, false, 10, 0);

        Commande envoyee = createCommandeEnvoyee(savedDisp);
        Ligne l1 = new Ligne();
        l1.setCommande(envoyee);
        l1.setMedicament(savedMed);
        l1.setQuantite(7);
        ligneRepository.saveAndFlush(l1);


        Commande enCours = createCommandeEnCours(savedDisp);
        Ligne l3 = new Ligne(); l3.setCommande(enCours); l3.setMedicament(savedMed); l3.setQuantite(100);
        ligneRepository.saveAndFlush(l3);

        Long total = ligneRepository.totalArticlesEnvoyesByDispensaireCode(code);
        assertEquals(7L, total);
    }

    @Test
    public void testCommandesEnCoursParDispensaire() {
        Dispensaire savedDisp = createDispensaire();
        Integer code = savedDisp.getCode();

        createCommandeEnvoyee(savedDisp);
        createCommandeEnCours(savedDisp);
        createCommandeEnCours(savedDisp);

        List<Commande> enCours = commandeRepository
                .findByDispensaireCodeAndEnvoyeeLeIsNull(code);

        assertEquals(2, enCours.size());
        assertTrue(enCours.stream().allMatch(c -> c.getEnvoyeeLe() == null));
    }

    @Test
    public void testMedicamentsDisponiblesALaCommandePourCategorie() {
        Categorie savedCat = createCategorie("CatReq3");

        Medicament ok = createMedicament("MedOKReq3", savedCat, false, 10, 5);

        createMedicament("MedIndispoReq3", savedCat, true, 10, 0);
        createMedicament("MedStockKOReq3", savedCat, false, 1, 5);

        List<Medicament> res = medicamentRepository
                .findDisponiblesALaCommandeByCategorieCode(savedCat.getCode());

        assertEquals(1, res.size());
        assertEquals(ok.getNom(), res.get(0).getNom());
    }

    // ----------------------------
    // Helpers
    // ----------------------------

    private Categorie createCategorie(String libelle) {
        Categorie c = new Categorie();
        c.setLibelle(libelle);
        return categorieRepository.saveAndFlush(c);
    }

    private Medicament createMedicament(String nom, Categorie cat, boolean indispo, int stock, int commandees) {
        Medicament m = new Medicament();
        m.setNom(nom);
        m.setCategorie(cat);
        m.setIndisponible(indispo);
        m.setUnitesEnStock(stock);
        m.setUnitesCommandees(commandees);
        return medicamentRepository.saveAndFlush(m);
    }

    private Dispensaire createDispensaire() {
        Dispensaire d = new Dispensaire();
        d.setNom("Disp Test");
        d.setAdresse("Rue 1");
        d.setCodePostal("75000");
        d.setVille("Paris");
        d.setRegion("IDF");
        d.setPays("France");
        return dispensaireRepository.saveAndFlush(d);
    }

    private Commande createCommandeEnvoyee(Dispensaire d) {
        Commande c = baseCommande(d);
        c.setEnvoyeeLe(LocalDate.now());
        return commandeRepository.saveAndFlush(c);
    }

    private Commande createCommandeEnCours(Dispensaire d) {
        Commande c = baseCommande(d);
        c.setEnvoyeeLe(null);
        return commandeRepository.saveAndFlush(c);
    }

    private Commande baseCommande(Dispensaire d) {
        Commande c = new Commande();
        c.setDispensaire(d);
        c.setSaisieLe(LocalDate.now());
        c.setPort(new BigDecimal("10.00"));
        c.setRemise(new BigDecimal("0.00"));

        // ⚠️ IMPORTANT : ton SQL utilise CODE_POSTALE (avec e)
        c.setCodePostale("75000");
        c.setRegion("IDF");
        c.setVille("Paris");
        c.setAdresse("Rue 1");

        return c;
    }
}
