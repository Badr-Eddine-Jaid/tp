package pharmacie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @NoArgsConstructor @ToString

public class Ligne {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Integer id;

    /* ==========================
       Relation vers Medicament
       ========================== */
    @ManyToOne(optional = false)
    @JoinColumn(
        name = "medicament_reference",         // colonne FK dans cette table
        referencedColumnName = "reference",    // clé primaire de Medicament
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_detail_medicament")
    )
    private Medicament medicament;

    /* ==========================
       Relation vers Commande
       ========================== */
    @ManyToOne(optional = false)
    @JoinColumn(
        name = "commande_numero",              // colonne FK dans cette table
        referencedColumnName = "numero",       // clé primaire de Commande
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_detail_commande")
    )
    private Commande commande;

    /* ==========================
       Quantité
       ========================== */
    @Min(1)
    @Column(nullable = false)
    private Integer quantite;
}