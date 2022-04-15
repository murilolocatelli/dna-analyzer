package br.com.mercadolivre.dnaanalyzer.business.test;

import br.com.mercadolivre.dnaanalyzer.business.DnaAnalyzerBusiness;
import br.com.mercadolivre.dnaanalyzer.business.impl.DnaAnalyzerBusinessImpl;
import br.com.mercadolivre.dnaanalyzer.exception.InvalidDnaException;
import br.com.mercadolivre.dnaanalyzer.exception.MissingParameterException;
import br.com.mercadolivre.dnaanalyzer.test.BaseUnitTest;

import org.junit.Assert;
import org.junit.Test;

public class DnaAnalyzerBusinessTest extends BaseUnitTest {

    private DnaAnalyzerBusiness dnaAnalyzerBusiness = new DnaAnalyzerBusinessImpl();

    @Test
    public void testIsSimianHorizontal() {
        final String[] dna = {
            "TTTTA",
            "ACCCC",
            "TCGAA",
            "CGATC",
            "TCGAG"};

        final boolean simian = this.dnaAnalyzerBusiness.isSimian(dna);

        Assert.assertTrue(simian);
    }

    @Test
    public void testIsSimianVertical() {
        final String[] dna = {
            "ATCGA",
            "ATATC",
            "ATGAA",
            "ATATC",
            "TCGAG"};

        final boolean simian = this.dnaAnalyzerBusiness.isSimian(dna);

        Assert.assertTrue(simian);
    }

    @Test
    public void testIsSimianRightDiagonal() {
        final String[] dna = {
            "ATCGA",
            "CAATC",
            "TCAAA",
            "CGCAC",
            "TCGCG"};

        final boolean simian = this.dnaAnalyzerBusiness.isSimian(dna);

        Assert.assertTrue(simian);
    }

    @Test
    public void testIsSimianLeftDiagonal() {
        final String[] dna = {
            "ATCGA",
            "CGAAC",
            "TCACA",
            "CACTC",
            "TCGAG"};

        final boolean simian = this.dnaAnalyzerBusiness.isSimian(dna);

        Assert.assertTrue(simian);
    }

    @Test
    public void testSimianFalse() {
        final String[] dna = {
            "ATCGA",
            "CGATC",
            "TCGAA",
            "CGATC",
            "TCGAG"};

        final boolean simian = this.dnaAnalyzerBusiness.isSimian(dna);

        Assert.assertFalse(simian);
    }

    @Test(expected = MissingParameterException.class)
    public void testMissingDna() {
        this.dnaAnalyzerBusiness.isSimian(null);
    }

    @Test(expected = MissingParameterException.class)
    public void testEmptyDna() {
        final String[] dna = {};

        this.dnaAnalyzerBusiness.isSimian(dna);
    }

    @Test
    public void testInvalidMatrixNotSquare() {
        final String[] dna = {
            "ATCGA",
            "CGATC"};

        try {
            this.dnaAnalyzerBusiness.isSimian(dna);
        } catch (InvalidDnaException ex) {
            Assert.assertEquals(ex.getParameters()[0], "The DNA matrix is not square");

            return;
        }

        Assert.fail();
    }

    @Test
    public void testInvalidMatrixNotSquareNull() {
        final String[] dna = {
            "ATCGA",
            "CGATC",
            "TCGAA",
            null,
            "TCGAG"};

        try {
            this.dnaAnalyzerBusiness.isSimian(dna);
        } catch (InvalidDnaException ex) {
            Assert.assertEquals(ex.getParameters()[0], "The DNA matrix is not square");

            return;
        }

        Assert.fail();
    }

    @Test
    public void testInvalidMatrixNotSquareEmpty() {
        final String[] dna = {
            "ATCGA",
            "CGATC",
            "TCGAA",
            "",
            "TCGAG"};

        try {
            this.dnaAnalyzerBusiness.isSimian(dna);
        } catch (InvalidDnaException ex) {
            Assert.assertEquals(ex.getParameters()[0], "The DNA matrix is not square");

            return;
        }

        Assert.fail();
    }

    @Test
    public void testInvalidateLetters() {
        final String[] dna = {
            "ATCGA",
            "CGATC",
            "BBBBB",
            "CGATC",
            "TCGAG"};

        try {
            this.dnaAnalyzerBusiness.isSimian(dna);
        } catch (InvalidDnaException ex) {
            Assert.assertEquals(ex.getParameters()[0], "Invalid letter(s)");

            return;
        }

        Assert.fail();
    }

}
