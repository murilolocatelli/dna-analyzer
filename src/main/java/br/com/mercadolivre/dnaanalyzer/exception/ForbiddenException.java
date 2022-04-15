package br.com.mercadolivre.dnaanalyzer.exception;

public class ForbiddenException extends BaseException {

    private static final long serialVersionUID = 8185279854726293181L;

    public ForbiddenException(final String... parameters) {
        super(parameters);
    }

}
