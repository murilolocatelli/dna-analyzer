package br.com.mercadolivre.dnaanalyzer.business;

import br.com.mercadolivre.dnaanalyzer.dto.DnaStatsDto;

public interface DnaBusiness {

    boolean analyzeDna(String[] dna);

    DnaStatsDto getStats();

}
