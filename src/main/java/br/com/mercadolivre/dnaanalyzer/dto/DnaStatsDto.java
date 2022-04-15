package br.com.mercadolivre.dnaanalyzer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
public class DnaStatsDto implements Serializable {

    private static final long serialVersionUID = 2019549117432946131L;

    @JsonProperty(value = "count_mutant_dna")
    private Long countMutantDna;

    @JsonProperty(value = "count_human_dna")
    private Long countHumanDna;

    @JsonProperty(value = "ratio")
    private Double ratio;

}
