package pharmacie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @NoArgsConstructor @ToString
public class Dispensaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Integer code;

    @NotBlank
    @Size(min = 1, max = 100)
    @Column(nullable = false, length = 100)
    private String nom;

    @NotBlank
    @Size(min = 1, max = 60)
    @Column(nullable = false, length = 60)
    private String adresse;

    @NotBlank
    @Size(min = 1, max = 10)
    @Column(name = "code_postal", nullable = false, length = 10)
    private String codePostal;

    @NotBlank
    @Size(min = 1, max = 15)
    @Column(nullable = false, length = 15)
    private String ville;

    @NotBlank
    @Size(min = 1, max = 50)
    @Column(nullable = false, length = 50)
    private String region;

    @NotBlank
    @Size(min = 1, max = 100)
    @Column(nullable = false, length = 100)
    private String pays;

    @Size(max = 20)
    @Pattern(
        regexp = "^\\+?[0-9\\s-]{6,20}$",
        message = "Numéro de téléphone invalide"
    )
    @Column(length = 20)
    private String telephone;

    @Size(max = 20)
    @Pattern(
        regexp = "^\\+?[0-9\\s-]{6,20}$",
        message = "Numéro de fax invalide"
    )
    @Column(length = 20)
    private String fax;

    @Size(max = 100)
    @Column(length = 100)
    private String contact;

    @Size(max = 100)
    @Column(length = 100)
    private String fonction;
}