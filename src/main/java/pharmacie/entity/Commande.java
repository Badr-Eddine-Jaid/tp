package pharmacie.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @NoArgsConstructor @ToString
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Integer numero;

    @NotNull
    @Column(name = "saisie_le", nullable = false)
    private LocalDate saisieLe;

    @Column(name = "envoyee_le") // nullable => commande "en cours" si null
    private LocalDate envoyeeLe;

    @NotNull
    @Digits(integer = 16, fraction = 2)
    @Column(precision = 18, scale = 2, nullable = false)
    private BigDecimal port;

    @NotNull
    @Digits(integer = 8, fraction = 2)
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal remise;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dispensaire_code", nullable = false, referencedColumnName = "code")
    @ToString.Exclude
    private Dispensaire dispensaire;

    // ⚠️ IMPORTANT : ton SQL = CODE_POSTALE (avec e)
    @NotBlank
    @Size(max = 10)
    @Column(name = "code_postale", length = 10, nullable = false)
    private String codePostale;

    @NotBlank
    @Size(max = 50)
    @Column(length = 50, nullable = false)
    private String region;

    @NotBlank
    @Size(max = 15)
    @Column(length = 15, nullable = false)
    private String ville;

    @NotBlank
    @Size(max = 60)
    @Column(length = 60, nullable = false)
    private String adresse;

    // ✅ CONTRAINTE : supprimer une commande => supprime ses lignes
    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Ligne> lignes = new ArrayList<>();

    public void addLigne(Ligne ligne) {
    lignes.add(ligne);
    ligne.setCommande(this);
}

}
