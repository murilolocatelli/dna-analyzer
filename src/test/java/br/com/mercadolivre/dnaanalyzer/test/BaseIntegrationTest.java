package br.com.mercadolivre.dnaanalyzer.test;

import br.com.mercadolivre.dnaanalyzer.DnaAnalyzerApplication;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DnaAnalyzerApplication.class)
@Transactional
public abstract class BaseIntegrationTest {

}
