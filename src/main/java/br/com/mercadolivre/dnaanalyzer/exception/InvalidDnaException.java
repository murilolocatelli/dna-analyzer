package br.com.mercadolivre.dnaanalyzer.exception;

public class InvalidDnaException extends BaseException {

    private static final long serialVersionUID = 8986002676898256902L;

    public InvalidDnaException(String... parameters) {
        super(parameters);
    }

}
