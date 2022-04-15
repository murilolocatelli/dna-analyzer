package br.com.mercadolivre.dnaanalyzer.business.impl;

import br.com.mercadolivre.dnaanalyzer.business.DnaAnalyzerBusiness;
import br.com.mercadolivre.dnaanalyzer.business.DnaBusiness;
import br.com.mercadolivre.dnaanalyzer.dto.DnaStatsDto;
import br.com.mercadolivre.dnaanalyzer.enumeration.DnaType;
import br.com.mercadolivre.dnaanalyzer.exception.EntityAlreadyExistsException;
import br.com.mercadolivre.dnaanalyzer.exception.EntityNotFoundException;
import br.com.mercadolivre.dnaanalyzer.model.DnaHistory;
import br.com.mercadolivre.dnaanalyzer.repository.DnaHistoryRepository;
import br.com.mercadolivre.dnaanalyzer.util.DecimalUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DnaBusinessImpl implements DnaBusiness {

    @Autowired
    private DnaAnalyzerBusiness dnaAnalyzerBusiness;

    @Autowired
    private DnaHistoryRepository dnaHistoryRepository;

    @Transactional
    public boolean analyzeDna(final String[] dna) {
        boolean existsDna = this.dnaHistoryRepository.existsByDna(Arrays.toString(dna));

        if (existsDna) {
            throw new EntityAlreadyExistsException("DNA");
        }

        final boolean isSimian = this.dnaAnalyzerBusiness.isSimian(dna);

        final DnaHistory dnaHistory = DnaHistory.builder()
            .dna(Arrays.toString(dna))
            .dnaType(isSimian ? DnaType.SIMIAN : DnaType.HUMAN)
            .build();

        this.dnaHistoryRepository.save(dnaHistory);

        return isSimian;
    }

    public DnaStatsDto getStats() {

        final List<DnaHistory> dnaHistories = this.dnaHistoryRepository.findAll();

        Optional.ofNullable(dnaHistories)
            .filter(t -> !t.isEmpty())
            .orElseThrow(() -> new EntityNotFoundException("DNA History"));

        final var countHuman = new AtomicReference<>(0L);
        final var countMutant = new AtomicReference<>(0L);

        dnaHistories.forEach(t -> {
            if (t.getDnaType().equals(DnaType.HUMAN)) {
                countHuman.updateAndGet(count -> count + 1);
            } else if (t.getDnaType().equals(DnaType.SIMIAN)) {
                countMutant.updateAndGet(count -> count + 1);
            }
        });

        final Double ratio = countHuman.get() != 0 && countMutant.get() != 0
            ? (countMutant.get().doubleValue() / countHuman.get().doubleValue())
            : null;

        return DnaStatsDto.builder()
            .countHumanDna(countHuman.get())
            .countMutantDna(countMutant.get())
            .ratio(DecimalUtils.format(ratio, 2))
            .build();
    }

}
