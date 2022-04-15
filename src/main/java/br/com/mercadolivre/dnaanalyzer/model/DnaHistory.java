package br.com.mercadolivre.dnaanalyzer.model;

import br.com.mercadolivre.dnaanalyzer.enumeration.DnaType;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "dna_history")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
@Builder
public class DnaHistory  implements Serializable {

    private static final long serialVersionUID = 8228445718761359810L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String dna;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DnaType dnaType;

    @Column(nullable = false)
    private ZonedDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = ZonedDateTime.now();
    }

}
