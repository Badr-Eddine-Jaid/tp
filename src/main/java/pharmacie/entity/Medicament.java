package pharmacie.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @NoArgsConstructor @ToString
public class Medicament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Integer reference;

    @NotBlank
    @Column(unique = true, length = 255, nullable = false)
    private String nom;

    @Column(name = "quantite_par_unite", length = 255)
    private String quantiteParUnite = "Une boîte de 12";

    @PositiveOrZero
    @Column(name = "prix_unitaire", precision = 10, scale = 2)
    private BigDecimal prixUnitaire = BigDecimal.TEN;

    @PositiveOrZero
    @Column(name = "unites_en_stock")
    private int unitesEnStock = 0;

    @PositiveOrZero
    @Column(name = "unites_commandees")
    private int unitesCommandees = 0;

    @PositiveOrZero
    @Column(name = "niveau_de_reappro")
    private int niveauDeReappro = 0;

    @Column(name = "indisponible")
    private boolean indisponible = false;

    @Column(name = "image_url", length = 500)
    private String imageURL;

    // CONTRAINTE : un médicament DOIT avoir une catégorie
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categorie_code", nullable = false)
    @ToString.Exclude
    private Categorie categorie;
}
