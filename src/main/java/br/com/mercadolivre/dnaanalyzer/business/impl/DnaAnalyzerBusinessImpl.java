package br.com.mercadolivre.dnaanalyzer.business.impl;

import br.com.mercadolivre.dnaanalyzer.business.DnaAnalyzerBusiness;
import br.com.mercadolivre.dnaanalyzer.exception.InvalidDnaException;
import br.com.mercadolivre.dnaanalyzer.exception.MissingParameterException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class DnaAnalyzerBusinessImpl implements DnaAnalyzerBusiness {

    private static final int SIZE_CHAIN = 4;
    private static final int QUANTITY_CHAINS = 2;
    private static final String VALID_LETTERS = "ATCG";

    public boolean isSimian(String[] dna) {
        this.validateDna(dna);

        final int chainsSize = dna.length;

        String horizontalChain;
        String verticalChain;
        String rightDiagonalChain;
        String leftDiagonalChain;

        int count = 0;

        for (int x = 0; x < chainsSize; x++) {

            for (int y = 0; y < chainsSize; y++) {

                horizontalChain = "";
                verticalChain = "";
                rightDiagonalChain = "";
                leftDiagonalChain = "";

                int horizontalX;
                int horizontalY;
                int verticalX;
                int verticalY;
                int rightDiagonalX;
                int rightDiagonalY;
                int leftDiagonalX;
                int leftDiagonalY;

                for (int i = 0; i < SIZE_CHAIN; i++) {

                    if (y <= chainsSize - SIZE_CHAIN) {
                        horizontalX = x;
                        horizontalY = y + i;

                        horizontalChain = horizontalChain.concat(String.valueOf(dna[horizontalX].charAt(horizontalY)));
                    }

                    if (x <= chainsSize - SIZE_CHAIN) {
                        verticalX = x + i;
                        verticalY = y;

                        verticalChain = verticalChain.concat(String.valueOf(dna[verticalX].charAt(verticalY)));
                    }

                    if ((y <= chainsSize - SIZE_CHAIN) && (x <= chainsSize - SIZE_CHAIN)) {
                        rightDiagonalX = x + i;
                        rightDiagonalY = y + i;

                        rightDiagonalChain =
                            rightDiagonalChain.concat(String.valueOf(dna[rightDiagonalX].charAt(rightDiagonalY)));
                    }

                    if ((y >= SIZE_CHAIN - 1) && (x <= chainsSize - SIZE_CHAIN)) {
                        leftDiagonalX = x + i;
                        leftDiagonalY = y - i;

                        leftDiagonalChain =
                            leftDiagonalChain.concat(String.valueOf(dna[leftDiagonalX].charAt(leftDiagonalY)));
                    }
                }

                if (horizontalChain.length() >= SIZE_CHAIN) {
                    count += verifyChain(horizontalChain) ? 1 : 0;
                }

                if (verticalChain.length() >= SIZE_CHAIN) {
                    count += verifyChain(verticalChain) ? 1 : 0;
                }

                if (rightDiagonalChain.length() >= SIZE_CHAIN) {
                    count += verifyChain(rightDiagonalChain) ? 1 : 0;
                }

                if (leftDiagonalChain.length() >= SIZE_CHAIN) {
                    count += verifyChain(leftDiagonalChain) ? 1 : 0;
                }

                if (count >= QUANTITY_CHAINS) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean verifyChain(final String chain) {
        String[] letters = chain.split("");

        String last = "";

        int count = 1;

        for (int i = 0; i < letters.length; i++) {

            if (letters[i].equals(last)) {
                count++;
            } else {
                count = 1;
            }

            last = letters[i];
        }

        return (count >= SIZE_CHAIN);
    }

    private void validateDna(final String[] dna) {
        final List<String> dnaList = Optional.ofNullable(dna)
            .filter(t -> t.length > 0)
            .map(Arrays::asList)
            .orElseThrow(() -> new MissingParameterException("dna"));

        final int dnaSize = dnaList.size();

        boolean isNotSquare = dnaList
            .stream()
            .anyMatch(t -> t == null || t.length() != dnaSize);

        if (isNotSquare) {
            throw new InvalidDnaException("The DNA matrix is not square");
        }

        dnaList
            .forEach(chain -> {

                final List<String> lettersChain = Optional.ofNullable(chain)
                    .map(t -> t.split(""))
                    .map(Arrays::asList)
                    .orElse(Collections.emptyList());

                final boolean hasInvalidLetter = lettersChain
                    .stream()
                    .anyMatch(t -> !VALID_LETTERS.contains(t));

                if (hasInvalidLetter) {
                    throw new InvalidDnaException("Invalid letter(s)");
                }
            });
    }

}
