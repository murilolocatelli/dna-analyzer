package br.com.mercadolivre.dnaanalyzer.repository;

import br.com.mercadolivre.dnaanalyzer.model.DnaHistory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DnaHistoryRepository extends JpaRepository<DnaHistory, Long>  {

    boolean existsByDna(String dna);

}
