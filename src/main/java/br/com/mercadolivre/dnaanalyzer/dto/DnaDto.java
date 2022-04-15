package br.com.mercadolivre.dnaanalyzer.dto;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotEmpty;

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
public class DnaDto implements Serializable {

    private static final long serialVersionUID = -1877746162686597019L;

    @NotEmpty
    private List<String> dna;

}
