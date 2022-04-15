package br.com.mercadolivre.dnaanalyzer.security.handler;

import static java.text.MessageFormat.format;

import br.com.mercadolivre.dnaanalyzer.dto.ResponseError;
import br.com.mercadolivre.dnaanalyzer.service.JsonService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private JsonService jsonService;

    @Override
    public void commence(
        final HttpServletRequest request, final HttpServletResponse response,
        final AuthenticationException authException) throws IOException {

        final ResponseError responseError = ResponseError.builder()
            .message(format("Unauthorized: {0}", authException.getMessage()))
            .build();

        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().println(this.jsonService.toJsonString(responseError));
    }

}
