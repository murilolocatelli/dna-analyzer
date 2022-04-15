package br.com.mercadolivre.dnaanalyzer.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public abstract class DecimalUtils {

    public static Double format(Double value, int decimalPlaces) {
        return Optional.ofNullable(value)
            .map(BigDecimal::new)
            .map(t -> t.setScale(decimalPlaces, RoundingMode.HALF_UP))
            .map(BigDecimal::doubleValue)
            .orElse(null);
    }

}
