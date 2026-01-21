package pharmacie.entity;

import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @NoArgsConstructor @ToString
public class Categorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Integer code;

    @NotBlank
    @Size(max = 255)
    @Column(unique = true, length = 255, nullable = false)
    private String libelle;

    @Size(max = 255)
    @Column(length = 255)
    private String description;

    // ⚠️ PAS de cascade REMOVE ici, sinon supprimer catégorie supprimerait les médicaments.
    @OneToMany(mappedBy = "categorie")
    @ToString.Exclude
    private List<Medicament> medicaments = new LinkedList<>();
}
