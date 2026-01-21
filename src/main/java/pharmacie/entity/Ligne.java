package pharmacie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(
  uniqueConstraints = @UniqueConstraint(
    name = "uk_ligne_commande_medicament",
    columnNames = {"commande_numero", "medicament_reference"}
  )
)
@Getter @Setter @NoArgsConstructor @ToString
public class Ligne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medicament_reference", referencedColumnName = "reference", nullable = false)
    @ToString.Exclude
    private Medicament medicament;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "commande_numero", referencedColumnName = "numero", nullable = false)
    @ToString.Exclude
    private Commande commande;

    @Min(1)
    @Column(nullable = false)
    private Integer quantite;
}
