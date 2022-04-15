package br.com.mercadolivre.dnaanalyzer.controller;

import br.com.mercadolivre.dnaanalyzer.business.DnaBusiness;
import br.com.mercadolivre.dnaanalyzer.dto.DnaDto;
import br.com.mercadolivre.dnaanalyzer.exception.ForbiddenException;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class DnaController extends BaseController {

    @Autowired
    private DnaBusiness dnaBusiness;

    @PostMapping(value = "/simian", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Post DNA Simian"),
        @ApiResponse(code = 403, message = "Post DNA Human"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 401, message = "Unauthorized")
    })
    public ResponseEntity<Object> postDna(@RequestBody @Valid final DnaDto dnaDto) {

        final boolean isSimian =
            this.dnaBusiness.analyzeDna(dnaDto.getDna().stream().map(String::new).toArray(String[]::new));

        if (!isSimian) {
            throw new ForbiddenException();
        }

        return super.buildResponse(HttpStatus.OK, null);
    }

    @GetMapping("/stats")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Get Status"),
        @ApiResponse(code = 400, message = "DNA History Not Found"),
        @ApiResponse(code = 401, message = "Unauthorized"),
    })
    public ResponseEntity<Object> getStats() {
        return super.buildResponse(HttpStatus.OK, this.dnaBusiness.getStats());
    }

}
